package com.opp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Optional;

/**
 * Created by ctobe on 8/16/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadTestApplicationCoverageFilter {

    private Optional<String> loadTestName = Optional.empty();
    private Optional<Integer> applicationId = Optional.empty();

    public LoadTestApplicationCoverageFilter() {
    }

    public LoadTestApplicationCoverageFilter(Optional<String> loadTestName, Optional<Integer> applicationId) {
        this.loadTestName = loadTestName;
        this.applicationId = applicationId;
    }

    public Optional<String> getLoadTestName() {
        return loadTestName;
    }

    public void setLoadTestName(Optional<String> loadTestName) {
        this.loadTestName = loadTestName;
    }

    public Optional<Integer> getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Optional<Integer> applicationId) {
        this.applicationId = applicationId;
    }
}
