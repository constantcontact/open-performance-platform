package com.opp.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.opp.BaseIntegrationTest;
import com.opp.dto.datagen.DataGenRequest;
import com.opp.dto.datagen.DataGenResponse;
import com.opp.service.DataGenService;
import com.opp.service.LoadTestAggregateService;
import com.opp.service.LoadTestDataService;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ctobe on 6/30/16.
 */
public class MaintenanceControllerTest extends BaseIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataGenService dataGenService;

    @Autowired
    LoadTestAggregateService loadTestAggregateService;

    @Autowired
    LoadTestDataService loadTestDataService;


    @After
    public void tearDown() throws Exception {
        //int recordsDeleted = jdbcTemplate.update("delete from load_test where id = 1");
        //System.out.println("Deleted " + recordsDeleted + " records");
    }

    /**
     * Re-aggregate all data.  Don't run this on a large system or while other tests are running as it can break them
     * @throws Exception
     */
    @Test
    @Ignore
    public void reAggregateData() throws Exception {

        // reaggregate data
        // Create - POST
        HttpResponse<JsonNode> jsonCreateResp = Unirest.post(getBaseUrl() + "/loadsvc/v1/maintenance/reaggregatedata").asJson();
        assertTrue("Reaggregate Data - Verifying Response Code", jsonCreateResp.getStatus() == 201);
        // could definitely addAll more tests here but this should really get run unless absolutely needed
    }

    @Test
    public void cleanApiLogs() throws Exception {

        // reaggregate data
        // Create - POST
        HttpResponse<JsonNode> jsonCleanLogsResp = Unirest.post(getBaseUrl() + "/loadsvc/v1/maintenance/cleanlogs").asJson();
        assertTrue("Clean Logs - Verifying Response Code", jsonCleanLogsResp.getStatus() == 202);

    }

    @Test
    public void deleteTransactions() throws Exception {
        // generate data
        int loadTests = 3, minRawData = 9, maxRawData = 9, minTrans = 3, maxTrans = 3;
        List<DataGenResponse> dataGenResponseList = dataGenService.generateData(new DataGenRequest(loadTests, minTrans, maxTrans, minRawData, maxRawData, true));

        // get generated test and data for later comparison
        DataGenResponse generatedTest = dataGenResponseList.get(0);
        int rawDataCount = generatedTest.getLoadTestData().size();
        int transactionCount = generatedTest.getTransactionBreakDown().size();
        String transNameToDelete = generatedTest.getTransactionBreakDown().entrySet().iterator().next().getKey();

        // build delete transaction request object
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("testName", generatedTest.getLoadTest().getTestName());
        jsonObj.put("transactionName", transNameToDelete);
        // make API call
        HttpResponse<JsonNode> jsonResp = Unirest.delete(getBaseUrl() + "/loadsvc/v1/maintenance/transaction").body(jsonObj).asJson();
        // verify response code
        assertTrue("Delete Transactions - Verifying Response Code", jsonResp.getStatus() == 202);

        // get response data
        int rawDataRowsRemoved = jsonResp.getBody().getObject().getInt("rawDataRowsRemoved");
        int aggregateDataRowsRemoved = jsonResp.getBody().getObject().getInt("aggregateDataRowsRemoved");

        // assert correct data was removed
        assertEquals("Delete Transactions - Raw data removed", 3, rawDataRowsRemoved);
        assertEquals("Delete Transactions - Aggregate data removed", 1, aggregateDataRowsRemoved);
        assertEquals("Delete Transactions - Total aggregates left in test", transactionCount - aggregateDataRowsRemoved, loadTestAggregateService.getByLoadTestId(generatedTest.getLoadTestId()).size());
        assertEquals("Delete Transactions - Total raw data left in test", rawDataCount - rawDataRowsRemoved, loadTestDataService.getAllByLoadTestId(generatedTest.getLoadTestId()).size());

    }

    /**
     * Cleans all the test data.  By default, do NOT run this test as it can break other tests as it deletes the data created by other tests
     * @throws Exception
     */
    @Test
    @Ignore
    public void cleanTestData() throws Exception {
        // verify 202 response and baseline system at 0 tests
        HttpResponse<JsonNode> jsonResp = Unirest.delete(getBaseUrl() + "/loadsvc/v1/maintenance/cleanTestData").asJson();
        assertEquals("Maint. - Delete test data - Verify Response Code", 202, jsonResp.getStatus());
        System.out.println("Maint. - Number of tests deleted: " + jsonResp.getBody().getObject().getInt("testsDeleted"));

        // the 2nd call should result in 3 tests deleted
        int loadTests = 3, minRawData = 9, maxRawData = 9, minTrans = 3, maxTrans = 3;
        dataGenService.generateData(new DataGenRequest(loadTests, minTrans, maxTrans, minRawData, maxRawData, true));
        HttpResponse<JsonNode> jsonResp2 = Unirest.delete(getBaseUrl() + "/loadsvc/v1/maintenance/cleanTestData").asJson();
        assertEquals("Maint. - Delete test data - Verify deleted in response", 3, jsonResp2.getBody().getObject().getInt("testsDeleted"));

        // last call to make sure nothing is left
        HttpResponse<JsonNode> jsonResp3 = Unirest.delete(getBaseUrl() + "/loadsvc/v1/maintenance/cleanTestData").asJson();
        assertEquals("Maint. - Delete test data - Verify no tests left", 0, jsonResp3.getBody().getObject().getInt("testsDeleted"));
    }



}