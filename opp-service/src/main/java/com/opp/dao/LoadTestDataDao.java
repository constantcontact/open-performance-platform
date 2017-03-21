package com.opp.dao;

import com.opp.domain.ChartDetails;
import com.opp.domain.LoadTest;
import com.opp.domain.LoadTestData;
import com.opp.domain.LoadTestTimeChartData;
import com.opp.exception.BadRequestException;
import com.google.common.collect.Lists;
import com.opp.dao.util.InsertBuilder;
import com.opp.dao.util.SelectUtils;
import com.opp.dao.util.UpdateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ctobe on 7/14/16.
 */
@Component
public class LoadTestDataDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedTemplate;

    @Value("${opp.db.importBatchSize}")
    private int importBatchSize;

    private static final String TABLE_NAME = "load_test_data";


    /**
     * Get LoadTestData by Id
     * @param loadTestId
     * @param dataId
     * @return
     */
    public Optional<LoadTestData> findById(int loadTestId, long dataId){
        return SelectUtils.getOptional(()->
                jdbcTemplate.queryForObject("select * from " + TABLE_NAME + " where id = ? and load_test_id = ?", new BeanPropertyRowMapper<>(LoadTestData.class), dataId, loadTestId));
    }

    /**
     * Find all by loadTestId
     * @param loadTestId
     * @return
     */
    public List<LoadTestData> findAllByLoadTestId(int loadTestId){
        return SelectUtils.getOrReturnEmpty(()->
                jdbcTemplate.query("select * from "+ TABLE_NAME +" where load_test_id = ?", new BeanPropertyRowMapper<>(LoadTestData.class), loadTestId)
        );
    }


    /**
     * Delete by loadTestDataId
     * Forcing loadTestId for extra security here so people don't accidentally delete other peoples data
     * @param loadTestId
     * @param dataId
     * @return
     */
    public int delete(int loadTestId, long dataId) {
        return jdbcTemplate.update("DELETE FROM "+TABLE_NAME+" WHERE id = ? and load_test_id = ?", dataId, loadTestId);
    }

    /**
     * Delete Load Test by id
     * @param loadTestId
     * @return
     */
    public int deleteAllByLoadTestId(int loadTestId) {
        return jdbcTemplate.update("DELETE FROM "+TABLE_NAME+" WHERE load_test_id = ?", loadTestId);
    }


    private String getTimeSeriesPercentileSql(int pct, String respName){
        return "round(CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(GROUP_CONCAT(response_time " +
                "ORDER BY response_time SEPARATOR ','), ',', " + pct + " / 100 * COUNT(*) + 1), ',', - 1) " +
                "AS DECIMAL), 0) AS `" + respName + "`";
    }

    /**
     * Get Line Time Series Data
     * @param chartDetails
     * @param loadTest
     * @return
     */
    public List<LoadTestTimeChartData> getLineTimeSeries(ChartDetails chartDetails, LoadTest loadTest) {

        String yAxisValue;

        if(chartDetails.getyAxis().contains("resp_pct")) {
            // extract the percentile.
            String[] values = chartDetails.getyAxis().split("resp_pct");
            int percentile = Integer.parseInt(values[1]);

            yAxisValue = getTimeSeriesPercentileSql(percentile, chartDetails.getyAxis());

        } else {

            switch (chartDetails.getyAxis()) {
                case "resp_min":
                    yAxisValue = "min(response_time) as `resp_min`";
                    break;
                case "resp_max":
                    yAxisValue = "max(response_time) as `resp_max`";
                    break;
                case "resp_avg":
                    yAxisValue = "round(avg(response_time), 0) as `resp_avg`";
                    break;
                case "resp_median":
                    yAxisValue = getTimeSeriesPercentileSql(50, chartDetails.getyAxis());
                    break;
                case "resp_stddev":
                    yAxisValue = "round(stddev(response_time), 2) as `resp_stddev`";
                    break;
                case "call_count":
                    yAxisValue = "count(*) as `call_count`";
                    break;
                case "error_count":
                    yAxisValue = "sum(error_count) as `error_count`";
                    break;
                case "total_bytes_received":
                    yAxisValue = "sum(bytes_received) as `total_bytes_received`";
                    break;
                case "total_bytes_sent":
                    yAxisValue = "sum(bytes_sent) as `total_bytes_sent`";
                    break;
                case "total_bytes":
                    yAxisValue = "(sum(bytes_sent) + sum(bytes_received)) as `total_bytes`";
                    break;
                default:
                    throw new BadRequestException("Invalid yAxis");
            }
        }

        String SQL = "SELECT " + loadTest.getId() + " as load_test_id," +
                " start_time," +
                " transaction_name," +
                yAxisValue +
                " FROM " + TABLE_NAME +
                " WHERE load_test_id = " + loadTest.getId() +
                " GROUP BY transaction_name , DATE_FORMAT(FROM_UNIXTIME(start_time), '%Y%m%d %H:%i')";

        return SelectUtils.getOrReturnEmpty(() ->
                jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(LoadTestTimeChartData.class))
        );
    }

    /**
     * Update or patch Load Test Data.
     * Notes: Cannot change load_test_id
     * @param loadTestId
     * @param loadTestData
     */
    public void update(int loadTestId, long dataId, LoadTestData loadTestData) {
        jdbcTemplate.update(
                conn -> UpdateBuilder.updateInto(TABLE_NAME)
                        .value("transaction_name", loadTestData.getTransactionName())
                        .value("response_time", loadTestData.getResponseTime())
                        .value("bytes_received", loadTestData.getBytesReceived())
                        .value("bytes_sent", loadTestData.getBytesSent())
                        .value("connection_time", loadTestData.getConnectionTime())
                        .value("dns_lookup", loadTestData.getDnsLookup())
                        .value("error_count", loadTestData.getErrorCount())
                        .value("location", loadTestData.getLocation())
                        .value("operation", loadTestData.getOperation())
                        .value("receive_time", loadTestData.getReceiveTime())
                        .value("send_time", loadTestData.getSendTime())
                        .value("server", loadTestData.getServer())
                        .value("start_time", loadTestData.getStartTime())
                        .value("target", loadTestData.getTarget())
                        .value("ttfb", loadTestData.getTtfb())
                        .value("ttlb", loadTestData.getTtlb())
                        .whereColumnEqualsValue("id", dataId)
                        .whereColumnEqualsValue("load_test_id", loadTestId)
                        .build(conn)
        );
    }


    /**
     * Add new Load Test Data
     * @param loadTestData
     * @return
     */
    public int insert(int loadTestId, LoadTestData loadTestData) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> InsertBuilder.insertInto(TABLE_NAME)
                        .value("load_test_id", loadTestId)
                        .value("transaction_name", loadTestData.getTransactionName())
                        .value("response_time", loadTestData.getResponseTime())
                        .value("bytes_received", loadTestData.getBytesReceived())
                        .value("bytes_sent", loadTestData.getBytesSent())
                        .value("connection_time", loadTestData.getConnectionTime())
                        .value("dns_lookup", loadTestData.getDnsLookup())
                        .value("error_count", loadTestData.getErrorCount())
                        .value("location", loadTestData.getLocation())
                        .value("operation", loadTestData.getOperation())
                        .value("receive_time", loadTestData.getReceiveTime())
                        .value("send_time", loadTestData.getSendTime())
                        .value("server", loadTestData.getServer())
                        .value("start_time", loadTestData.getStartTime())
                        .value("target", loadTestData.getTarget())
                        .value("ttfb", loadTestData.getTtfb())
                        .value("ttlb", loadTestData.getTtlb())
                        .build(conn, Statement.RETURN_GENERATED_KEYS),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }


    /**
     * Bulk insert LoadTestData
     * @param loadTestId
     * @param loadTestDataList
     * @return
     */
    public int insertAll(int loadTestId, List<LoadTestData> loadTestDataList) {
        AtomicInteger totalUpdateCount = new AtomicInteger(0);
        String sql =    "INSERT INTO "+ TABLE_NAME +"\n" +
                        "(" +
                            "`load_test_id`, `transaction_name`, `response_time`,\n" +
                            "`bytes_received`, `bytes_sent`, `connection_time`, `dns_lookup`,\n" +
                            "`error_count`, `location`, `operation`, `receive_time`,\n" +
                            "`send_time`, `server`, `start_time`, `target`, `ttfb`, `ttlb`" +
                        ")" +
                        "VALUES " +
                        "(" +
                            loadTestId+", :transactionName, :responseTime,\n" +
                            ":bytesReceived, :bytesSent, :connectionTime, :dnsLookup,\n" +
                            ":errorCount, :location, :operation,\n :receiveTime,\n" +
                            ":sendTime, :server, :startTime, :target, :ttfb, :ttlb" +
                        ")";

        // iterate through batch
        Lists.partition(loadTestDataList, importBatchSize).forEach((listChunk) -> {
            SqlParameterSource params[] = listChunk.stream()
                    .map(BeanPropertySqlParameterSource::new)
                    .collect(Collectors.toList())
                    .toArray(new SqlParameterSource[0]);

            // execute batch and save updated rows to the total update count
            totalUpdateCount.getAndAdd(IntStream.of(namedTemplate.batchUpdate(sql, params)).sum());

        });
        return totalUpdateCount.get();
    }


}
