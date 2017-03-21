package com.opp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

/**
 * Created by ctobe on 8/18/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadTestRunHistory {
    LocalDateTime lastRun;
    int totalRuns;
    String testName;
    String testSubName;
    String appUnderTest;
    String environment;
    boolean hasSla;
    String description;
    String comments;

    public LocalDateTime getLastRun() {
        return lastRun;
    }

    public void setLastRun(LocalDateTime lastRun) {
        this.lastRun = lastRun;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
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

    public String getAppUnderTest() {
        return appUnderTest;
    }

    public void setAppUnderTest(String appUnderTest) {
        this.appUnderTest = appUnderTest;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public boolean isHasSla() {
        return hasSla;
    }

    public void setHasSla(boolean hasSla) {
        this.hasSla = hasSla;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
