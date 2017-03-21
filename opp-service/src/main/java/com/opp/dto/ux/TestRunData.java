package com.opp.dto.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by ctobe on 9/28/16.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class TestRunData {
    String timestamp;
    String page;
    String wptId;
    String _id;
    String rev;
    String connectivity;
    JsonNode viewData;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getWptId() {
        return wptId;
    }

    public void setWptId(String wptId) {
        this.wptId = wptId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public JsonNode getViewData() {
        return viewData;
    }

    public void setViewData(JsonNode viewData) {
        this.viewData = viewData;
    }
}

