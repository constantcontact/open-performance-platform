package com.opp.service;

import com.opp.domain.LoadTest;
import com.opp.domain.LoadTestData;
import com.opp.dto.aggregate.LoadTestAggregateDataResp;
import com.opp.dto.datagen.DataGenRequest;
import com.opp.dto.datagen.DataGenResponse;
import com.opp.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

/**
 * Created by ctobe on 7/13/16.
 */
@Service
public class DataGenService {

    @Autowired
    private LoadTestService loadTestService;

    @Autowired
    private LoadTestAggregateService loadTestAggregateService;

    @Autowired
    private LoadTestDataService loadTestDataService;

    /**
     * Main data generation function
     * @param dataGenRequest
     * @return
     */
    public List<DataGenResponse> generateData(DataGenRequest dataGenRequest) {
        // insertAll load tests
        List<Optional<LoadTest>> loadTests = createLoadTests(dataGenRequest);
        List<DataGenResponse> dataGenResponseList = loadTests.stream()
                .filter(loadTest -> loadTest.isPresent())
                .map(loadTest -> {
                    // load raw data
                    List<LoadTestData> loadTestDataList = generateLoadTestData(loadTest.get(), dataGenRequest);

                    // batch insert load test data objects
                    int recordsAdded = loadTestDataService.addAll(loadTest.get().getId(), loadTestDataList);

                    // aggregate data if needed
                    LoadTestAggregateDataResp loadTestAggregateDataResp = (dataGenRequest.isAggregateData()) ?
                            loadTestAggregateService.aggregateLoadTestData(loadTest.get().getId()) : null;

                    // get unique transactions per load test
                    Map<String, Long> transactionCounts = loadTestDataList.stream().collect(
                            groupingBy(LoadTestData::getTransactionName, counting()));

                    // save details to map
                    DataGenResponse dataGenResponse = new DataGenResponse();
                    dataGenResponse.setLoadTestId(loadTest.get().getId());
                    dataGenResponse.setLoadTest(loadTest.get());
                    dataGenResponse.setLoadTestData(loadTestDataList);
                    dataGenResponse.setDataCount(recordsAdded);
                    dataGenResponse.setTransactionBreakDown(transactionCounts);
                    dataGenResponse.setAggregates(loadTestAggregateDataResp);
                    return dataGenResponse;
                })
                .collect(toList());



        dataGenResponseList.forEach( dataGenResponse -> {
            System.out.println("---------- New load test ---------");
            System.out.println("Test Id: " + dataGenResponse.getLoadTestId());
            System.out.println("Load Test: " + dataGenResponse.getLoadTest());
            System.out.println("Data: " + dataGenResponse.getLoadTestData());
            System.out.println("Data Count: " + dataGenResponse.getDataCount());
            System.out.println("Transaction Breakdown: " + dataGenResponse.getTransactionBreakDown());
            System.out.println("Aggregates: " + dataGenResponse.getAggregates());
        });

        return dataGenResponseList;

    }

    /**
     * Creates a load tests
     * @param dataGenRequest
     * @return
     */
    private List<Optional<LoadTest>> createLoadTests(DataGenRequest dataGenRequest) {
        String testNameSuffix = String.format("data=%d-%d|tran=%d-%d|agg=%s|%s", dataGenRequest.getMinRawDataPerLoadTest(), dataGenRequest.getMaxRawDataPerLoadTest(), dataGenRequest.getMinTransactionsPerLoadTest(), dataGenRequest.getMaxTransactionsPerLoadTest(), dataGenRequest.isAggregateData(), dataGenRequest.getTestNameSuffix());
        return IntStream.range(0, dataGenRequest.getNumOfLoadTests())
               .mapToObj((i) -> loadTestService.add(generateLoadTestObj(testNameSuffix, dataGenRequest.getLoadTestObjOverrides())))
               .collect(toList());
    }

