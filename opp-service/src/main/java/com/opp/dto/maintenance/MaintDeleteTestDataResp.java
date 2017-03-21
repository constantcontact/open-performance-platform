package com.opp.dto.maintenance;


/**
 * Created by ctobe on 6/27/16.
 */
public class MaintDeleteTestDataResp {
    int testsDeleted;

    public MaintDeleteTestDataResp(int testsDeleted) {
        this.testsDeleted = testsDeleted;
    }

    public int getTestsDeleted() {
        return testsDeleted;
    }

    public void setTestsDeleted(int testsDeleted) {
        this.testsDeleted = testsDeleted;
    }
}
