package com.opp.dto.reports;

import com.opp.dto.ChartDataResponse;

/**
 * Created by ctobe on 11/22/16.
 */
public class LoadTestSummaryReportCharts {
    private ChartDataResponse respPct90;
    private ChartDataResponse respPct75;
    private ChartDataResponse respMedian;
    private ChartDataResponse respAvg;
    private ChartDataResponse tpsMedian;
    private ChartDataResponse tpsMax;
    private ChartDataResponse tsCallCount; // time series charts
    private ChartDataResponse tsRespPct90; // time series charts

    public LoadTestSummaryReportCharts(ChartDataResponse respPct90, ChartDataResponse respPct75, ChartDataResponse respMedian, ChartDataResponse respAvg, ChartDataResponse tpsMedian, ChartDataResponse tpsMax, ChartDataResponse tsCallCount, ChartDataResponse tsRespPct90) {
        this.respPct90 = respPct90;
        this.respPct75 = respPct75;
        this.respMedian = respMedian;
        this.respAvg = respAvg;
        this.tpsMedian = tpsMedian;
        this.tpsMax = tpsMax;
        this.tsCallCount = tsCallCount;
        this.tsRespPct90 = tsRespPct90;
    }

    public ChartDataResponse getRespPct90() {
        return respPct90;
    }

    public void setRespPct90(ChartDataResponse respPct90) {
        this.respPct90 = respPct90;
    }

    public ChartDataResponse getRespPct75() {
        return respPct75;
    }

    public void setRespPct75(ChartDataResponse respPct75) {
        this.respPct75 = respPct75;
    }

    public ChartDataResponse getRespMedian() {
        return respMedian;
    }

    public void setRespMedian(ChartDataResponse respMedian) {
        this.respMedian = respMedian;
    }

    public ChartDataResponse getRespAvg() {
        return respAvg;
    }

    public void setRespAvg(ChartDataResponse respAvg) {
        this.respAvg = respAvg;
    }

    public ChartDataResponse getTpsMedian() {
        return tpsMedian;
    }

    public void setTpsMedian(ChartDataResponse tpsMedian) {
        this.tpsMedian = tpsMedian;
    }

    public ChartDataResponse getTpsMax() {
        return tpsMax;
    }

    public void setTpsMax(ChartDataResponse tpsMax) {
        this.tpsMax = tpsMax;
    }

    public ChartDataResponse getTsCallCount() {
        return tsCallCount;
    }

    public void setTsCallCount(ChartDataResponse tsCallCount) {
        this.tsCallCount = tsCallCount;
    }

    public ChartDataResponse getTsRespPct90() {
        return tsRespPct90;
    }

    public void setTsRespPct90(ChartDataResponse tsRespPct90) {
        this.tsRespPct90 = tsRespPct90;
    }
}