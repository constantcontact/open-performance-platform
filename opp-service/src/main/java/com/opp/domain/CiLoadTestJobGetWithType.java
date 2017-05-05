package com.opp.domain;

import javax.validation.constraints.NotNull;

/**
 * Created by ctobe on 4/26/17.
 */
public class CiLoadTestJobGetWithType extends CiLoadTestJob {
    private int job_type_id;
    private String jobType;
    private String additionalOptions;
    private String testTool;
    private String testToolVersion;

    public int getJob_type_id() {
        return job_type_id;
    }

    public void setJob_type_id(int job_type_id) {
        this.job_type_id = job_type_id;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
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
