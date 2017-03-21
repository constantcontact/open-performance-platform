package com.opp.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by ctobe on 6/28/16.
 */
public class Sla {
    private int slaId;
    private String team;
    private String application;
    private String name;
    private String endPoint;
    private String type;
    private String userType;
    private Integer serverRespTime90Ms;
    private Integer clientRespTimeMs;
    private BigDecimal rpm;
    private Integer pageSizeKb;
    private Timestamp createdDt;

    public int getSlaId() {
        return slaId;
    }

    public void setSlaId(int slaId) {
        this.slaId = slaId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getServerRespTime90Ms() {
        return serverRespTime90Ms;
    }

    public void setServerRespTime90Ms(Integer serverRespTime90Ms) {
        this.serverRespTime90Ms = serverRespTime90Ms;
    }

    public Integer getClientRespTimeMs() {
        return clientRespTimeMs;
    }

    public void setClientRespTimeMs(Integer clientRespTimeMs) {
        this.clientRespTimeMs = clientRespTimeMs;
    }

    public BigDecimal getRpm() {
        return rpm;
    }

    public void setRpm(BigDecimal rpm) {
        this.rpm = rpm;
    }

    public Integer getPageSizeKb() {
        return pageSizeKb;
    }

    public void setPageSizeKb(Integer pageSizeKb) {
        this.pageSizeKb = pageSizeKb;
    }

    public Timestamp getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Timestamp createdDt) {
        this.createdDt = createdDt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sla sla = (Sla) o;
        return slaId == sla.slaId &&
                Objects.equals(team, sla.team) &&
                Objects.equals(application, sla.application) &&
                Objects.equals(name, sla.name) &&
                Objects.equals(endPoint, sla.endPoint) &&
                Objects.equals(type, sla.type) &&
                Objects.equals(userType, sla.userType) &&
                Objects.equals(serverRespTime90Ms, sla.serverRespTime90Ms) &&
                Objects.equals(clientRespTimeMs, sla.clientRespTimeMs) &&
                Objects.equals(rpm, sla.rpm) &&
                Objects.equals(pageSizeKb, sla.pageSizeKb) &&
                Objects.equals(createdDt, sla.createdDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slaId, team, application, name, endPoint, type, userType, serverRespTime90Ms, clientRespTimeMs, rpm, pageSizeKb, createdDt);
    }
}
