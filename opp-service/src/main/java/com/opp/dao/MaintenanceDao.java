package com.opp.dao;

import com.opp.dto.maintenance.MaintDeleteTransactionResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by ctobe on 6/13/16.
 */
@Component
public class MaintenanceDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Deletes all transaction data inside all load tests matching the load test name and transaction name
     * @param testName
     * @param transactionName
     * @return
     */
    public MaintDeleteTransactionResp deleteTransactions(String testName, String transactionName) {
        String sqlAggDataDelete = "delete from load_test_aggregate\n" +
                "     where load_test_id in (\n" +
                "           SELECT id FROM load_test where test_name = ?\n" +
                "         ) and transaction_name = ?";

        // Weird... I know.  need to do it using a join because MySQL optimizes this wrong when using a subquery and it does a full table scan on load_test_data
        String sqlDataDelete = "delete ltd from load_test_data ltd \n" +
                "join load_test lt on ltd.load_test_id = lt.id \n" +
                "where lt.test_name = ? \n" +
                "and ltd.transaction_name = ?";

        MaintDeleteTransactionResp resp = new MaintDeleteTransactionResp();
        resp.setAggregateDataRowsRemoved(jdbcTemplate.update(sqlAggDataDelete, new Object[] { testName, transactionName }));
        resp.setRawDataRowsRemoved(jdbcTemplate.update(sqlDataDelete, new Object[] { testName, transactionName }));
        return resp;
    }

    /**
     * Deletes API logs older than the specified number of days to keep
     * @param daysToKeep
     * @return
     */
    public int deleteApiLogs(int daysToKeep) {
        return jdbcTemplate.update("delete from api_logs where `time` < UNIX_TIMESTAMP(DATE_ADD(CURDATE(),INTERVAL ? DAY))", (daysToKeep * -1));
    }

    public int deleteTestData() {
        return jdbcTemplate.update("delete from load_test where app_under_test = 'Fake App' or test_name like 'Generated Data Test%'");
    }
}
