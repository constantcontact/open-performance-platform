package com.opp.controller.ux;

import com.opp.domain.ux.WptResult;
import com.opp.domain.ux.WptTestImport;
import com.opp.domain.ux.WptUINavigation;
import com.opp.dto.ErrorResponse;
import com.opp.dto.graphite.GraphiteSimpleMetric;
import com.opp.dto.graphite.GraphiteSimpleResp;
import com.opp.dto.ux.WptDeleteRequest;
import com.opp.dto.ux.WptDeleteResp;
import com.opp.dto.ux.WptTestRunData;
import com.opp.dto.ux.WptTrendChart;
import com.opp.exception.BadRequestException;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import com.opp.service.GraphiteService;
import com.opp.service.WptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;

/**
 * Created by ctobe on 10/18/16.
 */
@Api(value = "WebPageTest", description = "WPT API")
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
            @ApiResponse(code = 201, message = "Successfully created new WPT Test", response = IndexResponse.class),
            @ApiResponse(code = 400, message = "Invalid WPT Test object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public WptResult create(@Valid @RequestBody WptTestImport wptTestImport) throws Exception {
        return service.importTest(wptTestImport).orElseThrow(()->new InternalServiceException("Error occurred while creating WPT Test"));
    }

    @RequestMapping(value = "/uxsvc/v1/wpt/tests/{wpt_test_id}", method = RequestMethod.PUT)
    @ApiOperation(
            value = "Update an WPT Test",
            notes = "Updates an existing WPT Test"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated WPT Test", response = WptResult.class),
            @ApiResponse(code = 400, message = "Invalid WPT Test object provided", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "WPT Test not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public WptResult update(@PathVariable("wpt_test_id") String wptTestId, @Valid @RequestBody WptResult update) throws ExecutionException, InterruptedException {
        return service.update(wptTestId, update).orElseThrow(()->new InternalServiceException("Error occurred while updating WPT Test"));
    }

    @RequestMapping(value = "/uxsvc/v1/wpt/tests/{wpt_test_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(
            value = "Delete a WPT Test",
            notes = "Deletes an existing WPT Test"
    )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted WPT Test", response = WptDeleteResp.class),
            @ApiResponse(code = 400, message = "Failed to delete WPT Test", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "WPT Test not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public WptDeleteResp delete(@PathVariable("wpt_test_id") String wptTestId, HttpServletResponse response) {
        WptDeleteResp wptDeleteResp = service.deleteById(wptTestId);
        if(wptDeleteResp.getDeleteCount() == 0){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            return wptDeleteResp;
        }
        return wptDeleteResp;
    }

    @RequestMapping(value = "/uxsvc/v1/wpt/tests", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation( value = "Delete a WPT Test by ID or Label")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted WPT Test(s)", response = WptDeleteResp.class),
            @ApiResponse(code = 400, message = "Failed to delete WPT Test", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "WPT Test not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public WptDeleteResp delete(@Valid @RequestBody WptDeleteRequest request) {
        WptDeleteResp wptDeleteResp = null;
        if(request.getId() != null && !request.getId().isEmpty()){
            // delete by id
            wptDeleteResp = service.deleteById(request.getId());
            if(wptDeleteResp.getDeleteCount() == 0){
                throw new ResourceNotFoundException("No test found with ID " + request.getId());
            }
        } else {
            // delete by label
            if(request.getLabel() != null && !request.getLabel().isEmpty()){
                wptDeleteResp = service.deleteByLabel(request.getLabel());
                // don't think i need to throw a 404 if nothing was deleted here since you aren't asking for a specific ID
            }
        }
        if(wptDeleteResp == null){
            // throw invalid request
            throw new BadRequestException("Invalid request.  Must have either a label or id specified");
        } else {
            return wptDeleteResp;
        }
    }

    @RequestMapping(value = "/uxsvc/v1/wpt/tests/{wpt_test_id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get a WPT Test",
            notes = "Gets a WPT Test by ID"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved WPT Test", response = WptResult.class),
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
            @ApiResponse(code = 200, message = "Successfully retrieved WPT Tests", response = WptResult.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "WPT Test not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<WptResult> getAll(@RequestParam(value="esquery", defaultValue="*", required = false) String esQuery) {
        return service.getAll(esQuery);
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
     * @param testName
     * @param view
     * @param run
     * @return
     */
    @RequestMapping(value = "/uxsvc/v1/wpt/trend/table", method = RequestMethod.GET)
    @ApiOperation( value = "Get UX test high level results by label" )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved test run data list", response = WptTestRunData.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Test with specified name not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<WptTestRunData> getTrendTable(
            @RequestParam(value="name") String testName,
            @RequestParam(value="view", defaultValue="firstView", required = false) String view,
            @RequestParam(value="run", defaultValue="median", required = false) String run) {
        return service.getTrendTableData(testName, view, run);
    }


    /**
     *
     * @param testName
     * @param view
     * @param isUserTimingsBaseLine - whether or not to baseline user timings by starting them at the first user timing.
     * for example.  I have 3 user timings.  "start_editor", "load_editor_panel", "load_editor_doc"
     * Assuming "start_editor" comes first, its time would be subtracted from all the other times so you can have a consistent starting point to compare all other measures
     * @param interval
     * @return
     */
    @RequestMapping(value = "/uxsvc/v1/wpt/trend/histogram", method = RequestMethod.GET)
    @ApiOperation( value = "Get UX test trends by label as histogram aggregated by day" )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved histogram", response = WptTrendChart.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Test with specified name not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public WptTrendChart getTrendHistogram(
            @RequestParam(value="name") String testName,
            @RequestParam(value="view", defaultValue="firstView", required = false) String view,
            @RequestParam(value="utBaseline", defaultValue="false", required = false) boolean isUserTimingsBaseLine,
            @RequestParam(value="interval", defaultValue="1d", required = false) String interval
    ) {
        return service.getTrendChartData(testName, view, isUserTimingsBaseLine, interval);
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
