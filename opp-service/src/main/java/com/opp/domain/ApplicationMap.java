package com.opp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by ctobe on 4/27/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationMap {
    private Integer id;

    @ApiModelProperty(value = "The application key", required = true)
    @NotBlank
    private String appKey;
    private String newrelic;
    private String appdynamics;
    private String webpagetest;
    private String splunk;
    private String dynatrace;
    private boolean isClientSide;
    private boolean isServerSide;
    private boolean inCdPipelineClient;
    private boolean inCdPipelineServer;
    private String codeCoverageId;
    private Integer securityId;
    private Integer regressionResultsId;
    private String kqiAppName;
    private String teamName;

    public ApplicationMap() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getNewrelic() {
        return newrelic;
    }

    public void setNewrelic(String newrelic) {
        this.newrelic = newrelic;
    }

    public String getAppdynamics() {
        return appdynamics;
    }

    public void setAppdynamics(String appdynamics) {
        this.appdynamics = appdynamics;
    }

    public String getWebpagetest() {
        return webpagetest;
    }

    public void setWebpagetest(String webpagetest) {
        this.webpagetest = webpagetest;
    }

    public String getSplunk() {
        return splunk;
    }

    public void setSplunk(String splunk) {
        this.splunk = splunk;
    }

    public String getDynatrace() {
        return dynatrace;
    }

    public void setDynatrace(String dynatrace) {
        this.dynatrace = dynatrace;
    }

    public boolean getIsClientSide() {
        return isClientSide;
    }

    public void setIsClientSide(boolean clientSide) {
        isClientSide = clientSide;
    }

    public boolean getIsServerSide() {
        return isServerSide;
    }

    public void setIsServerSide(boolean serverSide) {
        isServerSide = serverSide;
    }

    public boolean isInCdPipelineClient() {
        return inCdPipelineClient;
    }

    public void setInCdPipelineClient(boolean inCdPipelineClient) {
        this.inCdPipelineClient = inCdPipelineClient;
    }

    public boolean isInCdPipelineServer() {
        return inCdPipelineServer;
    }

    public void setInCdPipelineServer(boolean inCdPipelineServer) {
        this.inCdPipelineServer = inCdPipelineServer;
    }

    public String getCodeCoverageId() {
        return codeCoverageId;
    }

    public void setCodeCoverageId(String codeCoverageId) {
        this.codeCoverageId = codeCoverageId;
    }

    public Integer getSecurityId() {
        return securityId;
    }

    public void setSecurityId(Integer securityId) {
        this.securityId = securityId;
    }

    public Integer getRegressionResultsId() {
        return regressionResultsId;
    }

    public void setRegressionResultsId(Integer regressionResultsId) {
        this.regressionResultsId = regressionResultsId;
    }

    public String getKqiAppName() {
        return kqiAppName;
    }

    public void setKqiAppName(String kqiAppName) {
        this.kqiAppName = kqiAppName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
