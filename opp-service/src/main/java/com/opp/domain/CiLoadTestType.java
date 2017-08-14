package com.opp.domain;

import javax.validation.constraints.NotNull;

/**
 * Created by ctobe on 4/26/17.
 */
public class CiLoadTestType {

    private int id;
    @NotNull
    private String testType;
    private String additionalOptions;
    private String testTool;
    private String testToolVersion;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getAdditionalOptions() {
        return additionalOptions;
    }

    public void setAdditionalOptions(String additionalOptions) {
        this.additionalOptions = additionalOptions;
    }

    public String getTestTool() {
        return testTool;
    }

    public void setTestTool(String testTool) {
        this.testTool = testTool;
    }

    public String getTestToolVersion() {
        return testToolVersion;
    }

    public void setTestToolVersion(String testToolVersion) {
        this.testToolVersion = testToolVersion;
    }
}
