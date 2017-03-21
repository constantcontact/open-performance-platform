package com.opp.dto.maintenance;

import javax.validation.constraints.NotNull;

/**
 * Created by ctobe on 6/27/16.
 */
public class MaintDeleteTransactionReq {
    @NotNull
    String testName;
    @NotNull
    String transactionName;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }
}
