package com.opp.controller;

import com.opp.domain.CiLoadTestType;
import com.opp.dto.ErrorResponse;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.CiLoadTestTypeService;
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
@Api(value = "CI Load Test Type", description = "CI Load Test Type API")
@RestController
public class CiLoadTestTypeController {

    @Autowired
    private CiLoadTestTypeService service;

    @RequestMapping(value = "/loadsvc/v1/ci/loadtesttypes", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Creates a new CI Load Test Job Type" )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new CI load test type", response = CiLoadTestType.class),
            @ApiResponse(code = 400, message = "Invalid Application object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public CiLoadTestType create(@Valid @RequestBody CiLoadTestType ciLoadTestType) {
        return service.add(ciLoadTestType).orElseThrow(()->new InternalServiceException("Error occurred while creating CI load test type"));
    }

    @RequestMapping(value = "/loadsvc/v1/ci/loadtesttypes/{id}", method = RequestMethod.PUT)
    @ApiOperation( value = "Update a CI Load Test Job Type")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated CI load test type", response = CiLoadTestType.class),
            @ApiResponse(code = 400, message = "Invalid CI load test type object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "CI load test type not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public CiLoadTestType update(@PathVariable("id") Integer ciLoadTestTypeId, @Valid @RequestBody CiLoadTestType update) {
        return service.update(ciLoadTestTypeId, update).orElseThrow(()->new InternalServiceException("Error occurred while updating CI load test type"));
    }

    @RequestMapping(value = "/loadsvc/v1/ci/loadtesttypes/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Delete a CI Load Test Job Type")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted CI load test type", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete CI load test type", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "CI load test type not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@PathVariable("id") Integer applicationId) {
        service.delete(applicationId);
    }

    @RequestMapping(value = "/loadsvc/v1/ci/loadtesttypes/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get a CI Load Test Job Type by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved CI load test type", response = CiLoadTestType.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "CI load test type not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public CiLoadTestType getById(@PathVariable("id") Integer applicationId) {
        return service.getById(applicationId).orElseThrow(()->new ResourceNotFoundException("ID does not exist."));
    }


    @RequestMapping(value = "/loadsvc/v1/ci/loadtesttypes", method = RequestMethod.GET)
    @ApiOperation(value = "Get CI Load Test Job Types")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved CI load test type(s)", response = CiLoadTestType.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<CiLoadTestType> searchForTestType(@RequestParam(value = "testType", defaultValue = "", required = false) String testType )
    {
        if(testType.isEmpty()){
            return service.getAll();
        } else {
            Optional<CiLoadTestType> testTypeOptional = service.getByTestType(testType);
            if(testTypeOptional.isPresent()){
                return Arrays.asList(testTypeOptional.get());
            } else {
                return Arrays.asList();
            }
        }
    }
}