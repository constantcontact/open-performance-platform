package com.opp.domain;

/**
 * Created by ctobe on 9/13/16.
 */
public class GraphiteMetric {
    int graphiteId;
    int applicationId;
    String appKey;
    String name;
    String graphitePath;

    public int getGraphiteId() {
        return graphiteId;
    }

    public void setGraphiteId(int graphiteId) {
        this.graphiteId = graphiteId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGraphitePath() {
        return graphitePath;
    }

    public void setGraphitePath(String graphitePath) {
        this.graphitePath = graphitePath;
    }
}