    /***
     * Generates a load test object
     * @param testNameSuffix - the suffix to be added to the end of the loadTestName
     * @param loadTestOverrides - A hashmap of LoadTest properties.  Reflection will be used to override value on the LoadTest object based on the property name (key) in the hashmap
     * @return
     */
    private LoadTest generateLoadTestObj(String testNameSuffix, Map<String, Object> loadTestOverrides) {
        LoadTest loadTest = new LoadTest();
        loadTest.setTestName(String.format("Generated Data Test #%d - %s", getRandomNumber(1000, 10000000), testNameSuffix));
        loadTest.setAppUnderTest("Fake App");
        loadTest.setAppUnderTestVersion("1.0");
        // loadTest.setComments(""); // intentionally left blank
        loadTest.setDescription("My Load test rocks");
        int hour = getRandomNumber(1, 12);
        int min = getRandomNumber(0, 59);
        loadTest.setStartTime(ZonedDateTime.now(ZoneOffset.UTC).withHour(hour).withMinute(min).withSecond(0).toEpochSecond());
        loadTest.setEndTime(ZonedDateTime.now(ZoneOffset.UTC).withHour(hour + 1).withMinute(min).withSecond(0).toEpochSecond());// one hour later
        loadTest.setEnvironment("s1");
        loadTest.setTestTool("CloudTest");
        loadTest.setTestToolVersion("2.0.0");
        loadTest.setVuserCount(getRandomNumber(10, 1000));
        loadTest.setExternalTestId("234234234");
        // apply overrides
        if(loadTestOverrides != null) {
            loadTestOverrides.entrySet().stream().forEach(e -> ReflectionUtil.setProperty(loadTest, e.getKey(), e.getValue()));
        }

        return loadTest;
    }

    /**
     * Creates a LoadTest object
     * @return
     */
    private LoadTest generateLoadTestObj(String testNameSuffix) {
        return generateLoadTestObj(testNameSuffix, new HashMap<>());
    }

    /**
     * Creates a LoadTestData object
     * @param loadTestId
     * @param transactionNum
     * @return
     */
    private LoadTestData generateLoadTestDataObj(int loadTestId, int transactionNum, long startTime){
        LoadTestData loadTestData = new LoadTestData();
        loadTestData.setTransactionName("Generated Transaction Name " + transactionNum);
        loadTestData.setLoadTestId(loadTestId);
        loadTestData.setResponseTime(getRandomNumber(20, 2000));
        loadTestData.setStartTime(startTime);
        return loadTestData;
    }




    private List<LoadTestData> generateLoadTestData(LoadTest loadTest, DataGenRequest dataGenRequest){
        // NOTE: if numOfRawDataRecords / transactionNum is not even divisible, the first few transactions might have 1 more LoadTestData objects

        // get random number of raw records to generate for a given test
        int numOfRawDataRecords = getRandomNumber(dataGenRequest.getMinRawDataPerLoadTest(), dataGenRequest.getMaxRawDataPerLoadTest());
        // get the number of transactions per test to generate
        int transactionNum = getRandomNumber(dataGenRequest.getMinTransactionsPerLoadTest(), dataGenRequest.getMaxTransactionsPerLoadTest());

        // calculate time to insertAll to each LoadTestData object
        long startTime = loadTest.getStartTime();
        long totalTestDuration = loadTest.getEndTime() - startTime;
        long incrementTimeBy = (long)Math.floor((double)totalTestDuration / (double)numOfRawDataRecords);

        List<LoadTestData> loadTestDataList = new ArrayList<>();
        int transNameCount = 1; // used to name the transaction
        // loop through the count of LoadTestData objects I need to add
        for(int i=1; i<=numOfRawDataRecords; i++){
            // generate LoadTestData obj and insertAll to list
            loadTestDataList.add(generateLoadTestDataObj(loadTest.getId(), transNameCount, startTime));
            // increment name count to set the new name for the next transaction
            transNameCount++;
            // if transNameCount gets higher than the max trans allowed reset back to 1
            if(transNameCount > transactionNum) transNameCount = 1;
            // increment the start time
            startTime = startTime + incrementTimeBy;
        }
        return loadTestDataList;
    }

    /**
     * Generates a random number
     * @param min
     * @param max
     * @return
     */
    private int getRandomNumber(int min, int max){
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
