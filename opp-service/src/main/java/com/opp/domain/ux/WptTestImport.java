package com.opp.domain.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ctobe on 6/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WptTestImport {

    private String wptId;
    private WptTestLabel wptTestLabel;

    public WptTestImport() {
    }

    public WptTestImport(String wptId, WptTestLabel wptTestLabel) {
        this.wptId = wptId;
        this.wptTestLabel = wptTestLabel;
    }

    public String getWptId() {
        return wptId;
    }

    public void setWptId(String wptId) {
        this.wptId = wptId;
    }

    public WptTestLabel getWptTestLabel() {
        return wptTestLabel;
    }

    public void setWptTestLabel(WptTestLabel wptTestLabel) {
        this.wptTestLabel = wptTestLabel;
    }

    public void setWptTestLabel(String fullLabel) {
        this.wptTestLabel = new WptTestLabel(fullLabel);

    }
}
