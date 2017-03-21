package com.opp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * Created by ctobe on 6/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadTest {
    private int id;
    private String appUnderTest;
    private String appUnderTestVersion;
    private String comments;
    private String description;
    private String environment;
    private Long startTime;
    private Long endTime;
    private String testName;
    private String testSubName;
    private String testTool;
    private String testToolVersion;
    private int vuserCount;
    private Integer slaGroupId;
    private String externalTestId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    public int getVuserCount() {
        return vuserCount;
    }

    public void setVuserCount(int vuserCount) {
        this.vuserCount = vuserCount;
    }

    public Integer getSlaGroupId() {
        return slaGroupId;
    }

    public void setSlaGroupId(Integer slaGroupId) {
        this.slaGroupId = slaGroupId;
    }

    public String getExternalTestId() {
        return externalTestId;
    }

    public void setExternalTestId(String externalTestId) {
        this.externalTestId = externalTestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadTest loadTest = (LoadTest) o;
        return id == loadTest.id &&
                startTime == loadTest.startTime &&
                vuserCount == loadTest.vuserCount &&
                Objects.equals(appUnderTest, loadTest.appUnderTest) &&
                Objects.equals(appUnderTestVersion, loadTest.appUnderTestVersion) &&
                Objects.equals(comments, loadTest.comments) &&
                Objects.equals(description, loadTest.description) &&
                Objects.equals(environment, loadTest.environment) &&
                Objects.equals(endTime, loadTest.endTime) &&
                Objects.equals(testName, loadTest.testName) &&
                Objects.equals(testSubName, loadTest.testSubName) &&
                Objects.equals(testTool, loadTest.testTool) &&
                Objects.equals(testToolVersion, loadTest.testToolVersion) &&
                Objects.equals(slaGroupId, loadTest.slaGroupId) &&
                Objects.equals(externalTestId, loadTest.externalTestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appUnderTest, appUnderTestVersion, comments, description, environment, startTime, endTime, testName, testSubName, testTool, testToolVersion, vuserCount, slaGroupId, externalTestId);
    }

    @Override
    public String toString() {
        return "LoadTest{" +
                "id=" + id +
                ", appUnderTest='" + appUnderTest + '\'' +
                ", appUnderTestVersion='" + appUnderTestVersion + '\'' +
                ", comments='" + comments + '\'' +
                ", description='" + description + '\'' +
                ", environment='" + environment + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", testName='" + testName + '\'' +
                ", testSubName='" + testSubName + '\'' +
                ", testTool='" + testTool + '\'' +
                ", testToolVersion='" + testToolVersion + '\'' +
                ", vuserCount=" + vuserCount +
                ", slaGroupId=" + slaGroupId +
                ", externalTestId='" + externalTestId + '\'' +
                '}';
    }
}
