package com.opp.dto.datagen;

import com.opp.domain.LoadTest;
import com.opp.domain.LoadTestData;
import com.opp.dto.aggregate.LoadTestAggregateDataResp;

import java.util.List;
import java.util.Map;

/**
 * Created by ctobe on 7/13/16.
 */
public class DataGenResponse {
    int loadTestId;
    LoadTest loadTest;
    List<LoadTestData> loadTestData;
    int dataCount;
    Map<String, Long> transactionBreakDown;
    LoadTestAggregateDataResp aggregates;

    public LoadTestAggregateDataResp getAggregates() {
        return aggregates;
    }

    public void setAggregates(LoadTestAggregateDataResp aggregates) {
        this.aggregates = aggregates;
    }

    public int getLoadTestId() {
        return loadTestId;
    }

    public void setLoadTestId(int loadTestId) {
        this.loadTestId = loadTestId;
    }

    public LoadTest getLoadTest() {
        return loadTest;
    }

    public void setLoadTest(LoadTest loadTest) {
        this.loadTest = loadTest;
    }

    public List<LoadTestData> getLoadTestData() {
        return loadTestData;
    }

    public void setLoadTestData(List<LoadTestData> loadTestData) {
        this.loadTestData = loadTestData;
    }

    public int getDataCount() {
        return dataCount;
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public Map<String, Long> getTransactionBreakDown() {
        return transactionBreakDown;
    }

    public void setTransactionBreakDown(Map<String, Long> transactionBreakDown) {
        this.transactionBreakDown = transactionBreakDown;
    }
}
