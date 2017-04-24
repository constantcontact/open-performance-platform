package com.opp.domain.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ctobe on 6/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WptTestImport {

    private String wptId;
    private String wptTestLabel;
    private String data;

    public WptTestImport() {
    }

    public WptTestImport(String wptId, String wptTestLabel) {
        this.wptId = wptId;
        this.wptTestLabel = wptTestLabel;
    }

    public WptTestImport(String wptId, String wptTestLabel, String data) {
        this.wptId = wptId;
        this.wptTestLabel = wptTestLabel;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getWptId() {
        return wptId;
    }

    public void setWptId(String wptId) {
        this.wptId = wptId;
    }

    public String getWptTestLabel() {
        return wptTestLabel;
    }

    public void setWptTestLabel(String wptTestLabel) {
        this.wptTestLabel = wptTestLabel;
    }
}
