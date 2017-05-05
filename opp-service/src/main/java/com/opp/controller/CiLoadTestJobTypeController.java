package com.opp.controller;

import com.opp.domain.CiLoadTestJobType;
import com.opp.dto.ErrorResponse;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.CiLoadTestJobTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by ctobe on 4/26/17.
 */
@Api(value = "CI Load Test Job Type", description = "CI Load Test Job Type API")
@RestController
public class CiLoadTestJobTypeController {

    @Autowired
    private CiLoadTestJobTypeService service;

    @RequestMapping(value = "/loadsvc/v1/ci/loadtestjobtypes", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Creates a new CI Load Test Job Type" )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new CI load test job type", response = CiLoadTestJobType.class),
            @ApiResponse(code = 400, message = "Invalid Application object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public CiLoadTestJobType create(@Valid @RequestBody CiLoadTestJobType ciLoadTestJobType) {
        return service.add(ciLoadTestJobType).orElseThrow(()->new InternalServiceException("Error occurred while creating CI load test job type"));
    }

    @RequestMapping(value = "/loadsvc/v1/ci/loadtestjobtypes/{id}", method = RequestMethod.PUT)
    @ApiOperation( value = "Update a CI Load Test Job Type")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated CI load test job type", response = CiLoadTestJobType.class),
            @ApiResponse(code = 400, message = "Invalid CI load test job type object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "CI load test job type not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public CiLoadTestJobType update(@PathVariable("id") Integer ciLoadTestJobTypeId, @Valid @RequestBody CiLoadTestJobType update) {
        return service.update(ciLoadTestJobTypeId, update).orElseThrow(()->new InternalServiceException("Error occurred while updating CI load test job type"));
    }

    @RequestMapping(value = "/loadsvc/v1/ci/loadtestjobtypes/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Delete a CI Load Test Job Type")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted CI load test job type", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete CI load test job type", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "CI load test job type not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@PathVariable("id") Integer applicationId) {
        service.delete(applicationId);
    }

    @RequestMapping(value = "/loadsvc/v1/ci/loadtestjobtypes/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get a CI Load Test Job Type by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved CI load test job type", response = CiLoadTestJobType.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "CI load test job type not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public CiLoadTestJobType getById(@PathVariable("id") Integer applicationId) {
        return service.getById(applicationId).orElseThrow(()->new ResourceNotFoundException("ID does not exist."));
    }


    @RequestMapping(value = "/loadsvc/v1/ci/loadtestjobtypes", method = RequestMethod.GET)
    @ApiOperation(value = "Get CI Load Test Job Types")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved CI load test job type(s)", response = CiLoadTestJobType.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<CiLoadTestJobType> searchForJob(@RequestParam(value = "jobType", defaultValue = "") String jobType )
    {
        if(jobType.isEmpty()){
            return service.getAll();
        } else {
            Optional<CiLoadTestJobType> jobTypeOptional = service.getByJobType(jobType);
            if(jobTypeOptional.isPresent()){
                return Arrays.asList(jobTypeOptional.get());
            } else {
                return Arrays.asList();
            }
        }
    }
}