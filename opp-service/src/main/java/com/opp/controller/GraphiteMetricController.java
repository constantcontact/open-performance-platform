package com.opp.controller;

import com.opp.domain.GraphiteMetric;
import com.opp.dto.ErrorResponse;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.GraphiteMetricService;
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
 * Created by ctobe on 9/13/16.
 */
@Api(value = "GraphiteMetric", description = "Graphite Metric API", basePath = "/loadsvc")
@RestController
public class GraphiteMetricController {

    @Autowired
    private GraphiteMetricService service;

    @RequestMapping(value = "/loadsvc/v1/graphitemetrics", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Creates a new Graphite Metric")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new Graphite Metric", response = GraphiteMetric.class),
            @ApiResponse(code = 400, message = "Invalid Graphite Metric object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public GraphiteMetric create(@Valid @RequestBody GraphiteMetric graphiteMetric) {
        return service.add(graphiteMetric).orElseThrow(()->new InternalServiceException("Error occurred while creating GraphiteMetric"));
    }

    @RequestMapping(value = "/loadsvc/v1/graphitemetrics/{graphite_metric_id}", method = RequestMethod.PUT)
    @ApiOperation( value = "Update a GraphiteMetric")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated GraphiteMetric", response = GraphiteMetric.class),
            @ApiResponse(code = 400, message = "Invalid GraphiteMetric object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "GraphiteMetric not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public GraphiteMetric update(@PathVariable("graphite_metric_id") Integer graphiteMetricId, @Valid @RequestBody GraphiteMetric update) {
        return service.update(graphiteMetricId, update).orElseThrow(()->new InternalServiceException("Error occurred while updating GraphiteMetric"));
    }

    @RequestMapping(value = "/loadsvc/v1/graphitemetrics/{graphite_metric_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation( value = "Delete a GraphiteMetric")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted GraphiteMetric", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete GraphiteMetric", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "GraphiteMetric not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@PathVariable("graphite_metric_id") Integer graphiteMetricId, HttpServletResponse response) {
        if(!service.delete(graphiteMetricId)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/loadsvc/v1/graphitemetrics/{graphite_metric_id}", method = RequestMethod.GET)
    @ApiOperation( value = "Get a GraphiteMetric by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved GraphiteMetric", response = GraphiteMetric.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "GraphiteMetric not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public GraphiteMetric getById(@PathVariable("graphite_metric_id") Integer graphiteMetricId) {
        return service.getById(graphiteMetricId).orElseThrow(()->new ResourceNotFoundException("GraphiteMetric does not exist with that ID."));
    }

    @RequestMapping(value = "/loadsvc/v1/graphitemetrics", method = RequestMethod.GET)
    @ApiOperation( value = "Get all GraphiteMetrics")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved GraphiteMetric", response = GraphiteMetric.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "GraphiteMetric not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<GraphiteMetric> getAll() {
        return service.getAll();
    }
    

}