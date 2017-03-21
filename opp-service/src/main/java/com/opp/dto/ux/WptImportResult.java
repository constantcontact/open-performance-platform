package com.opp.dto.ux;

import com.opp.dto.ux.couchdb.CouchDbActionResp;

/**
 * Created by ctobe on 10/26/16.
 */
public class WptImportResult {
    CouchDbActionResp summaryResults;
    CouchDbActionResp rawResults;

    public WptImportResult(CouchDbActionResp summaryResults, CouchDbActionResp rawResults) {
        this.summaryResults = summaryResults;
        this.rawResults = rawResults;
    }

    public CouchDbActionResp getSummaryResults() {
        return summaryResults;
    }

    public void setSummaryResults(CouchDbActionResp summaryResults) {
        this.summaryResults = summaryResults;
    }

    public CouchDbActionResp getRawResults() {
        return rawResults;
    }

    public void setRawResults(CouchDbActionResp rawResults) {
        this.rawResults = rawResults;
    }
}
