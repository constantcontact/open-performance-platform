package com.opp.dto.reports;

import com.opp.domain.LoadTest;
import com.opp.dto.LoadTestAggregateDataWithSlas;

import java.util.List;

/**
 * Created by ctobe on 11/21/16.
 */
public class LoadTestSummaryReport {

    LoadTest loadTest;
    List<LoadTestAggregateDataWithSlas> aggregateData;
    LoadTestSummaryReportCharts charts;

    public LoadTestSummaryReport(LoadTest loadTest, List<LoadTestAggregateDataWithSlas> aggregateData, LoadTestSummaryReportCharts charts) {
        this.loadTest = loadTest;
        this.aggregateData = aggregateData;
        this.charts = charts;
    }

    public LoadTest getLoadTest() {
        return loadTest;
    }

    public void setLoadTest(LoadTest loadTest) {
        this.loadTest = loadTest;
    }

    public List<LoadTestAggregateDataWithSlas> getAggregateData() {
        return aggregateData;
    }

    public void setAggregateData(List<LoadTestAggregateDataWithSlas> aggregateData) {
        this.aggregateData = aggregateData;
    }

    public LoadTestSummaryReportCharts getCharts() {
        return charts;
    }

    public void setCharts(LoadTestSummaryReportCharts charts) {
        this.charts = charts;
    }
}
