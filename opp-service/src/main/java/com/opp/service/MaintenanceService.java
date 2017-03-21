package com.opp.service;

import com.opp.dao.LoadTestDao;
import com.opp.dao.MaintenanceDao;
import com.opp.dto.aggregate.LoadTestAggregateDataResp;
import com.opp.dto.maintenance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ctobe on 6/27/16.
 */
@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceDao dao;

    @Autowired
    private LoadTestDao loadTestDao;

    @Autowired
    LoadTestAggregateService loadTestAggregateService;

    /**
     * Deletes all transaction data inside all load tests matching the load test name and transaction name
     * @param request
     * @return
     */
    public MaintDeleteTransactionResp deleteTransactions(MaintDeleteTransactionReq request){
        return dao.deleteTransactions(request.getTestName(), request.getTransactionName());
    }

    /**
     * Re-aggregates all tests that have raw data
     * @return
     */
    // TODO: it looks like we are re-aggregating everything.  We should check first to make sure the raw data still exists
    public MaintReaggregateDataResp reAggregateData() {
        // get all load test ids
        int[] loadTestIds = loadTestDao.findAllLoadTestIds();
        AtomicInteger totalAggregated = new AtomicInteger(0);  // using atomic int so i can increment inside lambda
        List<LoadTestAggregateDataResp> loadTestAggregateDataRespList = new ArrayList<>();

        // iterate over load tests and
        Arrays.stream(loadTestIds).forEach(ltId -> {
            // aggregate data
            LoadTestAggregateDataResp loadTestAggregateDataResp = loadTestAggregateService.aggregateLoadTestData(ltId);
            loadTestAggregateDataRespList.add(loadTestAggregateDataResp); // insertAll to list
            if(loadTestAggregateDataResp.getTransactionsAggregated() > 0) totalAggregated.getAndAdd(1); // increment if transactions were aggregated
        });

        return new MaintReaggregateDataResp(loadTestIds.length, totalAggregated.get(), loadTestAggregateDataRespList);
    }

    /**
     * Deletes API logs
     * @param daysToKeep
     * @return
     */
    public MaintDeleteApiLogsResp deleteApiLogs(int daysToKeep) {
        return new MaintDeleteApiLogsResp(dao.deleteApiLogs(daysToKeep));
    }

    public MaintDeleteTestDataResp deleteTestData() {
        return new MaintDeleteTestDataResp(dao.deleteTestData());
    }
}
