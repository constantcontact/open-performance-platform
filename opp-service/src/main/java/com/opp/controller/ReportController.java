package com.opp.controller;

import com.opp.dto.ErrorResponse;
import com.opp.dto.LoadTestRunHistory;
import com.opp.dto.reports.LoadTestSummaryReport;
import com.opp.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ctobe on 8/13/16.
 */
@Api(value = "Reports", description = "Reports API", basePath = "/loadsvc")
@RestController
public class ReportController {

    @Autowired
    private ReportService service;

    @RequestMapping(value = "/loadsvc/v1/reports/testrunhistory", method = RequestMethod.GET)
    @ApiOperation( value = "Gets the run history of all tests" )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved list of tests run without the specified time period", response = LoadTestRunHistory.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTestRunHistory not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<LoadTestRunHistory> getLoadTestRunHistory(@RequestParam(value="daysBack", required=false, defaultValue = "30") int daysBack) {
        return service.getLoadTestRunsByDaysBack(daysBack);
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{id}/trendreport", method = RequestMethod.GET)
    @ApiOperation( value = "Gets the load test trend report object" )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved load test trend report", response = LoadTestRunHistory.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Load test id not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadTestSummaryReport getLoadTestTrendReport(@PathVariable("id") Integer loadTestId) {
        return service.getLoadTestTrendReport(loadTestId);
    }


}