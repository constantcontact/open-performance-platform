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
public class CustomUserTimingsAgg extends WptTrendMetric.UserTimingMetric {
    private long timePeriod;

    public long getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(long timePeriod) {
        this.timePeriod = timePeriod;
    }

    public CustomUserTimingsAgg(Integer min, Integer max, Integer median, Double average, String name, long timePeriod) {
        super(min, max, median, average, name);
        this.timePeriod = timePeriod;
    }
}


