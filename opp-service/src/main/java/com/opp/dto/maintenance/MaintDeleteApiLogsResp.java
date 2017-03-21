package com.opp.dto.maintenance;


/**
 * Created by ctobe on 6/27/16.
 */
public class MaintDeleteApiLogsResp {
    int deletedEntries;

    public MaintDeleteApiLogsResp(int deletedEntries) {
        this.deletedEntries = deletedEntries;
    }

    public int getDeletedEntries() {
        return deletedEntries;
    }

    public void setDeletedEntries(int deletedEntries) {
        this.deletedEntries = deletedEntries;
    }
}
