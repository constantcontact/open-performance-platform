package com.opp.domain;

import javax.validation.constraints.NotNull;

/**
 * Created by ctobe on 4/26/17.
 */
public class CiLoadTestJob {
    private int id;
    @NotNull
    private String testPath;
    private String testType;
    @NotNull
    private String testName;
    private String testSubName;
    @NotNull
    private Long runDuration;
    private String appUnderTest;
    private String appUnderTestVersion;
    private String environment; // can be comma delimited
    @NotNull
    private Integer vuserCount;
    private String description;
    private String comments;
    private String testDataType;
    private Integer slaGroupId;
    private Integer rampVuserStartDelay;
    private Integer rampVuserEndDelay;
    private String cronSchedule;
    private String hostName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestPath() {
        return testPath;
    }

    public void setTestPath(String testPath) {
        this.testPath = testPath;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = this.testType;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestSubName() {
        return testSubName;
    }

    public void setTestSubName(String testSubName) {
        this.testSubName = testSubName;
    }

    public Long getRunDuration() {
        return runDuration;
    }

    public void setRunDuration(Long runDuration) {
        this.runDuration = runDuration;
    }

    public String getAppUnderTest() {
        return appUnderTest;
    }

    public void setAppUnderTest(String appUnderTest) {
        this.appUnderTest = appUnderTest;
    }

    public String getAppUnderTestVersion() {
        return appUnderTestVersion;
    }

    public void setAppUnderTestVersion(String appUnderTestVersion) {
        this.appUnderTestVersion = appUnderTestVersion;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Integer getVuserCount() {
        return vuserCount;
    }

    public void setVuserCount(Integer vuserCount) {
        this.vuserCount = vuserCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTestDataType() {
        return testDataType;
    }

    public void setTestDataType(String testDataType) {
        this.testDataType = testDataType;
    }

    public Integer getSlaGroupId() {
        return slaGroupId;
    }

    public void setSlaGroupId(Integer slaGroupId) {
        this.slaGroupId = slaGroupId;
    }

    public Integer getRampVuserStartDelay() {
        return rampVuserStartDelay;
    }

    public void setRampVuserStartDelay(Integer rampVuserStartDelay) {
        this.rampVuserStartDelay = rampVuserStartDelay;
    }

    public Integer getRampVuserEndDelay() {
        return rampVuserEndDelay;
    }

    public void setRampVuserEndDelay(Integer rampVuserEndDelay) {
        this.rampVuserEndDelay = rampVuserEndDelay;
    }

    public String getCronSchedule() {
        return cronSchedule;
    }

    public void setCronSchedule(String cronSchedule) {
        this.cronSchedule = cronSchedule;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

}
