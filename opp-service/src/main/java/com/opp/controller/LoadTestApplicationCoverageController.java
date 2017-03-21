package com.opp.controller;

import com.opp.domain.LoadTestApplicationCoverage;
import com.opp.dto.ErrorResponse;
import com.opp.dto.LoadTestApplicationCoverageFilter;
import com.opp.exception.InternalServiceException;
import com.opp.service.LoadTestApplicationCoverageService;
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
import java.util.Optional;

/**
 * Created by ctobe on 6/13/16.
 */
@Api(value = "LoadTestApplicationCoverageDto", description = "Load Test Application Coverage API", basePath = "/loadsvc")
@RestController
public class LoadTestApplicationCoverageController {

    @Autowired
    private LoadTestApplicationCoverageService service;

    @RequestMapping(value = "/loadsvc/v1/loadtestapplicationcoverages", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Creates a new LoadTestApplicationCoverageDto",
            notes = "Creates a new LoadTestApplicationCoverageDto"
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new LoadTestApplicationCoverageDto", response = LoadTestApplicationCoverage.class),
            @ApiResponse(code = 400, message = "Invalid LoadTestApplicationCoverageDto object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public LoadTestApplicationCoverage create(@Valid @RequestBody LoadTestApplicationCoverage ltac) {
        if(service.add(ltac) == 1){
            return ltac; // if its created successfully, just return what they sent in since its the same
        } else {
            throw new InternalServiceException("Error occurred while creating LoadTestApplicationCoverageDto");
        }
    }

    @RequestMapping(value = "/loadsvc/v1/loadtestapplicationcoverages", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Delete a LoadTestApplicationCoverageDto",
            notes = "Deletes an existing LoadTestApplicationCoverageDto"
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted LoadTestApplicationCoverageDto(s)", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete LoadTestApplicationCoverageDto(s)", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTestApplicationCoverageDto not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@Valid @RequestBody LoadTestApplicationCoverageFilter filter,
                       HttpServletResponse response) {
        if(!service.deleteByFilter(filter)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    @RequestMapping(value = "/loadsvc/v1/loadtestapplicationcoverages", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all LoadTestApplicationCoverages",
            notes = "You may also filter by loadTestName and/or applicationId"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved LoadTestApplicationCoverageDto(s)", response = LoadTestApplicationCoverage.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "LoadTestApplicationCoverageDto not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<LoadTestApplicationCoverage> getAll(@RequestParam(value="loadTestName", required=false) Optional<String> loadTestName,
                                                    @RequestParam(value="applicationId", required=false) Optional<Integer> applicationId) {
        if(loadTestName.isPresent() || applicationId.isPresent()){
            return service.getByFilter(new LoadTestApplicationCoverageFilter(loadTestName, applicationId));
        } else {
            return service.getAll();
        }
    }

}