package com.opp.controller;

import com.opp.domain.LoadSla;
import com.opp.dto.ErrorResponse;
import com.opp.dto.LoadTestSla;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.LoadSlaService;
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
@Api(value = "sla", description = "Load Test SLA API", basePath = "/loadsvc")
@RestController
public class LoadSlaController {

    @Autowired
    private LoadSlaService service;

    @RequestMapping(value = "/loadsvc/v1/slas", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Creates a new sla" )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new sla", response = LoadSla.class),
            @ApiResponse(code = 400, message = "Invalid sla object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadSla create(@Valid @RequestBody LoadSla sla) {
        return service.add(sla).orElseThrow(()->new InternalServiceException("Error occurred while creating SLA"));
    }

    @RequestMapping(value = "/loadsvc/v1/slas_bulk_import/{load_test_id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Creates a new sla" )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new sla", response = LoadSla.class),
            @ApiResponse(code = 400, message = "Invalid sla object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadSlaService.LoadSlaBulkImportResult bulkImport(@PathVariable("load_test_id") Integer loadTestId, @Valid @RequestBody List<LoadSla> slas) {
        return service.bulkImport(loadTestId, slas);
    }

    @RequestMapping(value = "/loadsvc/v1/slas/{sla_id}", method = RequestMethod.PUT)
    @ApiOperation(
            value = "Update an SLA",
            notes = "Updates an existing SLA"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated SLA", response = LoadSla.class),
            @ApiResponse(code = 400, message = "Invalid SLA object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "SLA not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadSla update(@PathVariable("sla_id") Integer slaId, @Valid @RequestBody LoadSla update) {
        return service.update(slaId, update).orElseThrow(()->new InternalServiceException("Error occurred while updating SLA"));
    }

    @RequestMapping(value = "/loadsvc/v1/slas_bulk_update/{load_test_id}", method = RequestMethod.PUT)
    @ApiOperation(
            value = "Bulk update SLAs"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated SLA", response = LoadSla.class),
            @ApiResponse(code = 400, message = "Invalid SLA object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "SLA not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadSlaService.LoadSlaBulkImportResult bulkUpdate(@Valid @RequestBody List<LoadSla> update) {
        return service.bulkUpdate(update);
    }

    @RequestMapping(value = "/loadsvc/v1/slas/{sla_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Delete a SLA",
            notes = "Deletes an existing SLA"
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted SLA", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete SLA", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "SLA not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@PathVariable("sla_id") Integer slaId, HttpServletResponse response) {
        if(!service.delete(slaId)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/loadsvc/v1/slas/{sla_id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get a SLA",
            notes = "Gets a SLA by ID"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved SLA", response = LoadSla.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "SLA not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadSla getById(@PathVariable("sla_id") Integer slaId) {
        return service.getById(slaId).orElseThrow(()->new ResourceNotFoundException("SLA does not exist with that ID."));
    }

    @RequestMapping(value = "/loadsvc/v1/slas", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all SLAs",
            notes = "Gets all SLAs"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved SLAs", response = LoadSla.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "SLAs not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<LoadSla> getAll() {
        return service.getAll();
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{loadTestId}/slas", method = RequestMethod.GET)
    @ApiOperation( value = "Get all SLAs from LoadTest")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved SLAs", response = LoadSla.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "SLAs not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<LoadTestSla> getAllFromLoadTest(@PathVariable("loadTestId") Integer loadTestId) {
        return service.getAllByLoadTestId(loadTestId);
    }

}