package com.opp.controller;

import com.opp.domain.ChartDetails;
import com.opp.domain.LoadTestAggregateView;
import com.opp.domain.LoadTestTimeChartData;
import com.opp.dto.ChartDataResponse;
import com.opp.dto.ErrorResponse;
import com.opp.service.ChartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jhermida on 8/24/16.
 */
@Api(value = "Charts", description = "Charting Data", basePath = "/loadsvc")
@RestController
public class ChartController {

    @Autowired
    private ChartService chartService;

    @RequestMapping(value = "/loadsvc/v1/charts/aggregate/loadtests/{id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Gets a line series chart.",
            notes = "Gets charts data aggregation by load test id."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved chart aggregation data.", response = ChartDataResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Load test id not found.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = ErrorResponse.class)
    })
    public ChartDataResponse aggregateChartDataByLoadTestId(@PathVariable("id") Integer loadTestId,
                                                            @RequestParam(value="yaxis") String yAxis,
                                                            @RequestParam(value="xaxis", defaultValue="start_time", required = false) String xAxis,
                                                            @RequestParam(value="plot", defaultValue="transaction_name", required = false) String plot,
                                                            @RequestParam(value="trendOn", defaultValue="test_name", required = false) String trendOn) {

        ChartDetails chartDetails = new ChartDetails();
        chartDetails.setLoadTestId(loadTestId);
        chartDetails.setyAxis(yAxis);
        chartDetails.setxAxis(xAxis);
        chartDetails.setPlotBy(plot);
        chartDetails.setTrendingBy(trendOn);

        List<LoadTestAggregateView> chartData = chartService.aggregateChartDataByLoadTestId(chartDetails);

        return chartService.mapToAggregateChartResponse(chartDetails, chartData);
    }

    @RequestMapping(value = "/loadsvc/v1/charts/timeseries/loadtests/{id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Gets a time series line chart.",
            notes = "Gets a time series line chart by load test id."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved chart aggregation data.", response = ChartDataResponse.class),
            @ApiResponse(code = 401, message = "Failed authentication or not authorized.", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Load test id not found.", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = ErrorResponse.class)
    })
    public ChartDataResponse timeSeriesChartDataByLoadTestId(@PathVariable("id") Integer loadTestId,
                                                             @RequestParam(value="yaxis") String yAxis,
                                                             @RequestParam(value="xaxis", defaultValue="start_time", required = false) String xAxis,
                                                             @RequestParam(value="plot", defaultValue="transaction_name", required = false) String plot) {
        ChartDetails chartDetails = new ChartDetails();
        chartDetails.setLoadTestId(loadTestId);
        chartDetails.setyAxis(yAxis);
        chartDetails.setxAxis(xAxis);
        chartDetails.setPlotBy(plot);

        List<LoadTestTimeChartData> chartData = chartService.timeseriesChartDataByLoadTestId(chartDetails);

        return chartService.mapToTimeSeriesChartResponse(chartDetails, chartData);
    }


    public void setChartService(ChartService chartService) {
        this.chartService = chartService;
    }
}
