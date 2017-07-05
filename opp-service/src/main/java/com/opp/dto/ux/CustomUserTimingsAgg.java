package com.opp.dto.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ctobe on 6/13/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUserTimingsAgg {
    private long timePeriod;
    private Map<String, WptTrendMetric.BasicMetric> userTimings;

    public long getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(long timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Map<String, WptTrendMetric.BasicMetric> getUserTimings() {
        return userTimings;
    }

    public void setUserTimings(Map<String, WptTrendMetric.BasicMetric> userTimings) {
        this.userTimings = userTimings;
    }
}


