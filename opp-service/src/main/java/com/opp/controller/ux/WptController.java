package com.opp.controller.ux;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.opp.domain.ApplicationMap;
import com.opp.domain.ux.*;
import com.opp.dto.ErrorResponse;
import com.opp.dto.graphite.GraphiteSimpleMetric;
import com.opp.dto.graphite.GraphiteSimpleResp;
import com.opp.dto.ux.WptTrendDataResp;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.GraphiteService;
import com.opp.service.WptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;

/**
 * Created by ctobe on 10/18/16.
 */
@Api(value = "WebPageTest", description = "WPT API", basePath = "/uxsvc")
@RestController
public class WptController {

    @Autowired
    private WptService service;


    @Autowired
    private GraphiteService graphiteService;


    // ======== CRUD ===============
    @RequestMapping(value = "/uxsvc/v1/wpt/tests", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Creates a new WPT Test")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created new WPT Test", response = WptTest.class),
            @ApiResponse(code = 400, message = "Invalid WPT Test object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public IndexResponse create(@RequestBody WptTestImport wptTestImport) throws Exception {
        return service.importTest(wptTestImport); //.orElseThrow(()->new InternalServiceException("Error occurred while creating WPT Test"));
    }

    @RequestMapping(value = "/uxsvc/v1/wpt/tests/{wpt_test_id}", method = RequestMethod.PUT)
    @ApiOperation(
            value = "Update an WPT Test",
            notes = "Updates an existing WPT Test"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated WPT Test", response = WptTest.class),
            @ApiResponse(code = 400, message = "Invalid WPT Test object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "WPT Test not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public UpdateResponse update(@PathVariable("wpt_test_id") String wptTestId, @Valid @RequestBody WptResult update) throws ExecutionException, InterruptedException {
        return service.update(wptTestId, update); //.orElseThrow(()->new InternalServiceException("Error occurred while updating WPT Test"));
    }

    @RequestMapping(value = "/uxsvc/v1/wpt/tests/{wpt_test_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Delete a WPT Test",
            notes = "Deletes an existing WPT Test"
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted WPT Test", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete WPT Test", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "WPT Test not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public void delete(@PathVariable("wpt_test_id") String wptTestId, HttpServletResponse response) {
        if(!service.delete(wptTestId)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/uxsvc/v1/wpt/tests/{wpt_test_id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get a WPT Test",
            notes = "Gets a WPT Test by ID"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved WPT Test", response = WptTest.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "WPT Test not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public WptResult getById(@PathVariable("wpt_test_id") String wptTestId) {
        return service.getById(wptTestId).orElseThrow(()->new ResourceNotFoundException("WPT Test does not exist with that ID."));
    }

    @RequestMapping(value = "/uxsvc/v1/wpt/tests", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all WPT Tests",
            notes = "Get all WPT Tests"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved WPT Tests", response = WptTest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "WPT Test not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<WptTest> getAll() {
        return service.getAll();
    }


    // ============= END CRUD ===========




    @RequestMapping(value = "/uxsvc/v1/wpt/navigation", method = RequestMethod.GET)
    @ApiOperation( value = "Get parametric navigation choices" )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved category list", response = WptUINavigation.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "application map not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<WptUINavigation> getNavigation() {
        return service.getNavigation();
    }

    /**
     *
     * @param isUserTimingsBaseLine - whether or not to baseline user timings by starting them at the first user timing.
     * for example.  I have 3 user timings.  "start_editor", "load_editor_panel", "load_editor_doc"
     * Assuming "start_editor" comes first, its time would be subtracted from all the other times so you can have a consistent starting point to compare all other measures
     * @param testName
     * @param cached
     * @param runDuration
     * @return
     */
    @RequestMapping(value = "/uxsvc/v1/wpt/trend", method = RequestMethod.GET)
    @ApiOperation( value = "Get UX test trends" )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved category list", response = ApplicationMap.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "application map not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public WptTrendDataResp getTrend(
            @RequestParam(value="name", required = true) String testName,
            @RequestParam(value="cached", defaultValue="true", required = false) boolean cached,
            @RequestParam(value="run", defaultValue="median", required = false) String runDuration,
            @RequestParam(value="utBaseline", defaultValue="false", required = false) boolean isUserTimingsBaseLine) {

        String view = (cached) ? "repeatView" : "firstView"; // default to first view
        try {
            return service.getTrendDataForTest(testName, view, runDuration, isUserTimingsBaseLine);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/uxsvc/v1/graphite", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation( value = "Adds data to graphite", notes = "Data will be stored in the graphiteMetricsUxPath")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully added data to graphite", response = GraphiteSimpleResp.class),
            @ApiResponse(code = 400, message = "Invalid data object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public GraphiteSimpleResp saveToGraphite(@Valid @RequestBody List<GraphiteSimpleMetric> graphiteMetrics, ServerHttpResponse response) {
        List<String> uxMetrics = graphiteMetrics.stream().map(
                r -> (r.getTime() == null) ? graphiteService.buildUxMessage(r.getName(), r.getValue()) : graphiteService.buildUxMessage(r.getName(), r.getValue(), r.getTime())
        ).collect(toList());
        GraphiteSimpleResp resp = new GraphiteSimpleResp(graphiteService.logToGraphite(uxMetrics), graphiteMetrics.size(), uxMetrics);
        if (resp.isSuccess()) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            resp.setErrorMessage("failed to send data to graphite");
        }
        return resp;
    }




}
