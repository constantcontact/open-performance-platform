package com.opp.controller.ux;

import com.opp.domain.ApplicationMap;
import com.opp.domain.ux.WptNavCategory;
import com.opp.dto.ErrorResponse;
import com.opp.dto.graphite.GraphiteSimpleMetric;
import com.opp.dto.graphite.GraphiteSimpleResp;
import com.opp.dto.ux.WptTrendDataResp;
import com.opp.dto.ux.couchdb.DeleteWptDocResp;
import com.opp.exception.BadRequestException;
import com.opp.service.GraphiteService;
import com.opp.service.WptService;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.eclipsesource.json.Json.array;
import static java.util.stream.Collectors.joining;
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

    @RequestMapping(value = "/uxsvc/v1/wpt/categories", method = RequestMethod.GET)
    @ApiOperation( value = "Get parametric navigation choices" )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved category list", response = Map.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "application map not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public Map<String, List<WptNavCategory>> getCategories() {
        return service.getCategories();
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

    @RequestMapping(value = "/uxsvc/v1/tests", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation( value = "Delete tests by name or by ID", notes = "The couchid is the wpt-summary id")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Successfully deleted tests", response = Void.class),
            @ApiResponse(code = 400, message = "Failed to delete tests", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Tests not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<DeleteWptDocResp> deleteAllByTestName(
            @RequestParam(name = "name", required = false) String testName,
            @RequestParam(name = "wptid", required = false) String wptId,
            @RequestParam(name = "couchid", required = false) String couchSummaryId
    ){
        try {
            if(testName != null){
                return service.deleteByName(testName);
            } else {
                return Arrays.asList(service.deleteById(wptId, couchSummaryId));
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            throw new BadRequestException("Failed deleting tests from couchdb");
        }
    }



}
