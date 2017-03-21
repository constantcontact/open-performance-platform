package com.opp.dto.maintenance;

import com.opp.dto.aggregate.LoadTestAggregateDataResp;

import java.util.List;

/**
 * Created by ctobe on 6/29/16.
 */
public class MaintReaggregateDataResp {
    int totalProcessed;
    int totalAggregated;
    List<LoadTestAggregateDataResp> processed;

    public MaintReaggregateDataResp() {
    }

    public MaintReaggregateDataResp(int totalProcessed, int totalAggregated, List<LoadTestAggregateDataResp> processed) {
        this.totalProcessed = totalProcessed;
        this.totalAggregated = totalAggregated;
        this.processed = processed;
    }

    public int getTotalAggregated() {
        return totalAggregated;
    }

    public void setTotalAggregated(int totalAggregated) {
        this.totalAggregated = totalAggregated;
    }

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }

    public List<LoadTestAggregateDataResp> getProcessed() {
        return processed;
    }

    public void setProcessed(List<LoadTestAggregateDataResp> processed) {
        this.processed = processed;
    }
}
