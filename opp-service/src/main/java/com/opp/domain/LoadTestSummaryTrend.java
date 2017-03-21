package com.opp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ctobe on 8/31/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadTestSummaryTrend {

    private int loadTestId;
    private String appUnderTest;
    private String appUnderTestVersion;
    private String comments;
    private String description;
    private String environment;
    private Long startTime;
    private Long endTime;
    private String testName;
    private String testSubName;
    private String testTool;
    private String testToolVersion;
    private int vuserCount;
    private String externalTestId;
    private int totalCallCount;
    private int respAvg;
    private int respMedian;
    private int respPct90;
    private long totalBytes;
    private double tpsMedian;
    private double tpsMax;
    private int errorCount;
    private String transactionName;

    public int getLoadTestId() {
        return loadTestId;
    }

    public void setLoadTestId(int loadTestId) {
        this.loadTestId = loadTestId;
    }

    public String getAppUnderTest() {
        return appUnderTest;
    }

    public void setAppUnderTest(String appUnderTest) {
        this.appUnderTest = appUnderTest;
    }

    public String getAppUnderTestVersion() {
        return appUnderTestVersion;
    }

    public void setAppUnderTestVersion(String appUnderTestVersion) {
        this.appUnderTestVersion = appUnderTestVersion;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

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

    public String getTestTool() {
        return testTool;
    }

    public void setTestTool(String testTool) {
        this.testTool = testTool;
    }

    public String getTestToolVersion() {
        return testToolVersion;
    }

    public void setTestToolVersion(String testToolVersion) {
        this.testToolVersion = testToolVersion;
    }

    public int getVuserCount() {
        return vuserCount;
    }

    public void setVuserCount(int vuserCount) {
        this.vuserCount = vuserCount;
    }

    public String getExternalTestId() {
        return externalTestId;
    }

    public void setExternalTestId(String externalTestId) {
        this.externalTestId = externalTestId;
    }

    public int getTotalCallCount() {
        return totalCallCount;
    }

    public void setTotalCallCount(int totalCallCount) {
        this.totalCallCount = totalCallCount;
    }

    public int getRespAvg() {
        return respAvg;
    }

    public void setRespAvg(int respAvg) {
        this.respAvg = respAvg;
    }

    public int getRespMedian() {
        return respMedian;
    }

    public void setRespMedian(int respMedian) {
        this.respMedian = respMedian;
    }

    public int getRespPct90() {
        return respPct90;
    }

    public void setRespPct90(int respPct90) {
        this.respPct90 = respPct90;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public double getTpsMedian() {
        return tpsMedian;
    }

    public void setTpsMedian(double tpsMedian) {
        this.tpsMedian = tpsMedian;
    }

    public double getTpsMax() {
        return tpsMax;
    }

    public void setTpsMax(double tpsMax) {
        this.tpsMax = tpsMax;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }
}
