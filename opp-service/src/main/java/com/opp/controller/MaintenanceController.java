package com.opp.controller;

import com.opp.dto.ErrorResponse;
import com.opp.dto.maintenance.*;
import com.opp.service.MaintenanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by ctobe on 6/13/16.
 */
@Api(value = "maintenance", description = "Maintenance API", basePath = "/loadsvc")
@RestController
public class MaintenanceController {

    @Autowired
    private MaintenanceService service;

    @RequestMapping(value = "/loadsvc/v1/maintenance/transaction", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Deletes all transaction data inside all load tests matching the load test name and transaction name",
            notes = "Deletes an existing application map"
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted transactions", response = MaintDeleteTransactionResp.class),
            @ApiResponse(code = 400, message = "Failed to delete transactions", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "No transactions found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public MaintDeleteTransactionResp deleteTransaction(@Valid @RequestBody MaintDeleteTransactionReq mTrans) {
        return service.deleteTransactions(mTrans);
    }

    @RequestMapping(value = "/loadsvc/v1/maintenance/cleanTestData", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Deletes all test data",
            notes = ""
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted test data", response = MaintDeleteTransactionResp.class),
            @ApiResponse(code = 400, message = "Failed to delete transactions", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "No transactions found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public MaintDeleteTestDataResp deleteTestData() {
        return service.deleteTestData();
    }

    @RequestMapping(value = "/loadsvc/v1/maintenance/reaggregatedata", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Re-aggregates all data",
            notes = "Re-aggregates all tests that still have raw data"
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully re-aggregated all data", response = MaintReaggregateDataResp.class),
            @ApiResponse(code = 400, message = "Invalid request object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public MaintReaggregateDataResp reAggregateData() {

        return service.reAggregateData();
    }

    @RequestMapping(value = "/loadsvc/v1/maintenance/cleanlogs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Cleans API logs older than X",
            notes = "Cleans API logs older than X"
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted api logs", response = MaintDeleteApiLogsResp.class),
            @ApiResponse(code = 400, message = "Invalid request object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public MaintDeleteApiLogsResp deleteApiLogs(@RequestParam(value="daysToKeep", defaultValue="30") int daysToKeep) {
        return service.deleteApiLogs(daysToKeep);
    }


}