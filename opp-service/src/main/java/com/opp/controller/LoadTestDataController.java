package com.opp.controller;

import com.opp.domain.LoadTestData;
import com.opp.dto.ErrorResponse;
import com.opp.dto.LoadTestDataImportResp;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.LoadTestDataService;
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
 * Created by ctobe on 8/23/16.
 */
@Api(value = "LoadTestData", description = "Load Test Data API", basePath = "/loadsvc")
@RestController
public class LoadTestDataController {

    @Autowired
    private LoadTestDataService service;

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}/data", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Creates a new load test data",
            notes = "Must pass in array of data"
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully bulk imported load test data", response = LoadTestData.class),
            @ApiResponse(code = 400, message = "Invalid load test data object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadTestDataImportResp addAll(@PathVariable("load_test_id") int loadTestId, @RequestBody List<LoadTestData> loadTestDataList) {
        int rowsAdded = service.addAll(loadTestId, loadTestDataList);
        if(rowsAdded > 0){
            return new LoadTestDataImportResp(loadTestId, rowsAdded, loadTestDataList.size());
        } else {
            throw new InternalServiceException("Error occurred while trying import LoadTestData.  0 records imported.");
        }
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}/data/{data_id}", method = RequestMethod.PUT)
    @ApiOperation(
            value = "Update an existing LoadTestData",
            notes = "Cannot change the loadTestId"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated LoadTestData", response = LoadTestData.class),
            @ApiResponse(code = 400, message = "Invalid LoadTestData object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTestData not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadTestData update(@PathVariable("load_test_id") int loadTestId, @PathVariable("data_id") int dataId, @Valid @RequestBody LoadTestData update) {
        return service.update(loadTestId, dataId, update).orElseThrow(()->new InternalServiceException("Error occurred while updating LoadTestData"));
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}/data/{data_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Delete a LoadTestData",
            notes = "Deletes an existing LoadTestData"
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted LoadTestData", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete LoadTestData", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTestData not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@PathVariable("load_test_id") int loadTestId, @PathVariable("data_id") int dataId, HttpServletResponse response) {
        if(!service.delete(loadTestId, dataId)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}/data", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Delete all LoadTestData for a given Load Test"
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted all LoadTestData", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete LoadTestData", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTestData not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void deleteAll(@PathVariable("load_test_id") int loadTestId, HttpServletResponse response) {
        int deletedCount = service.deleteAll(loadTestId);
        if(deletedCount == 0){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}/data/{data_id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get a LoadTestData by ID"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved LoadTestData", response = LoadTestData.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTestData not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadTestData getById(@PathVariable("load_test_id") int loadTestId, @PathVariable("data_id") int dataId) {
        return service.getById(loadTestId, dataId).orElseThrow(()->new ResourceNotFoundException("LoadTestData does not exist with that ID."));
    }

    @RequestMapping(value = "/loadsvc/v1/loadtests/{load_test_id}/data", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all LoadTestData for a LoadTest"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved LoadTestData", response = LoadTestData.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTestData not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<LoadTestData> getAll(@PathVariable("load_test_id") int loadTestId) {
        return service.getAllByLoadTestId(loadTestId);
    }

}