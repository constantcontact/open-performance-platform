package com.opp.dao;

import com.opp.config.settings.SummaryTrendsConfiguration;
import com.opp.domain.LoadTestSummaryTrend;
import com.opp.util.ReflectionUtil;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.opp.dao.util.SelectUtils.getOptional;
import static com.opp.dao.util.SelectUtils.getOrReturnEmpty;
import static java.util.stream.Collectors.joining;

/**
 * Created by ctobe on 6/27/16.
 */
@Component
public class LoadTestSummaryTrendDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String TABLE_NAME = "load_test_summary_trend";

    @Autowired
    private SummaryTrendsConfiguration summaryTrendsConfiguration;




    /**
     * Get LoadTestSummaryTrend by loadTestId
     * @param loadTestId
     * @return
     */
    public Optional<LoadTestSummaryTrend> findByLoadTestId(int loadTestId){
        return getOptional(()->
                jdbcTemplate.queryForObject("select * from " + TABLE_NAME + " where load_test_id = ?", new BeanPropertyRowMapper<>(LoadTestSummaryTrend.class), loadTestId));
    }

    public List<AbstractGroupSummaryRow> findSummaryTrendsMatchingCriteria(Map<String, String> criteria, String[] ignoreCriteria){

        // build where clause and store off sqlParams
        List<Object> sqlParams = new ArrayList<>();
        LoadTestSummaryTrend loadTestSummaryTrend = new LoadTestSummaryTrend(); // used for verifying properties exist

        // build where clause and join together with AND
        String whereClause = criteria.entrySet().stream()
                // only allow if key is not in the ignoreKeys list and the property exists on the LoadTestSummaryTrend object
                .filter(entry -> !Arrays.asList(ignoreCriteria).contains(entry.getKey()) && ReflectionUtil.hasProperty(loadTestSummaryTrend, entry.getKey()))
                .map((entry) -> {
                    sqlParams.add(entry.getValue()); // append sql param
                    // format where clause and convert camelCase to lower underscore
                    return String.format("%s regexp ?", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey()));
                }).collect(joining(" AND "));

        // set limit to 100 if its not already set
        sqlParams.add((criteria.containsKey("limit") ? Integer.parseInt(criteria.get("limit")) : 100));

        // get all load tests by criteria and group by name, sub name, app, and vuser count
        String sql = "SELECT lt.test_name, lt.test_sub_name, lt.app_under_test, lt.vuser_count, group_concat(lt.load_test_id order by lt.start_time desc) as load_test_ids \n" +
                "  FROM load_test_summary_trend lt\n" +
                "  WHERE " + whereClause + "\n" +
                "  group by lt.test_name, lt.test_sub_name, lt.app_under_test, lt.vuser_count\n" +
                "  order by lt.test_name asc\n" +
                "  limit ?";

        return getOrReturnEmpty(()-> jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AbstractGroupSummaryRow.class), sqlParams.toArray()));

    }

    public List<LoadTestSummaryTrend> findSummaryTrendsByFilter(String filter){
        // build where clause
        String whereFilterClause = "";
        List<String> sqlParams = new ArrayList<>();

        // build where clause if filter is present
        if(!StringUtils.isEmpty(filter)) {
            // split WHERE criterion into OR clauses and the filter value to sqlParams for each time through the loop
            whereFilterClause = summaryTrendsConfiguration.getSearchableColumns().stream().map(s -> {
                sqlParams.add("%"+filter+"%");
                return s + " like ?";
            }).collect(joining(" OR "));
        }

        String whereClause = "";
        if(!whereFilterClause.isEmpty()) {
            whereClause = " WHERE ( " + whereFilterClause + " ) ";
        }

        String sql = "SELECT * FROM load_test_summary_trend " + whereClause + " order by load_test_id ASC LIMIT 0,100";

        return getOrReturnEmpty(()-> jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LoadTestSummaryTrend.class), sqlParams.toArray()));

    }

    public List<LoadTestSummaryTrend> findSummaryTrendsByLoadTestIds(int[] ids){
        String sql = "SELECT * FROM load_test_summary_trend WHERE load_test_id in ( :ids ) order by load_test_id ASC LIMIT 0,100";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", IntStream.of(ids).boxed().collect(Collectors.toSet())); // convert to set (need this to make it work)
        return getOrReturnEmpty(()-> namedParameterJdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(LoadTestSummaryTrend.class)));
    }


    public static class AbstractGroupSummaryRow {
        private String testName;
        private String testSubName;
        private String appUnderTest;
        private int vuserCount;
        private String loadTestIds;

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        public String getTestSubName() {
            return testSubName;
        }

        public void setTestSubName(String testSubName) {
            this.testSubName = testSubName;
        }

        public String getAppUnderTest() {
            return appUnderTest;
        }

        public void setAppUnderTest(String appUnderTest) {
            this.appUnderTest = appUnderTest;
        }

        public int getVuserCount() {
            return vuserCount;
        }

        public void setVuserCount(int vuserCount) {
            this.vuserCount = vuserCount;
        }

        public String getLoadTestIds() {
            return loadTestIds;
        }

        public void setLoadTestIds(String loadTestIds) {
            this.loadTestIds = loadTestIds;
        }
    }



}
