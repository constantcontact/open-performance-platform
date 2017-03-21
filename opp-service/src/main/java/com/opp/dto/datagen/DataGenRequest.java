package com.opp.dto.datagen;

import java.util.Map;

/**
 * Created by ctobe on 7/13/16.
 */
public class DataGenRequest {
    private int numOfLoadTests;
    private int minTransactionsPerLoadTest;
    private int maxTransactionsPerLoadTest;
    private int minRawDataPerLoadTest;
    private int maxRawDataPerLoadTest;
    private boolean aggregateData;
    private String testNameSuffix;
    private Map<String, Object> loadTestObjOverrides;

    // todo add override field for load test

    public DataGenRequest() {
        // set defaults
        this.numOfLoadTests = 1;
        this.minTransactionsPerLoadTest = 3;
        this.maxTransactionsPerLoadTest = 3;
        this.minRawDataPerLoadTest = 40;
        this.maxRawDataPerLoadTest = 40;
        this.aggregateData = true;
        this.testNameSuffix = "";
    }

    public DataGenRequest(int numOfLoadTests, int minTransactionsPerLoadTest, int maxTransactionsPerLoadTest, int minRawDataPerLoadTest, int maxRawDataPerLoadTest, boolean aggregateData) {
        this.numOfLoadTests = numOfLoadTests;
        this.minTransactionsPerLoadTest = minTransactionsPerLoadTest;
        this.maxTransactionsPerLoadTest = maxTransactionsPerLoadTest;
        this.minRawDataPerLoadTest = minRawDataPerLoadTest;
        this.maxRawDataPerLoadTest = maxRawDataPerLoadTest;
        this.aggregateData = aggregateData;
    }
    public DataGenRequest(int numOfLoadTests, int minTransactionsPerLoadTest, int maxTransactionsPerLoadTest, int minRawDataPerLoadTest, int maxRawDataPerLoadTest, boolean aggregateData, String testNameSuffix) {
        this.numOfLoadTests = numOfLoadTests;
        this.minTransactionsPerLoadTest = minTransactionsPerLoadTest;
        this.maxTransactionsPerLoadTest = maxTransactionsPerLoadTest;
        this.minRawDataPerLoadTest = minRawDataPerLoadTest;
        this.maxRawDataPerLoadTest = maxRawDataPerLoadTest;
        this.aggregateData = aggregateData;
        this.testNameSuffix = testNameSuffix;
    }

    public Map<String, Object> getLoadTestObjOverrides() {
        return loadTestObjOverrides;
    }

    public void setLoadTestObjOverrides(Map<String, Object> loadTestObjOverrides) {
        this.loadTestObjOverrides = loadTestObjOverrides;
    }

    public int getNumOfLoadTests() {
        return numOfLoadTests;
    }

    public void setNumOfLoadTests(int numOfLoadTests) {
        this.numOfLoadTests = numOfLoadTests;
    }

    public int getMinTransactionsPerLoadTest() {
        return minTransactionsPerLoadTest;
    }

    public void setMinTransactionsPerLoadTest(int minTransactionsPerLoadTest) {
        this.minTransactionsPerLoadTest = minTransactionsPerLoadTest;
    }

    public int getMaxTransactionsPerLoadTest() {
        return maxTransactionsPerLoadTest;
    }

    public void setMaxTransactionsPerLoadTest(int maxTransactionsPerLoadTest) {
        this.maxTransactionsPerLoadTest = maxTransactionsPerLoadTest;
    }

    public int getMinRawDataPerLoadTest() {
        return minRawDataPerLoadTest;
    }

    public void setMinRawDataPerLoadTest(int minRawDataPerLoadTest) {
        this.minRawDataPerLoadTest = minRawDataPerLoadTest;
    }

    public int getMaxRawDataPerLoadTest() {
        return maxRawDataPerLoadTest;
    }

    public void setMaxRawDataPerLoadTest(int maxRawDataPerLoadTest) {
        this.maxRawDataPerLoadTest = maxRawDataPerLoadTest;
    }

    public boolean isAggregateData() {
        return aggregateData;
    }

    public void setAggregateData(boolean aggregateData) {
        this.aggregateData = aggregateData;
    }

    public String getTestNameSuffix() {
        return testNameSuffix;
    }

    public void setTestNameSuffix(String testNameSuffix) {
        this.testNameSuffix = testNameSuffix;
    }
}
