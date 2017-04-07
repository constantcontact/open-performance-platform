package com.opp.domain.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ctobe on 6/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WptTestImport {

    private String wptId;
    private String wptTestLabel;

    public WptTestImport() {
    }

    public WptTestImport(String wptId, String wptTestLabel) {
        this.wptId = wptId;
        this.wptTestLabel = wptTestLabel;
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
