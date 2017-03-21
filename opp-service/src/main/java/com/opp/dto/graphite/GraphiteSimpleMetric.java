package com.opp.dto.graphite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ctobe on 10/26/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphiteSimpleMetric {
    String name;
    Long value;
    Long time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
