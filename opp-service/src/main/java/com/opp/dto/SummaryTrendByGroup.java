package com.opp.dto;

import com.opp.domain.LoadTestSummaryTrend;

import java.util.List;

/**
 * Created by ctobe on 9/2/16.
 */
public class SummaryTrendByGroup {

    private String testName;
    private String testSubName;
    private String appUnderTest;
    private int vuserCount;
    private LoadTestSummaryTrend latestTest;
    private List<LoadTestSummaryTrendGet> summaryTrend;
    private List<Integer> sparkline90;
    private List<Integer> sparkline50;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestSubName() {
        return testSubName;
    }

    public void setTestSubName(String testSubName) {
        this.testSubName = testSubName;
    }

    public String getAppUnderTest() {
        return appUnderTest;
    }

    public void setAppUnderTest(String appUnderTest) {
        this.appUnderTest = appUnderTest;
    }

    public int getVuserCount() {
        return vuserCount;
    }

    public void setVuserCount(int vuserCount) {
        this.vuserCount = vuserCount;
    }

    public LoadTestSummaryTrend getLatestTest() {
        return latestTest;
    }

    public void setLatestTest(LoadTestSummaryTrend latestTest) {
        this.latestTest = latestTest;
    }

    public List<LoadTestSummaryTrendGet> getSummaryTrend() {
        return summaryTrend;
    }

    public void setSummaryTrend(List<LoadTestSummaryTrendGet> summaryTrend) {
        this.summaryTrend = summaryTrend;
    }

    public List<Integer> getSparkline90() {
        return sparkline90;
    }

    public void setSparkline90(List<Integer> sparkline90) {
        this.sparkline90 = sparkline90;
    }

    public List<Integer> getSparkline50() {
        return sparkline50;
    }

    public void setSparkline50(List<Integer> sparkline50) {
        this.sparkline50 = sparkline50;
    }
}
