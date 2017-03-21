package com.opp.controller;

import com.opp.domain.LoadSlaGroup;
import com.opp.dto.ErrorResponse;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.LoadSlaGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by ctobe on 8/5/16.
 */
@Api(value = "sla group", description = "SLA Group API", basePath = "/loadsvc")
@RestController
public class LoadSlaGroupController {

    @Autowired
    private LoadSlaGroupService service;

    @RequestMapping(value = "/loadsvc/v1/slagroups", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Creates a new SLA Group",
            notes = "Creates a new SLA Group"
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new SLA Group", response = LoadSlaGroup.class),
            @ApiResponse(code = 400, message = "Invalid SLA Group object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadSlaGroup create(@Valid @RequestBody LoadSlaGroup sla) {
        return service.add(sla).orElseThrow(()->new InternalServiceException("Error occurred while creating SLA Group"));
    }

    @RequestMapping(value = "/loadsvc/v1/slagroups/{sla_group_id}", method = RequestMethod.PUT)
    @ApiOperation(
            value = "Update an SLA Group",
            notes = "Updates an existing SLA Group"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated SLA Group", response = LoadSlaGroup.class),
            @ApiResponse(code = 400, message = "Invalid SLA Group object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "SLA Group not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadSlaGroup update(@PathVariable("sla_group_id") Integer slaGroupId, @Valid @RequestBody LoadSlaGroup update) {
        return service.update(slaGroupId, update).orElseThrow(()->new InternalServiceException("Error occurred while updating SLA Group"));
    }

    @RequestMapping(value = "/loadsvc/v1/slagroups/{sla_group_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Delete a SLA Group",
            notes = "Deletes an existing SLA Group"
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted SLA Group", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete SLA Group", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "SLA Group not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@PathVariable("sla_group_id") Integer slaGroupId, HttpServletResponse response) {
        if(!service.delete(slaGroupId)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/loadsvc/v1/slagroups/{sla_group_id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get a SLA Group",
            notes = "Gets a SLA Group by ID"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved SLA Group", response = LoadSlaGroup.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "SLA Group not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadSlaGroup getById(@PathVariable("sla_group_id") Integer slaGroupId) {
        return service.getById(slaGroupId).orElseThrow(()->new ResourceNotFoundException("SLA Group does not exist with that ID."));
    }

    @RequestMapping(value = "/loadsvc/v1/slagroups", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all SLA Groups",
            notes = "Get all SLA Groups"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved SLA Groups", response = LoadSlaGroup.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "SLA Group not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<LoadSlaGroup> getAll() {
        return service.getAll();
    }



}