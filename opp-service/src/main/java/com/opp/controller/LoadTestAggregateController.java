package com.opp.controller;

import com.opp.domain.LoadTestAggregate;
import com.opp.dto.ErrorResponse;
import com.opp.dto.LoadTestAggregateDataWithSlas;
import com.opp.dto.aggregate.LoadTestAggregateDataResp;
import com.opp.dto.aggregate.LoadTestAggregateImportResp;
import com.opp.service.LoadTestAggregateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by ctobe on 6/13/16.
 */
@Api(value = "LoadTestAggregate", description = "Load Test Aggregate API", basePath = "/loadsvc")
@RestController
public class LoadTestAggregateController {

    @Autowired
    private LoadTestAggregateService service;

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}/aggData", method = RequestMethod.GET)
    @ApiOperation( value = "Gets aggregated data from a load test" )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved aggregate data", response = LoadTestAggregate.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Aggregate data not found for load test", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<LoadTestAggregateDataWithSlas> getAggregateData(@PathVariable("load_test_id") int loadTestId) {
        return service.getByLoadTestIdWithSlas(loadTestId);
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}/aggData", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Import already aggregated data", notes = "This will import your own aggregated data into the system." )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully imported aggregated data", response = LoadTestAggregateImportResp.class),
            @ApiResponse(code = 400, message = "Invalid object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Load test not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadTestAggregateImportResp importAggregates(@PathVariable("load_test_id") int loadTestId, @Valid @RequestBody List<LoadTestAggregate> loadTestAggregateList) {
        return service.addAll(loadTestId, loadTestAggregateList);
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}/aggregate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Aggregates all raw data for a given test ID.", notes = "Aggregates all raw data for a given test ID.  This is also delete existing aggregate data." )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully aggregated load test", response = LoadTestAggregateDataResp.class),
            @ApiResponse(code = 400, message = "Invalid object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Load test not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadTestAggregateDataResp aggregateLoadTest(@PathVariable("load_test_id") int loadTestId) {
        return service.aggregateLoadTestData(loadTestId);
    }

}