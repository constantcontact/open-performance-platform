package com.opp.service;

import com.opp.dao.ReportsDao;
import com.opp.domain.ChartDetails;
import com.opp.domain.LoadTest;
import com.opp.domain.LoadTestAggregateView;
import com.opp.domain.LoadTestTimeChartData;
import com.opp.dto.LoadTestAggregateDataWithSlas;
import com.opp.dto.LoadTestRunHistory;
import com.opp.dto.reports.LoadTestSummaryReport;
import com.opp.dto.reports.LoadTestSummaryReportCharts;
import com.opp.exception.InternalServiceException;
import com.opp.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by ctobe on 8/18/16.
 */
@Service
public class ReportService {

    @Autowired
    ReportsDao dao;

    @Autowired
    LoadTestService loadTestService;

    @Autowired
    LoadTestAggregateService loadTestAggService;

    @Autowired
    ChartService chartService;

    public List<LoadTestRunHistory> getLoadTestRunsByDaysBack(int daysBack){
        return dao.findLoadTestRunsByDaysBack(daysBack);
    }

    public LoadTestSummaryReport getLoadTestTrendReport(Integer loadTestId) {

        LoadTest loadTest = loadTestService.getById(loadTestId).orElseThrow(()-> new ResourceNotFoundException("Load Test ID does not exist"));

        ExecutorService executor = Executors.newWorkStealingPool();

        // get all data
        Future<List<LoadTestAggregateDataWithSlas>> futureAggData = executor.submit(() -> loadTestAggService.getByLoadTestIdWithSlas(loadTestId));
        Future<List<LoadTestAggregateView>> futureChartTpsMax = executor.submit(() -> chartService.aggregateChartDataByLoadTestId(new ChartDetails(loadTestId, "tps_max")));
        Future<List<LoadTestAggregateView>> futureChartTpsMedian= executor.submit(() -> chartService.aggregateChartDataByLoadTestId(new ChartDetails(loadTestId, "tps_median")));
        Future<List<LoadTestAggregateView>> futureChartRespAvg = executor.submit(() -> chartService.aggregateChartDataByLoadTestId(new ChartDetails(loadTestId, "resp_avg")));
        Future<List<LoadTestAggregateView>> futureChartRespMedian = executor.submit(() -> chartService.aggregateChartDataByLoadTestId(new ChartDetails(loadTestId, "resp_median")));
        Future<List<LoadTestAggregateView>> futureChartResp75 = executor.submit(() -> chartService.aggregateChartDataByLoadTestId(new ChartDetails(loadTestId, "resp_pct75")));
        Future<List<LoadTestAggregateView>> futureChartResp90 = executor.submit(() -> chartService.aggregateChartDataByLoadTestId(new ChartDetails(loadTestId, "resp_pct90")));
        Future<List<LoadTestTimeChartData>> futureChartTsPct90 = executor.submit(() -> chartService.timeseriesChartDataByLoadTestId(new ChartDetails(loadTestId, "call_count")));
        Future<List<LoadTestTimeChartData>> futureChartTsCallCount = executor.submit(() -> chartService.timeseriesChartDataByLoadTestId(new ChartDetails(loadTestId, "resp_pct90")));

        try {
            List<LoadTestAggregateDataWithSlas> aggData = futureAggData.get();
            LoadTestSummaryReportCharts charts = new LoadTestSummaryReportCharts(
                chartService.mapToAggregateChartResponse(new ChartDetails(loadTestId, "resp_pct90"), futureChartResp90.get()),
                chartService.mapToAggregateChartResponse(new ChartDetails(loadTestId, "resp_pct75"), futureChartResp75.get()),
                chartService.mapToAggregateChartResponse(new ChartDetails(loadTestId, "resp_median"), futureChartRespMedian.get()),
                chartService.mapToAggregateChartResponse(new ChartDetails(loadTestId, "resp_avg"), futureChartRespAvg.get()),
                chartService.mapToAggregateChartResponse(new ChartDetails(loadTestId, "tps_median"), futureChartTpsMedian.get()),
                chartService.mapToAggregateChartResponse(new ChartDetails(loadTestId, "tps_max"), futureChartTpsMax.get()),
                chartService.mapToTimeSeriesChartResponse(new ChartDetails(loadTestId, "call_count"), futureChartTsCallCount.get()),
                chartService.mapToTimeSeriesChartResponse(new ChartDetails(loadTestId, "resp_pct90"), futureChartTsPct90.get())
            );
            return new LoadTestSummaryReport(loadTest, aggData, charts);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new InternalServiceException("Error getting dashboard data");
        }

    }
}

