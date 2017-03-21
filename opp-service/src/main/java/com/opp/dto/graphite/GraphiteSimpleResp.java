package com.opp.dto.graphite;

import java.util.List;

/**
 * Created by ctobe on 10/26/16.
 */
public class GraphiteSimpleResp {
    boolean success;
    int totalProcessed;
    List<String> metricData;
    String errorMessage = "";

    public GraphiteSimpleResp(boolean success, int totalProcessed, List<String> metricData) {
        this.success = success;
        this.totalProcessed = totalProcessed;
        this.metricData = metricData;
    }

    public List<String> getMetricData() {
        return metricData;
    }

    public void setMetricData(List<String> metricData) {
        this.metricData = metricData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }
}
