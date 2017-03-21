package com.opp.service;

import com.opp.BaseIntegrationTest;
import com.opp.dto.datagen.DataGenRequest;
import com.opp.dto.datagen.DataGenResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ctobe on 7/14/16.
 */
public class DataGenTest extends BaseIntegrationTest {

    @Autowired
    private DataGenService dataGenService;

    @Test
    public void generateDataExact() throws Exception {
        int loadTests = 3;
        int minRawData = 11;
        int maxRawData = 11;
        int minTrans = 2;
        int maxTrans = 2;
        boolean aggregate = true;

        validate(loadTests, minTrans, maxTrans, minRawData, maxRawData, aggregate);

    }

    @Test
    public void generateDataRanges() throws Exception {
        int loadTests = 3;
        int minRawData = 10;
        int maxRawData = 30;
        int minTrans = 5;
        int maxTrans = 10;
        boolean aggregate = false;

        validate(loadTests, minTrans, maxTrans, minRawData, maxRawData, aggregate);

    }

    private void validate(int loadTests, int minTrans, int maxTrans, int minRawData, int maxRawData, boolean aggregate){
        DataGenRequest dataGenRequest = new DataGenRequest(loadTests, minTrans, maxTrans, minRawData, maxRawData, aggregate);
        List<DataGenResponse> dataGenResponseList = dataGenService.generateData(dataGenRequest);

        assertEquals("Load Test Count", loadTests, dataGenResponseList.size());
        dataGenResponseList.forEach(d -> {
            assertTrue("Transactions per Test", (d.getTransactionBreakDown().size() >= minTrans && d.getTransactionBreakDown().size() <= maxTrans));
            assertTrue("Raw Data per Test", (d.getDataCount() >= minRawData && d.getDataCount() <= maxRawData));
            if(aggregate) {
                assertTrue("Data aggregates are set", d.getAggregates().getTransactionsAggregated() == d.getTransactionBreakDown().size());
            } else {
                assertNull("Data aggregates is null", d.getAggregates());
            }
        });
    }

}