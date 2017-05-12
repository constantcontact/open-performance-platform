package com.opp.controller;

import com.opp.domain.CiLoadTestJob;
import com.opp.domain.CiLoadTestJobGetWithType;
import com.opp.dto.ErrorResponse;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.CiLoadTestJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by ctobe on 4/26/17.
 */
@Api(value = "CI Load Test Job", description = "CI Load Test Job API")
@RestController
public class CiLoadTestJobController {

    @Autowired
    private CiLoadTestJobService service;

    @RequestMapping(value = "/loadsvc/v1/ci/loadtestjobs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Creates a new CI Load Test Job" )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new CI load test job", response = CiLoadTestJob.class),
            @ApiResponse(code = 400, message = "Invalid Application object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public CiLoadTestJob create(@Valid @RequestBody CiLoadTestJob ciLoadTestJob) {
        return service.add(ciLoadTestJob).orElseThrow(()->new InternalServiceException("Error occurred while creating CI load test job"));
    }

    @RequestMapping(value = "/loadsvc/v1/ci/loadtestjobs/{id}", method = RequestMethod.PUT)
    @ApiOperation( value = "Update a CI Load Test Job")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated CI load test job", response = CiLoadTestJob.class),
            @ApiResponse(code = 400, message = "Invalid CI load test job object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "CI load test job not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public CiLoadTestJob update(@PathVariable("id") Integer ciLoadTestJobId, @Valid @RequestBody CiLoadTestJob update) {
        return service.update(ciLoadTestJobId, update).orElseThrow(()->new InternalServiceException("Error occurred while updating CI load test job"));
    }

    @RequestMapping(value = "/loadsvc/v1/ci/loadtestjobs/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Delete a CI Load Test Job")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted CI load test job", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete CI load test job", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "CI load test job not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@PathVariable("id") Integer applicationId) {
        service.delete(applicationId);
    }

    @RequestMapping(value = "/loadsvc/v1/ci/loadtestjobs/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get a CI Load Test Job by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved CI load test job", response = CiLoadTestJob.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "CI load test job not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public CiLoadTestJob getById(@PathVariable("id") Integer applicationId) {
        return service.getById(applicationId).orElseThrow(()->new ResourceNotFoundException("ID does not exist."));
    }


    @RequestMapping(value = "/loadsvc/v1/ci/loadtestjobs", method = RequestMethod.GET)
    @ApiOperation(value = "Get CI Load Test Jobs")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved CI load test job(s)", response = CiLoadTestJob.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<CiLoadTestJobGetWithType> searchForTest(
            @RequestParam(value = "testName", defaultValue = "") String testName,
            @RequestParam(value = "testType", defaultValue = "") String testType )
    {
        CiLoadTestJob job = new CiLoadTestJob();
        job.setTestName(testName);
        job.setTestType(testType);
        return service.search(job);
    }
}