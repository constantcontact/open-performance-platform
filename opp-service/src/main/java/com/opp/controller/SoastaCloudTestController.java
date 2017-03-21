package com.opp.controller;

import com.opp.domain.SoastaCloudTest;
import com.opp.dto.ErrorResponse;
import com.opp.dto.SoastaCloudTestResponse;
import com.opp.service.SoastaCloudTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Integrations", description = "Integration with Cloud Test", basePath = "/loadsvc")
@RestController
public class SoastaCloudTestController {

    private static final Logger logger = LoggerFactory.getLogger(SoastaCloudTestController.class);

    @Autowired
    private SoastaCloudTestService soastaCloudTestService;

    @RequestMapping(value = "/loadsvc/v1/soastacloudtest/start", method = RequestMethod.GET)
    @ApiOperation(
            value = "Export Load Test Results and Store into OPP",
            notes = "Export Load Test Results and Store into OPP"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved soasta cloud test data.", response = SoastaCloudTestResponse.class),
            @ApiResponse(code = 400, message = "Invalid request. Please check data parameters.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = ErrorResponse.class)
    })
    public SoastaCloudTestResponse start(@RequestParam(value="ctComposition") String ctComposition,
                                         @RequestParam(value="testName") String testName,
                                         @RequestParam(value="testSubName", required=false) String testSubName,
                                         @RequestParam(value="appUnderTest") String appUnderTest,
                                         @RequestParam(value="appUnderTestVersion", required=false) String appUnderTestVersion,
                                         @RequestParam(value="description", required=false) String description,
                                         @RequestParam(value="comments", required=false) String comments,
                                         @RequestParam(value="vuserCount") int vuserCount,
                                         @RequestParam(value="environment") String environment,
                                         @RequestParam(value="testTool", defaultValue="soasta") String testTool,
                                         @RequestParam(value="startTime") Double startTime,
                                         @RequestParam(value="testDataType", defaultValue="resultCollectionDetails") String testDataType,
                                         @RequestParam(value="slaGroupId", required=false) String slaGroupId,
                                         @RequestParam(value="externalTestId") String externalTestId,
                                         @RequestParam(value="dataFile", required=false) String dataFile) {


        SoastaCloudTest soastaCloudTest = new SoastaCloudTest();
        soastaCloudTest.setCtComposition(ctComposition);
        soastaCloudTest.setTestName(testName);
        soastaCloudTest.setTestSubName(testSubName);
        soastaCloudTest.setAppUnderTest(appUnderTest);
        soastaCloudTest.setAppUnderTestVersion(appUnderTestVersion);
        soastaCloudTest.setDescription(description);
        soastaCloudTest.setComments(comments);
        soastaCloudTest.setVuserCount(vuserCount);
        soastaCloudTest.setEnvironment(environment);
        soastaCloudTest.setTestTool(testTool);
        soastaCloudTest.setStartTime(startTime.longValue());
        soastaCloudTest.setTestDataType(testDataType);
        try {
            soastaCloudTest.setSlaGroupId(Integer.valueOf(slaGroupId));
        } catch(NumberFormatException e ) {
            // do nothing because it's not required.
        }

        soastaCloudTest.setExternalTestId(externalTestId);
        soastaCloudTest.setDataFile(dataFile);

        logger.info(soastaCloudTest.toString());
        return soastaCloudTestService.start(soastaCloudTest);
    }
}
