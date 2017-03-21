package com.opp.dto.aggregate;


/**
 * Created by ctobe on 6/27/16.
 */
public class LoadTestAggregateDataResp {
    int loadTestId;
    int transactionsAggregated;
    long startTime;
    long endTime;

    public LoadTestAggregateDataResp() {

    }

    public LoadTestAggregateDataResp(int loadTestId, int transactionsAggregated, long startTime, long endTime) {
        this.loadTestId = loadTestId;
        this.transactionsAggregated = transactionsAggregated;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getLoadTestId() {
        return loadTestId;
    }

    public void setLoadTestId(int loadTestId) {
        this.loadTestId = loadTestId;
    }

    public int getTransactionsAggregated() {
        return transactionsAggregated;
    }

    public void setTransactionsAggregated(int transactionsAggregated) {
        this.transactionsAggregated = transactionsAggregated;
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
}
