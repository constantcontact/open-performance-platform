package com.opp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * Created by ctobe on 6/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadTestData {
    private long id;
    private int loadTestId;
    private Integer bytesReceived;
    private Integer bytesSent;
    private Integer connectionTime;
    private Integer dnsLookup;
    private Integer errorCount;
    private String location;
    private String operation;
    private Integer receiveTime;
    private int responseTime;
    private Integer sendTime;
    private String server;
    private Long startTime;
    private String target;
    private String transactionName;
    private Integer ttfb;
    private Integer ttlb;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLoadTestId() {
        return loadTestId;
    }

    public void setLoadTestId(int loadTestId) {
        this.loadTestId = loadTestId;
    }

    public Integer getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(Integer bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public Integer getBytesSent() {
        return bytesSent;
    }

    public void setBytesSent(Integer bytesSent) {
        this.bytesSent = bytesSent;
    }

    public Integer getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(Integer connectionTime) {
        this.connectionTime = connectionTime;
    }

    public Integer getDnsLookup() {
        return dnsLookup;
    }

    public void setDnsLookup(Integer dnsLookup) {
        this.dnsLookup = dnsLookup;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Integer receiveTime) {
        this.receiveTime = receiveTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getSendTime() {
        return sendTime;
    }

    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Integer getTtfb() {
        return ttfb;
    }

    public void setTtfb(Integer ttfb) {
        this.ttfb = ttfb;
    }

    public Integer getTtlb() {
        return ttlb;
    }

    public void setTtlb(Integer ttlb) {
        this.ttlb = ttlb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadTestData that = (LoadTestData) o;
        return id == that.id &&
                responseTime == that.responseTime &&
                Objects.equals(bytesReceived, that.bytesReceived) &&
                Objects.equals(bytesSent, that.bytesSent) &&
                Objects.equals(connectionTime, that.connectionTime) &&
                Objects.equals(dnsLookup, that.dnsLookup) &&
                Objects.equals(errorCount, that.errorCount) &&
                Objects.equals(location, that.location) &&
                Objects.equals(operation, that.operation) &&
                Objects.equals(receiveTime, that.receiveTime) &&
                Objects.equals(sendTime, that.sendTime) &&
                Objects.equals(server, that.server) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(target, that.target) &&
                Objects.equals(transactionName, that.transactionName) &&
                Objects.equals(ttfb, that.ttfb) &&
                Objects.equals(ttlb, that.ttlb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bytesReceived, bytesSent, connectionTime, dnsLookup, errorCount, location, operation, receiveTime, responseTime, sendTime, server, startTime, target, transactionName, ttfb, ttlb);
    }
}
