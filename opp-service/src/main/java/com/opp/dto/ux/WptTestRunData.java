package com.opp.dto.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.opp.domain.ux.WptTestLabel;

/**
 * Created by ctobe on 9/28/16.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class WptTestRunData {
    private Long date;
    private WptTestLabel label;
    private String id;
    private String summaryUrl;
    private Integer ttfb;
    private Integer visuallyComplete;
    private Integer speedIndex;
    private Integer aboveTheFold;

    public WptTestRunData(Long date, WptTestLabel label, String id, String summaryUrl, Integer ttfb, Integer visuallyComplete, Integer speedIndex, Integer aboveTheFold) {
        this.date = date;
        this.label = label;
        this.id = id;
        this.summaryUrl = summaryUrl;
        this.ttfb = ttfb;
        this.visuallyComplete = visuallyComplete;
        this.speedIndex = speedIndex;
        this.aboveTheFold = aboveTheFold;
    }

    public String getSummaryUrl() {
        return summaryUrl;
    }

    public void setSummaryUrl(String summaryUrl) {
        this.summaryUrl = summaryUrl;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public WptTestLabel getLabel() {
        return label;
    }

    public void setLabel(WptTestLabel label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTtfb() {
        return ttfb;
    }

    public void setTtfb(Integer ttfb) {
        this.ttfb = ttfb;
    }

    public Integer getVisuallyComplete() {
        return visuallyComplete;
    }

    public void setVisuallyComplete(Integer visuallyComplete) {
        this.visuallyComplete = visuallyComplete;
    }

    public Integer getSpeedIndex() {
        return speedIndex;
    }

    public void setSpeedIndex(Integer speedIndex) {
        this.speedIndex = speedIndex;
    }

    public Integer getAboveTheFold() {
        return aboveTheFold;
    }

    public void setAboveTheFold(Integer aboveTheFold) {
        this.aboveTheFold = aboveTheFold;
    }
}

