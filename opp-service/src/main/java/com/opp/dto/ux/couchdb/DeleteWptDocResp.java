package com.opp.dto.ux.couchdb;

/**
 * Created by ctobe on 9/26/16.
 */
public class DeleteWptDocResp {
    private boolean files;
    private boolean rawData;
    private boolean summaryData;

    private CouchDbActionResp summaryResp;
    private CouchDbActionResp rawDataResp;

    public DeleteWptDocResp() {
        this.files = false;
        this.rawData = false;
        this.summaryData = false;
    }

    public DeleteWptDocResp(boolean files, boolean rawData, boolean summaryData) {
        this.files = files;
        this.rawData = rawData;
        this.summaryData = summaryData;
    }

    public boolean isFiles() {
        return files;
    }

    public void setFiles(boolean files) {
        this.files = files;
    }

    public boolean isRawData() {
        return rawData;
    }

    public void setRawData(boolean rawData) {
        this.rawData = rawData;
    }

    public boolean isSummaryData() {
        return summaryData;
    }

    public void setSummaryData(boolean summaryData) {
        this.summaryData = summaryData;
    }

    public CouchDbActionResp getSummaryResp() {
        return summaryResp;
    }

    public void setSummaryResp(CouchDbActionResp summaryResp) {
        this.summaryResp = summaryResp;
    }

    public CouchDbActionResp getRawDataResp() {
        return rawDataResp;
    }

    public void setRawDataResp(CouchDbActionResp rawDataResp) {
        this.rawDataResp = rawDataResp;
    }
}
