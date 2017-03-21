package com.opp.domain;

/**
 * Created by jhermida on 9/7/16.
 */
public class LoadTestTimeChartData {
    private int loadTestId;
    private long startTime;
    private String transactionName;
    private int respPct75;
    private int respPct90;
    private int respMin;
    private int respMax;
    private int respAvg;
    private int respMedian;
    private int respStddev;
    private int callCount;
    private int errorCount;
    private int totalBytesReceived;
    private int totalBytesSent;
    private int totalBytes;
    private String chartKey; // used for multithreading and identifying the chart

    public int getLoadTestId() {
        return loadTestId;
    }

    public void setLoadTestId(int loadTestId) {
        this.loadTestId = loadTestId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
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

    public int getRespMin() {
        return respMin;
    }

    public void setRespMin(int respMin) {
        this.respMin = respMin;
    }

    public int getRespMax() {
        return respMax;
    }

    public void setRespMax(int respMax) {
        this.respMax = respMax;
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

    public int getRespStddev() {
        return respStddev;
    }

    public void setRespStddev(int respStddev) {
        this.respStddev = respStddev;
    }

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public int getTotalBytesReceived() {
        return totalBytesReceived;
    }

    public void setTotalBytesReceived(int totalBytesReceived) {
        this.totalBytesReceived = totalBytesReceived;
    }

    public int getTotalBytesSent() {
        return totalBytesSent;
    }

    public void setTotalBytesSent(int totalBytesSent) {
        this.totalBytesSent = totalBytesSent;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }
}
