package com.opp.dto.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ctobe on 6/13/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUserTimingsAgg {
    private long timePeriod;
    private List<WptTrendMetric.BasicMetric> userTimings;

    public long getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(long timePeriod) {
        this.timePeriod = timePeriod;
    }

    public List<WptTrendMetric.BasicMetric> getUserTimings() {
        return userTimings;
    }

    public void setUserTimings(List<WptTrendMetric.BasicMetric> userTimings) {
        this.userTimings = userTimings;
    }
}


