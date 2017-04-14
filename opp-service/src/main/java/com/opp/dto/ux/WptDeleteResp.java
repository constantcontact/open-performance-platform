package com.opp.dto.ux;

/**
 * Created by ctobe on 4/13/17.
 */
public class WptDeleteResp {
    int deleteCount;

    public WptDeleteResp(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }
}
