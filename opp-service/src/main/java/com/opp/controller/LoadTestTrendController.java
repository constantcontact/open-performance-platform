package com.opp.controller;

import com.opp.dto.ErrorResponse;
import com.opp.dto.LoadTestSummaryTrendGet;
import com.opp.dto.SummaryTrendByGroup;
import com.opp.service.LoadTestTrendService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ctobe on 8/23/16.
 */
@Api(value = "LoadTestTrends", description = "Load Test Trends API", basePath = "/loadsvc")
@RestController
public class LoadTestTrendController {

    @Autowired
    private LoadTestTrendService service;

    @RequestMapping(value = "/loadsvc/v1/loadtesttrends/summarytrend", method = RequestMethod.GET)
    @ApiOperation( value = "Get summary trend for load test", notes = "If IDs is set in query param, the filter will be ignored")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved LoadTest Summary Trends", response = LoadTestSummaryTrendGet.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<LoadTestSummaryTrendGet> getSummaryTrend(@RequestParam(value="ids", defaultValue="", required = false) String ids, @RequestParam(value="filter", defaultValue="", required = false) String filter) {
        if(ids.isEmpty()){
            return service.getSummaryTrendsByFilter(filter);
        } else {
            return service.getSummaryTrendByIds(ids);
        }
    }

    @RequestMapping(value = "/loadsvc/v1/loadtesttrends/summarytrendgroup", method = RequestMethod.GET)
    @ApiOperation( value = "Get summary group rend for load tests", notes = "Setting sampleDataOnly to true will return a summarized list of data.  You can also pass in any column names from the LoadTest table and set a regular expression value to filter on that column")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUnderTest", paramType = "query"),
            @ApiImplicitParam(name = "appUnderTestVersion", paramType = "query"),
            @ApiImplicitParam(name = "comments", paramType = "query"),
            @ApiImplicitParam(name = "description", paramType = "query"),
            @ApiImplicitParam(name = "environment", paramType = "query"),
            @ApiImplicitParam(name = "startTime", paramType = "query"),
            @ApiImplicitParam(name = "endTime", paramType = "query"),
            @ApiImplicitParam(name = "testName", paramType = "query"),
            @ApiImplicitParam(name = "testSubName", paramType = "query"),
            @ApiImplicitParam(name = "testTool", paramType = "query"),
            @ApiImplicitParam(name = "testToolVersion", paramType = "query"),
            @ApiImplicitParam(name = "vuserCount", paramType = "query"),
            @ApiImplicitParam(name = "slaGroupId", paramType = "query"),
            @ApiImplicitParam(name = "externalTestId", paramType = "query")
    }
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved LoadTest Summary Trends", response = SummaryTrendByGroup.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Invalid request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public List<SummaryTrendByGroup> getSummaryTrendGroup(@RequestParam(value="sampleDataOnly", defaultValue="false", required = false) Boolean sampleDataOnly, HttpServletRequest request) {
        // map all query parameters and filter out sampleDataOnly
        Map<String, String> queryParamMap = request.getParameterMap().entrySet().stream().filter(e->!e.getKey().equals("sampleDataOnly")).collect(Collectors.toMap(Map.Entry::getKey, e -> StringUtils.join(e.getValue(), ","), (v1, v2) -> null, HashMap::new));
        if(sampleDataOnly){
            return service.getSummaryTrendByGroupSampleData(queryParamMap);
        } else {
            return service.getSummaryTrendByGroupFilter(queryParamMap);
        }
    }

}