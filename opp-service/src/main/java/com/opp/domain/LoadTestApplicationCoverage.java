package com.opp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * Created by ctobe on 6/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadTestApplicationCoverage {
    private int applicationId;
    private String loadTestName;

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getLoadTestName() {
        return loadTestName;
    }

    public void setLoadTestName(String loadTestName) {
        this.loadTestName = loadTestName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadTestApplicationCoverage that = (LoadTestApplicationCoverage) o;
        return applicationId == that.applicationId &&
                Objects.equals(loadTestName, that.loadTestName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, loadTestName);
    }
}
