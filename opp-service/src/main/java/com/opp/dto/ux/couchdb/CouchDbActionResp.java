package com.opp.dto.ux.couchdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ctobe on 9/27/16.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class CouchDbActionResp {
    private String id;
    private boolean ok;
    private String rev;

    // handle errors
    private String error;
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
