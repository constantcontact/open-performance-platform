package com.opp.controller;

import com.opp.domain.ApplicationMap;
import com.opp.dto.ErrorResponse;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.ApplicationMapService;
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
@Api(value = "application", description = "Application API", basePath = "/loadsvc")
@RestController
public class ApplicationMapController {

    @Autowired
    private ApplicationMapService service;

    @RequestMapping(value = "/loadsvc/v1/applications", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Creates a new application",
            notes = "Creates a new application"
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new application", response = ApplicationMap.class),
            @ApiResponse(code = 400, message = "Invalid Application object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public ApplicationMap create(@Valid @RequestBody ApplicationMap applicationMap) {
        return service.add(applicationMap).orElseThrow(()->new InternalServiceException("Error occurred while creating application map"));
    }

    @RequestMapping(value = "/loadsvc/v1/applications/{application_id}", method = RequestMethod.PUT)
    @ApiOperation(
            value = "Update an application",
            notes = "Updates an existing application"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated application map", response = ApplicationMap.class),
            @ApiResponse(code = 400, message = "Invalid application map object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "application map not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public ApplicationMap update(@PathVariable("application_id") Integer applicationMapId, @Valid @RequestBody ApplicationMap update) {
        return service.update(applicationMapId, update).orElseThrow(()->new InternalServiceException("Error occurred while updating application map"));
    }

    @RequestMapping(value = "/loadsvc/v1/applications/{application_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Delete a application map",
            notes = "Deletes an existing application map"
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted application map", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete application map", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "application map not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@PathVariable("application_id") Integer applicationId) {
        service.delete(applicationId);
    }

    @RequestMapping(value = "/loadsvc/v1/applications/{application_id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get a application map",
            notes = "Gets a application map by ID"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved application map", response = ApplicationMap.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "application map not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public ApplicationMap getById(@PathVariable("application_id") Integer applicationId) {
        return service.getById(applicationId).orElseThrow(()->new ResourceNotFoundException("Application ID does not exist."));
    }

    @RequestMapping(value = "/loadsvc/v1/applications", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all applications",
            notes = "Gets all applications"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved application map", response = ApplicationMap.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "application map not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<ApplicationMap> getAll() {
        return service.getAll();
    }
}