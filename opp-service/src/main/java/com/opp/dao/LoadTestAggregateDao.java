package com.opp.dao;

import com.google.common.collect.Lists;
import com.opp.dao.util.SelectUtils;
import com.opp.domain.LoadTest;
import com.opp.domain.LoadTestAggregate;
import com.opp.domain.LoadTestAggregateView;
import com.opp.dto.LoadTestAggregateDataWithSlas;
import com.opp.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ctobe on 6/27/16.
 */
@Component
public class LoadTestAggregateDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedTemplate;

    @Value("${opp.db.importBatchSize}")
    private int importBatchSize;

    private static final String sqlIncreaseStringSize =  "SET group_concat_max_len = 1048576000;"; // important for doing percentile calcs

    /**
     * Deletes aggregates values from a load test
     * @param loadTestId
     * @return
     */
    public int deleteByLoadTestId(int loadTestId){
        return jdbcTemplate.update("delete from load_test_aggregate where load_test_id = ?", loadTestId);
    }

    public List<LoadTestAggregate> findByLoadTestId(int loadTestId){
        return SelectUtils.getOrReturnEmpty(() ->
                jdbcTemplate.query("select * from load_test_aggregate where load_test_id = ?", new BeanPropertyRowMapper<>(LoadTestAggregate.class), loadTestId)
        );
    }

    public List<LoadTestAggregateDataWithSlas> findByLoadTestIdWithSlas(int loadTestId){
        return SelectUtils.getOrReturnEmpty(() ->
            jdbcTemplate.query("select *\n" +
        "                        from load_test_with_aggregate t1\n" +
        "                        left join\n" +
        "                            (select\n" +
        "                                ls.custom_name as sla_custom_name,\n" +
        "                                    ls.custom_value as sla_custom_value,\n" +
        "                                    ls.load_sla_group_id,\n" +
        "                                    ls.margin_of_error as sla_margin_of_error,\n" +
        "                                    ls.max as sla_max,\n" +
        "                                    ls.median as sla_median,\n" +
        "                                    ls.min as sla_min,\n" +
        "                                    ls.name as sla_name,\n" +
        "                                    ls.pct75 as sla_pct75,\n" +
        "                                    ls.pct90 as sla_pct90,\n" +
        "                                    ls.avg as sla_avg\n" +
        "                            from\n" +
        "                                load_sla ls, load_sla_test_group lstg, load_sla_group lsg\n" +
        "                            where\n" +
        "                                lstg.load_sla_group_id = lsg.id\n" +
        "                                    and ls.load_sla_group_id = lsg.id\n" +
        "                                    and lstg.load_test_id = ? \n" +
        "                                    and lstg.is_active = true) t ON load_test_id = t1.load_test_id\n" +
        "                                and sla_name = t1.transaction_name\n" +
        "                        where\n" +
        "                            t1.load_test_id = ? \n" +
        "                        order by t1.transaction_name asc", new BeanPropertyRowMapper<>(LoadTestAggregateDataWithSlas.class), loadTestId, loadTestId)
        );
    }

    public List<LoadTestAggregateView> findByTrends(Set<String> trends, LoadTest loadTest) {

        List<String> sqlParams = new ArrayList<>();

        // todo: optimize this so i don't need to filter and then map and look up the property value twice
        String whereClause = StringUtils.join(trends.stream()
                .filter(trend -> ReflectionUtil.getPropertyValueFromObject(loadTest, trend, true).isPresent())
                .map(trend -> {
                    sqlParams.add(ReflectionUtil.getPropertyValueFromObject(loadTest, trend, true).get());
                    return trend + " = ?";
                }).collect(Collectors.toList()), " AND ");

        String SQL = "SELECT * from load_test_with_aggregate WHERE " + whereClause;


        return SelectUtils.getOrReturnEmpty(() ->
                jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(LoadTestAggregateView.class), sqlParams.toArray())
        );

    }

    public int createLoadTestAggregate(int loadTestId){

        String sql = "INSERT INTO load_test_aggregate (load_test_id, transaction_name, resp_min, resp_max, resp_avg, resp_median, resp_pct75, resp_pct90, resp_stddev, call_count, error_count, total_bytes_received, total_bytes_sent, tps_median, tps_max) \n" +
                "                select \n" +
                "load_test_id, t1.transaction_name, resp_min, \n" +
                "resp_max, resp_avg, resp_median, resp_pct75, \n" +
                "resp_pct90, resp_stddev, call_count, error_count, \n" +
                "total_bytes_received, total_bytes_sent, tps_median, tps_max \n" +
                "from \n" +
                "    (SELECT \n" +
                "        ? as load_test_id, \n" +
                "            transaction_name, \n" +
                "            min(response_time) as `resp_min`, \n" +
                "            max(response_time) as `resp_max`, \n" +
                "            round(avg(response_time), 0) as `resp_avg`, \n" +
                "            round(CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(GROUP_CONCAT(response_time \n" +
                "                    ORDER BY response_time \n" +
                "                    SEPARATOR ','), ',', 50 / 100 * COUNT(*) + 1), ',', - 1) \n" +
                "                AS DECIMAL), 0) AS `resp_median`, \n" +
                "            round(CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(GROUP_CONCAT(response_time \n" +
                "                    ORDER BY response_time \n" +
                "                    SEPARATOR ','), ',', 75 / 100 * COUNT(*) + 1), ',', - 1) \n" +
                "                AS DECIMAL), 0) AS `resp_pct75`, \n" +
                "            round(CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(GROUP_CONCAT(response_time \n" +
                "                    ORDER BY response_time \n" +
                "                    SEPARATOR ','), ',', 90 / 100 * COUNT(*) + 1), ',', - 1) \n" +
                "                AS DECIMAL), 0) AS `resp_pct90`, \n" +
                "            round(stddev(response_time), 2) as `resp_stddev`, \n" +
                "            count(*) as `call_count`, \n" +
                "            IFNULL(sum(error_count),0) as `error_count`, \n" +
                "            IFNULL(sum(bytes_received),0) as `total_bytes_received`, \n" +
                "            IFNULL(sum(bytes_sent),0) as `total_bytes_sent` \n" +
                "    FROM \n" +
                "        load_test_data \n" +
                "    WHERE \n" +
                "        load_test_id = ? \n" +
                "    GROUP BY transaction_name) t1 \n" +
                "    join \n" +
                "    (select \n" +
                "            round(CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(GROUP_CONCAT(requests \n" +
                "                    ORDER BY requests \n" +
                "                    SEPARATOR ','), ',', 50 / 100 * COUNT(*) + 1), ',', - 1) \n" +
                "                AS DECIMAL), 0) AS `tps_median`, \n" +
                "            max(requests) as tps_max, \n" +
                "transaction_name \n" +
                "    from \n" +
                "        (select \n" +
                "transaction_name, count(*)/60 as requests \n" +
                "from \n" +
                "load_test_data \n" +
                "where \n" +
                "load_test_id = ?  \n" +
                " \n" +
                "group by transaction_name , DATE_FORMAT(FROM_UNIXTIME(start_time), '%Y%m%d %H:%i') \n" +
                ") t \n" +
                "    group by transaction_name) t2 ON t1.transaction_name = t2.transaction_name;";


        // bump the allowed string size for percentile calculation
        // TODO: eventually i may hit a limit here calculating percentiles like this.  may want to refactor
        jdbcTemplate.update(sqlIncreaseStringSize);

        // insert aggregate rows
        return jdbcTemplate.update(sql, loadTestId, loadTestId, loadTestId);

    }


    public int add(int loadTestId, List<LoadTestAggregate> loadTestAggregateList) {
        AtomicInteger totalUpdateCount = new AtomicInteger(0);
        String sql = "INSERT INTO load_test_aggregate " +
                " ( " +
                    "call_count, load_test_id, resp_avg, resp_max, resp_median, resp_min, "+
                    "resp_pct75, resp_pct90, resp_stddev, total_bytes_received, total_bytes_sent, " +
                    "tps_median, tps_max, error_count, transaction_name, custom_name1, custom_name2, " +
                    "custom_name3, custom_value1, custom_value2, custom_value3" +
                ") VALUES " +
                " ( " +
                    ":callCount, "+loadTestId+", :respAvg, :respMax, :respMedian, :respMin, "+
                    ":respPct75, :respPct90, :respStddev, :totalBytesReceived, :totalBytesSent, " +
                    ":tpsMedian, :tpsMax, :errorCount, :transactionName, :customName1, :customName2, " +
                    ":customName3, :customValue1, :customValue2, :customValue3 " +
                " )";

        // iterate through batch
        Lists.partition(loadTestAggregateList, importBatchSize).forEach((listChunk) -> {
            SqlParameterSource params[] = listChunk.stream()
                    .map(BeanPropertySqlParameterSource::new)
                    .collect(Collectors.toList())
                    .toArray(new SqlParameterSource[0]);

            // execute batch and save updated rows to the total update count
            totalUpdateCount.getAndAdd(IntStream.of(namedTemplate.batchUpdate(sql, params)).sum());

        });
        return totalUpdateCount.get();
    }

    private static class PropertyInfo {
        private Field field;
        private String value;

        public PropertyInfo(Field field, String value) {
            this.field = field;
            this.value = value;
        }

        public Field getField() {
            return field;
        }

        public String getValue() {
            return value;
        }
    }

   /* public int insertAll(List<LoadTestAggregate> loadTestAggregateList) {

        final String sql = "INSERT INTO load_test_aggregate \n" +
                "(`call_count`,\n" + // 1
                "`load_test_id`,\n" + // 2
                "`resp_avg`,\n" + // 3
                "`resp_max`,\n" + // 4
                "`resp_median`,\n" + // 5
                "`resp_min`,\n" + // 6
                "`resp_pct75`,\n" + // 7
                "`resp_pct90`,\n" + // 8
                "`resp_stddev`,\n" + // 9
                "`total_bytes_received`,\n" + // 10
                "`total_bytes_sent`,\n" + // 11
                "`tps_median`,\n" + // 12
                "`tps_max`,\n" + // 13
                "`error_count`,\n" + // 14
                "`transaction_name`,\n" + // 15
                "`custom_name1`,\n" + // 16
                "`custom_name2`,\n" + // 17
                "`custom_name3`,\n" + // 18
                "`custom_value1`,\n" + // 19
                "`custom_value2`,\n" + // 20
                "`custom_value3`)\n" + // 21
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        final int batchSize = 500;
        List<List<LoadTestAggregate>> batchLists = Lists.partition(loadTestAggregateList, batchSize);
        int totalUpdated = 0;

        for(List<LoadTestAggregate> batch : batchLists) {
            totalUpdated += jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    LoadTestAggregate loadTestAggregate = batch.get(i);
                    ps.setString(1, String.valueOf(loadTestAggregate.getCallCount()));
                    ps.setString(2, String.valueOf(loadTestAggregate.getLoadTestId());
                    ps.setString(3, String.valueOf(loadTestAggregate.getRespAvg()));
                    ps.setString(4, String.valueOf(loadTestAggregate.getRespMax()));
                    ps.setString(5, String.valueOf(loadTestAggregate.getRespMedian()));
                    ps.setString(6, String.valueOf(loadTestAggregate.getRespMin()));
                    ps.setString(7, String.valueOf(loadTestAggregate.getRespPct75()));
                    ps.setString(8, String.valueOf(loadTestAggregate.getRespPct90()));
                    ps.setString(9, String.valueOf(loadTestAggregate.getRespStddev()));
                    ps.setString(10, String.valueOf(loadTestAggregate.getTotalBytesReceived()));
                    ps.setString(11, String.valueOf(loadTestAggregate.getTotalBytesSent()));
                    ps.setString(12, String.valueOf(loadTestAggregate.getTpsMedian()));
                    ps.setString(13, String.valueOf(loadTestAggregate.getTpsMax()));
                    ps.setString(14, String.valueOf(loadTestAggregate.getErrorCount()));
                    ps.setString(15, loadTestAggregate.getTransactionName());
                    ps.setString(16, loadTestAggregate.getCustomName1());
                    ps.setString(17, loadTestAggregate.getCustomName2());
                    ps.setString(18, loadTestAggregate.getCustomName3());
                    ps.setString(19, String.valueOf(loadTestAggregate.getCustomValue1()));
                    ps.setString(20, String.valueOf(loadTestAggregate.getCustomValue2()));
                    ps.setString(21, String.valueOf(loadTestAggregate.getCustomValue3()));
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
        return totalUpdated;
    }*/
}
