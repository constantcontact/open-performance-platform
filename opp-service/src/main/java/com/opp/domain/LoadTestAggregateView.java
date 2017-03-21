package com.opp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by jhermida on 9/6/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadTestAggregateView {
    private int loadTestId;
    private String appUnderTest;
    private String appUnderTestVersion;
    private String comments;
    private String description;
    private String environment;
    private long startTime;
    private long endTime;
    private String testName;
    private String testSubName;
    private String testTool;
    private String testToolVersion;
    private int vuserCount;
    private String externalTestId;
    private String transactionName;
    private int callCount;
    private int respAvg;
    private int respMax;
    private int respMedian;
    private int respMin;
    private int respPct75;
    private int respPct90;
    private double respStddev;
    private long totalBytesReceived;
    private long totalBytesSent;
    private double tpsMedian;
    private double tpsMax;
    private int errorCount;

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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
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

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public int getRespAvg() {
        return respAvg;
    }

    public void setRespAvg(int respAvg) {
        this.respAvg = respAvg;
    }

    public int getRespMax() {
        return respMax;
    }

    public void setRespMax(int respMax) {
        this.respMax = respMax;
    }

    public int getRespMedian() {
        return respMedian;
    }

    public void setRespMedian(int respMedian) {
        this.respMedian = respMedian;
    }

    public int getRespMin() {
        return respMin;
    }

    public void setRespMin(int respMin) {
        this.respMin = respMin;
    }

    public int getRespPct75() {
        return respPct75;
    }

    public void setRespPct75(int respPct75) {
        this.respPct75 = respPct75;
    }

    public int getRespPct90() {
        return respPct90;
    }

    public void setRespPct90(int respPct90) {
        this.respPct90 = respPct90;
    }

    public double getRespStddev() {
        return respStddev;
    }

    public void setRespStddev(double respStddev) {
        this.respStddev = respStddev;
    }

    public long getTotalBytesReceived() {
        return totalBytesReceived;
    }

    public void setTotalBytesReceived(long totalBytesReceived) {
        this.totalBytesReceived = totalBytesReceived;
    }

    public long getTotalBytesSent() {
        return totalBytesSent;
    }

    public void setTotalBytesSent(long totalBytesSent) {
        this.totalBytesSent = totalBytesSent;
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
}
