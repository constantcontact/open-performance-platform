package com.opp.domain.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ctobe on 3/22/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WptTestLabel {
    private String full;
    private String environment;
    private String application;
    private String page;
    private String location;
    private String browser;
    private String connection;
    private String misc;

    public WptTestLabel() {
    }

    public WptTestLabel(String fullLabel) {
        String[] labelParts = fullLabel.split("\\.");
        this.full = fullLabel;
        this.environment = labelParts[0];
        this.application = labelParts[1];
        this.page = labelParts[2];
        this.location = labelParts[3];
        this.browser = labelParts[4];
        this.connection = labelParts[5];
        this.misc = (labelParts.length > 6) ? labelParts[6] : "";
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }
}
