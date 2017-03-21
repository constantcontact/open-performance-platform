package com.opp.dto.aggregate;


/**
 * Created by ctobe on 6/27/16.
 */
public class LoadTestAggregateImportResp {
    int loadTestId;
    int totalRecords;
    int totalCreated;
    long completionTimeSec;

    public LoadTestAggregateImportResp() {

    }

    public LoadTestAggregateImportResp(int loadTestId, int totalRecords, int totalCreated, long completionTimeSec) {
        this.loadTestId = loadTestId;
        this.totalRecords = totalRecords;
        this.totalCreated = totalCreated;
        this.completionTimeSec = completionTimeSec;
    }

    public long getCompletionTimeSec() {
        return completionTimeSec;
    }

    public void setCompletionTimeSec(long completionTimeSec) {
        this.completionTimeSec = completionTimeSec;
    }

    public int getLoadTestId() {
        return loadTestId;
    }

    public void setLoadTestId(int loadTestId) {
        this.loadTestId = loadTestId;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getTotalCreated() {
        return totalCreated;
    }

    public void setTotalCreated(int totalCreated) {
        this.totalCreated = totalCreated;
    }
}
