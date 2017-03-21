package com.opp.controller;

import com.opp.domain.LoadTest;
import com.opp.dto.ErrorResponse;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.LoadTestService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ctobe on 8/23/16.
 */
@Api(value = "LoadTest", description = "Load Test API", basePath = "/loadsvc")
@RestController
public class LoadTestController {

    @Autowired
    private LoadTestService service;

    @RequestMapping(value = "/loadsvc/v1/loadtests", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Creates a new load test")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new load test", response = LoadTest.class),
            @ApiResponse(code = 400, message = "Invalid load test object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadTest create(@Valid @RequestBody LoadTest loadTest) {
        return service.add(loadTest).orElseThrow(()->new InternalServiceException("Error occurred while creating LoadTest"));
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}", method = RequestMethod.PUT)
    @ApiOperation( value = "Update a LoadTest")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated LoadTest", response = LoadTest.class),
            @ApiResponse(code = 400, message = "Invalid LoadTest object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTest not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadTest update(@PathVariable("load_test_id") Integer loadTestId, @Valid @RequestBody LoadTest update) {
        return service.update(loadTestId, update).orElseThrow(()->new InternalServiceException("Error occurred while updating LoadTest"));
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation( value = "Delete a LoadTest", notes = "Pass in a comma delimited string of test ids to delete more than 1")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted LoadTest", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete LoadTest", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTest not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@PathVariable("load_test_id") String loadTestId, HttpServletResponse response) {
        if(service.delete(loadTestId) > 0){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        //todo: add better support and logging around deletes.  maybe change this to an object that shows the success and failure count
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}", method = RequestMethod.GET)
    @ApiOperation( value = "Get a LoadTest by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved LoadTest", response = LoadTest.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTest not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadTest getById(@PathVariable("load_test_id") Integer loadTestId) {
        return service.getById(loadTestId).orElseThrow(()->new ResourceNotFoundException("LoadTest does not exist with that ID."));
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests", method = RequestMethod.GET)
    @ApiOperation( value = "Get all LoadTests", notes = "You can send in a query param to filter by anything on the load test table.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved LoadTest", response = LoadTest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTest not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<LoadTest> getAll(
            @RequestParam(name = "appUnderTest", required = false) String appUnderTest,
            @RequestParam(name = "appUnderTestVersion", required = false) String appUnderTestVersion,
            @RequestParam(name = "comments", required = false) String comments,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "environment", required = false) String environment,
            @RequestParam(name = "externalTestId", required = false) Integer externalTestId,
            @ApiParam(value="in epoch time") @RequestParam(name = "startTime", required = false) Long startTime,
            @ApiParam(value="in epoch time") @RequestParam(name = "endTime", required = false) Long endTime,
            @RequestParam(name = "slaGroupId", required = false) String slaGroupId,
            @RequestParam(name = "testName", required = false) String testName,
            @RequestParam(name = "testSubName", required = false) String testSubName,
            @RequestParam(name = "testTool", required = false) String testTool,
            @RequestParam(name = "testToolVersion", required = false) String testToolVersion,
            @RequestParam(name = "vuserCount", required = false) Integer vuserCount,
            HttpServletRequest request) {
        // map all query parameters and filter out sampleDataOnly
        Map<String, String> queryParamMap = request.getParameterMap().entrySet().stream().filter(e->!e.getKey().equals("sampleDataOnly")).collect(Collectors.toMap(Map.Entry::getKey, e -> StringUtils.join(e.getValue(), ","), (v1, v2) -> null, HashMap::new));
        return service.getAll(queryParamMap);
    }



}