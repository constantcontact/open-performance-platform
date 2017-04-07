package com.opp.dao;

import com.google.common.base.CaseFormat;
import com.opp.domain.LoadTest;
import com.opp.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.opp.dao.util.InsertBuilder.insertInto;
import static com.opp.dao.util.SelectUtils.getOptional;
import static com.opp.dao.util.SelectUtils.getOrReturnEmpty;
import static com.opp.dao.util.UpdateBuilder.updateInto;
import static java.util.stream.Collectors.*;

/**
 * Created by ctobe on 6/27/16.
 */
@Component
public class LoadTestDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "load_test";


    /**
     * Get LoadTest by id
     * @param loadTestId
     * @return
     */
    public Optional<LoadTest> findById(int loadTestId){
        return getOptional(()->
                jdbcTemplate.queryForObject("select * from " + TABLE_NAME + " where id = ?", new BeanPropertyRowMapper<>(LoadTest.class), loadTestId));
    }


    /**
     * Find all Load Tests
     * @return
     * @param criterion
     */
    public List<LoadTest> findAll(Map<String, String> criterion) {
        if(criterion.size() == 0) {
            // just get all
            return getOrReturnEmpty(() -> jdbcTemplate.query(
                    "SELECT * FROM " + TABLE_NAME,
                    new BeanPropertyRowMapper<>(LoadTest.class)).stream().collect(toList()));
        } else {
            LoadTest loadTest = new LoadTest(); // used to verify class exists
            List<Object> sqlParams = new ArrayList<>(); // build where clause and store off sqlParams

            // build where clause and join together with AND
            String whereClause = criterion.entrySet().stream()
                    // only allow if key is not in the ignoreKeys list and the property exists on the LoadTestSummaryTrend object
                    .filter(entry -> ReflectionUtil.hasProperty(loadTest, entry.getKey()))
                    .map((entry) -> {
                        sqlParams.add(entry.getValue()); // append sql param
                        // format where clause and convert camelCase to lower underscore
                        return String.format("%s regexp ?", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey()));
                    }).collect(joining(" AND "));

            // set limit to 100 if its not already set
           // sqlParams.add((criterion.containsKey("limit") ? Integer.parseInt(criterion.get("limit")) : 100));

            // get all load tests by criteria and group by name, sub name, app, and vuser count
            String sql = "SELECT * from load_test where " + whereClause; // + " limit ?";

            return getOrReturnEmpty(()-> jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LoadTest.class), sqlParams.toArray()));

        }
    }


    /**
     * Delete Load Test by id
     * @param id
     * @return
     */
    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM "+TABLE_NAME+" WHERE id = ?", id);
    }



    /**
     * Add new Load Test
     * @param loadTest
     * @return
     */
    public int insert(LoadTest loadTest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> insertInto(TABLE_NAME)
                        .value("app_under_test", loadTest.getAppUnderTest())
                        .value("app_under_test_version", loadTest.getAppUnderTestVersion())
                        .value("comments", loadTest.getComments())
                        .value("description", loadTest.getDescription())
                        .value("start_time", loadTest.getStartTime())
                        .value("end_time", loadTest.getEndTime())
                        .value("environment", loadTest.getEnvironment())
                        .value("test_name", loadTest.getTestName())
                        .value("test_sub_name", loadTest.getTestSubName())
                        .value("test_tool", loadTest.getTestTool())
                        .value("test_tool_version", loadTest.getTestToolVersion())
                        .value("vuser_count", loadTest.getVuserCount())
                        .value("sla_group_id", loadTest.getSlaGroupId())
                        .value("external_test_id", loadTest.getExternalTestId())
                        .build(conn, Statement.RETURN_GENERATED_KEYS),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }

    /**
     * Update or patch Load Test
     * @param loadTestId
     * @param loadTest
     */
    public void update(int loadTestId, LoadTest loadTest) {
        jdbcTemplate.update(
                conn -> updateInto(TABLE_NAME)
                        .value("app_under_test", loadTest.getAppUnderTest())
                        .value("app_under_test_version", loadTest.getAppUnderTestVersion())
                        .value("comments", loadTest.getComments())
                        .value("description", loadTest.getDescription())
                        .value("start_time", loadTest.getStartTime())
                        .value("end_time", loadTest.getEndTime())
                        .value("environment", loadTest.getEnvironment())
                        .value("test_name", loadTest.getTestName())
                        .value("test_sub_name", loadTest.getTestSubName())
                        .value("test_tool", loadTest.getTestTool())
                        .value("test_tool_version", loadTest.getTestToolVersion())
                        .value("vuser_count", loadTest.getVuserCount())
                        .value("sla_group_id", loadTest.getSlaGroupId())
                        .value("external_test_id", loadTest.getExternalTestId())
                        .whereColumnEqualsValue("id", loadTestId)
                        .build(conn)
        );
    }

    /**
     * Used to ensure all load-test times have a correct start and end time.  The start time could be an epoc time with millis and by default there is no end time.
     * @param loadTestId
     * @return
     */
    public Map<String, Long> deriveLoadTestStartEndTimes(int loadTestId) {
        String sql = "select\n" +
                "    min(start_time) as 'start_time',\n" +
                "    (select round(((start_time*1000) + response_time)/1000) from load_test_data where load_test_id = ? order by ((start_time*1000) + response_time) desc limit 1) as end_time\n" +
                "    from load_test_data\n" +
                "    where load_test_id = ?";
        return jdbcTemplate.queryForMap(sql, new Object[]{loadTestId, loadTestId}).entrySet().stream().collect(toMap(e -> e.getKey(), e -> Long.valueOf(e.getValue().toString())));
    }

    /**
     * Updates the start and time of a load test
     * @param loadTestId
     * @param startTime
     * @param endTime
     * @return
     */
    public int updateStartEndTime(int loadTestId, long startTime, long endTime){
        String sql = "update load_test set start_time = ?, end_time = ? where id = ?";
        return jdbcTemplate.update(sql, new Object[] {startTime, endTime, loadTestId});
    }

    /**
     * Get all load test ids
     * @return
     */
    public int[] findAllLoadTestIds(){
        return jdbcTemplate.queryForList("select id from load_test", int.class).stream().mapToInt(Integer::intValue).toArray();
    }


}
