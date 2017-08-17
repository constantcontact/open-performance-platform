package com.opp.service;

import com.opp.domain.*;
import com.opp.dto.LoadTestApplicationCoverageFilter;
import com.opp.dto.SoastaCloudTestResponse;
import com.opp.dto.aggregate.LoadTestAggregateDataResp;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.util.JavaRunCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class SoastaCloudTestService {
    private static final Logger logger = LoggerFactory.getLogger(SoastaCloudTestService.class);

    @Value("${opp.cloudtest.url}")
    private String cloudTestUrl;
    @Value("${opp.cloudtest.user}")
    private String cloudTestUser;
    @Value("${opp.cloudtest.password}")
    private String cloudTestPassword;
    @Value("${opp.cloudtest.apiToken}")
    private String cloudTestApiToken;
    @Value("${opp.cloudtest.sCommandPath}")
    private String sCommandPath;
    @Value("${opp.cloudtest.dataPath}")
    private String cloudTestDataDirectory;

    // used to filter only transactions
    private static final String TRANSACTION_TYPE = "22";

    @Value("${opp.appMap.validateTestNameMapping}")
    private boolean validateTestNameMapping;
    @Value("${opp.db.importBatchSize}")
    private int importBatchSize;

    @Autowired
    private ApplicationMapService applicationMapService;
    @Autowired
    private LoadTestDataService loadTestDataService;
    @Autowired
    private LoadTestApplicationCoverageService loadTestApplicationCoverageService;
    @Autowired
    private LoadTestService loadTestService;
    @Autowired
    private LoadSlaTestGroupService loadSlaTestGroupService;
    @Autowired
    private LoadTestAggregateService loadTestAggregateService;


    public SoastaCloudTestResponse start(SoastaCloudTest soastaCloudTest) {
        long startTime = System.currentTimeMillis();

        SoastaCloudTestResponse soastaCloudTestResponse = new SoastaCloudTestResponse();
        SoastaCloudTestResponse.DataImport dataImport = new SoastaCloudTestResponse.DataImport();

        List<String> appsUnderTest = Arrays.asList(soastaCloudTest.getAppUnderTest().split(","));

        // loop through all apps under test.
        // and add it to the loadtest coverage
        appsUnderTest.forEach(app -> {
            // verify the app is valid.
            Optional<ApplicationMap> appMap = applicationMapService.getAllByAppKey(app.trim()).stream().findFirst();

            appMap.ifPresent(application -> {

                // check that the test name already exists in the application coverage
                LoadTestApplicationCoverageFilter loadTestApplicationCoverageFilter = new LoadTestApplicationCoverageFilter();
                loadTestApplicationCoverageFilter.setApplicationId(Optional.of(application.getId()));
                loadTestApplicationCoverageFilter.setLoadTestName(Optional.of(soastaCloudTest.getTestName()));

                Optional<LoadTestApplicationCoverage> coverage =
                        loadTestApplicationCoverageService.getByFilter(loadTestApplicationCoverageFilter)
                                .stream()
                                .findFirst();

                if (!coverage.isPresent()) {
                    LoadTestApplicationCoverage loadTestApplicationCoverage = new LoadTestApplicationCoverage();
                    loadTestApplicationCoverage.setApplicationId(application.getId());
                    loadTestApplicationCoverage.setLoadTestName(soastaCloudTest.getTestName());

                    loadTestApplicationCoverageService.add(loadTestApplicationCoverage);
                }
            });
        });

        LoadTestApplicationCoverageFilter loadTestApplicationCoverageFilter = new LoadTestApplicationCoverageFilter();
        loadTestApplicationCoverageFilter.setLoadTestName(Optional.of(soastaCloudTest.getTestName()));

        List<LoadTestApplicationCoverage> loadTestApplicationCoverage =
                loadTestApplicationCoverageService.getByFilter(loadTestApplicationCoverageFilter);

        // insert a load test
        if (loadTestApplicationCoverage.size() > 0) {
            Optional<LoadTest> loadTest = loadTestService.add(soastaCloudTest);

            int loadTestId = loadTest.isPresent() ? loadTest.get().getId() : 0;
            if (loadTestId > 0) {

                if (soastaCloudTest.getSlaGroupId() != null && soastaCloudTest.getSlaGroupId() != 0) {
                    // add the load test to the sla group
                    LoadSlaTestGroup loadSlaTestGroup = new LoadSlaTestGroup();
                    loadSlaTestGroup.setIsActive(true);
                    loadSlaTestGroup.setLoadSlaGroupId(soastaCloudTest.getSlaGroupId());
                    loadSlaTestGroup.setLoadTestId(loadTestId);

                    try {
                        loadSlaTestGroupService.insert(loadSlaTestGroup);
                    } catch (Exception e) {
                        logger.error("Error inserting Load SLA Test Group.", e);
                    }
                }

                // get the results from soasta
                String filename = exportResults(soastaCloudTest.getCtComposition(),
                        soastaCloudTest.getTestDataType(), soastaCloudTest.getDataFile());
                soastaCloudTestResponse.setSoastaDataExport(filename);

                if (!StringUtils.isEmpty(filename)) {
                    // import the data into the database.
                    AtomicInteger insertCount = new AtomicInteger(0);
                    long startImportTime = System.currentTimeMillis();
                    if ("resultCollectionDetails".equalsIgnoreCase(soastaCloudTest.getTestDataType())) {
                        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
                            AtomicInteger counter = new AtomicInteger(0);

                            final List<LoadTestData> loadTestList = new ArrayList<>();
                            lines.forEach(line -> {
                                counter.incrementAndGet();
                                List<String> results = Arrays.asList(line.split(","));
                                if (results.get(LoadTestDataFactory.CollectionDetailsColumn.TYPE.columnIndex).equals(TRANSACTION_TYPE)) {
                                    loadTestList.add(LoadTestDataFactory.create(soastaCloudTest.getTestDataType(), loadTestId, results));
                                }

                                if (loadTestList.size() == importBatchSize) {
                                    insertCount.addAndGet(loadTestDataService.addAll(loadTestId, loadTestList));
                                    loadTestList.clear();
                                }
                            });
                            // check if there's any left in the batch.
                            if (loadTestList.size() > 0) {
                                insertCount.addAndGet(loadTestDataService.addAll(loadTestId, loadTestList));
                            }

                            dataImport.setInserted(insertCount.get());
                            dataImport.setTime(System.currentTimeMillis() - startImportTime);
                            dataImport.setLineCount(counter.decrementAndGet());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    soastaCloudTestResponse.setLoadTest(loadTest.get());
                    if (insertCount.get() > 0) {
                        LoadTestAggregateDataResp loadTestAggregateDataResp =
                                loadTestAggregateService.aggregateLoadTestData(loadTestId);
                        soastaCloudTestResponse.setSuccess(true);
                        soastaCloudTestResponse.setCompletionTime(System.currentTimeMillis() - startTime);
                        soastaCloudTestResponse.setDataAggregation(loadTestAggregateDataResp);
                        soastaCloudTestResponse.setDataImport(dataImport);

                    } else {
                        soastaCloudTestResponse.setSuccess(false);
                        soastaCloudTestResponse.setCompletionTime(System.currentTimeMillis() - startTime);
                        throw new ResourceNotFoundException(soastaCloudTestResponse.toString());
                    }
                }
            } else {
                throw new InternalServiceException("Failed to create load test in OPP.  Please check the logs for more information.");
            }
        }

        return soastaCloudTestResponse;
    }

    public String exportResults(String ctComposition, String testDataType, String dataFile) {
        if (!StringUtils.isEmpty(dataFile)) {
            File file = new File(cloudTestDataDirectory + dataFile);
            return file.exists() ? cloudTestDataDirectory + dataFile : null;
        }

        // set user auth
        String userAuth = String.format("username=%s password=%s", cloudTestUser, cloudTestPassword);
        if(!cloudTestApiToken.isEmpty()){
            userAuth = "apitoken=" + cloudTestApiToken;
        }

        // verify that the test exists in soasta.
        String testNameCommand =
                String.format("%s cmd=list url=%s %s type=result | grep \"%s\" | tail -1 2>&1",
                        sCommandPath, cloudTestUrl, userAuth, ctComposition);

        Optional<String> testName = JavaRunCommand.run(testNameCommand);

        String absoluteFilename;
        if (testName.isPresent()) {
            String safeTestName = testName.get().trim().substring(1).replaceAll("[^a-zA-Z0-9.-]", "_");
            absoluteFilename = cloudTestDataDirectory + safeTestName + "-" + testDataType + ".csv";

            // build the scommand to export the data to a file.
            String getExportCommand = String.format(
                    "%s cmd=export name=\"%s\" file=\"%s\" url=%s %s type=result format=%s resultSource=%s",
                    sCommandPath, testName.get().trim(), absoluteFilename, cloudTestUrl, userAuth, "CSV", testDataType);

            Optional<String> exportResults = JavaRunCommand.run(getExportCommand);
            if (exportResults.isPresent()) {
                if (!exportResults.get().contains("was exported successfully")) {
                    throw new InternalServiceException("Error exporting " + absoluteFilename + ". " + exportResults.get());
                }

                logger.info("Successfully exported " + absoluteFilename);
            }
        } else {
            throw new InternalServiceException("Soasta Test " + ctComposition + " does not exist");
        }


        return absoluteFilename;
    }

    public boolean isValidateTestNameMapping() {
        return validateTestNameMapping;
    }

    public void setValidateTestNameMapping(boolean validateTestNameMapping) {
        this.validateTestNameMapping = validateTestNameMapping;
    }
}
