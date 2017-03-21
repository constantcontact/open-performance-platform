package com.opp.controller;

import com.opp.dto.ErrorResponse;
import com.opp.dto.datagen.DataGenRequest;
import com.opp.dto.datagen.DataGenResponse;
import com.opp.service.DataGenService;
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
@Api(value = "datagen", description = "Data Gen API", basePath = "/loadsvc")
@RestController
public class DataGenController {

    @Autowired
    private DataGenService service;

    @RequestMapping(value = "/loadsvc/v1/datagen", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Generates data",
            notes = "Creates data for testing the test tool :-)"
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created data", response = DataGenResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Invalid request object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<DataGenResponse> generateData(@Valid @RequestBody DataGenRequest dataGenRequest) {
        return service.generateData(dataGenRequest);
    }

}