package com.opp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * Created by ctobe on 6/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadTestAggregate {
    private int loadTestId;
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
    private String transactionName;
    private String customName1;
    private String customName2;
    private String customName3;
    private Double customValue1;
    private Double customValue2;
    private Double customValue3;


    public int getLoadTestId() {
        return loadTestId;
    }

    public void setLoadTestId(int loadTestId) {
        this.loadTestId = loadTestId;
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

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getCustomName1() {
        return customName1;
    }

    public void setCustomName1(String customName1) {
        this.customName1 = customName1;
    }

    public String getCustomName2() {
        return customName2;
    }

    public void setCustomName2(String customName2) {
        this.customName2 = customName2;
    }

    public String getCustomName3() {
        return customName3;
    }

    public void setCustomName3(String customName3) {
        this.customName3 = customName3;
    }

    public Double getCustomValue1() {
        return customValue1;
    }

    public void setCustomValue1(Double customValue1) {
        this.customValue1 = customValue1;
    }

    public Double getCustomValue2() {
        return customValue2;
    }

    public void setCustomValue2(Double customValue2) {
        this.customValue2 = customValue2;
    }

    public Double getCustomValue3() {
        return customValue3;
    }

    public void setCustomValue3(Double customValue3) {
        this.customValue3 = customValue3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadTestAggregate that = (LoadTestAggregate) o;
        return  callCount == that.callCount &&
                respAvg == that.respAvg &&
                respMax == that.respMax &&
                respMedian == that.respMedian &&
                respMin == that.respMin &&
                respPct75 == that.respPct75 &&
                respPct90 == that.respPct90 &&
                Double.compare(that.respStddev, respStddev) == 0 &&
                totalBytesReceived == that.totalBytesReceived &&
                totalBytesSent == that.totalBytesSent &&
                Double.compare(that.tpsMedian, tpsMedian) == 0 &&
                Double.compare(that.tpsMax, tpsMax) == 0 &&
                errorCount == that.errorCount &&
                Objects.equals(transactionName, that.transactionName) &&
                Objects.equals(customName1, that.customName1) &&
                Objects.equals(customName2, that.customName2) &&
                Objects.equals(customName3, that.customName3) &&
                Objects.equals(customValue1, that.customValue1) &&
                Objects.equals(customValue2, that.customValue2) &&
                Objects.equals(customValue3, that.customValue3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callCount, respAvg, respMax, respMedian, respMin, respPct75, respPct90, respStddev, totalBytesReceived, totalBytesSent, tpsMedian, tpsMax, errorCount, transactionName, customName1, customName2, customName3, customValue1, customValue2, customValue3);
    }
}
