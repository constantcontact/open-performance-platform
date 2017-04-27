package com.opp.dao;

import com.opp.dao.util.UpdateBuilder;
import com.opp.domain.CiLoadTestJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static com.opp.dao.util.InsertBuilder.insertInto;
import static com.opp.dao.util.SelectUtils.getOrReturnEmpty;
import static java.util.stream.Collectors.toList;

/**
 * Created by ctobe on 4/26/17.
 */
@Component
public class CiLoadTestJobDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String TABLE_NAME = "ci_load_test_job";
    private static final String SELECT_BY_ID = "select * from " +TABLE_NAME+ " where id = ?";
    private static final String SELECT_BY_TEST_NAME = "select * from "+TABLE_NAME+" where test_name = ?";


    /**
     * Add newl oad test job map
     * @param ciLoadTestJob
     * @return
     */
    public int insert(CiLoadTestJob ciLoadTestJob) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> insertInto(TABLE_NAME)
                        .value("app_under_test", ciLoadTestJob.getAppUnderTest())
                        .value("app_under_test_version", ciLoadTestJob.getAppUnderTestVersion())
                        .value("comments", ciLoadTestJob.getComments())
                        .value("cron_schedule", ciLoadTestJob.getCronSchedule())
                        .value("ct_additional_options", ciLoadTestJob.getCtAdditionalOptions())
                        .value("description", ciLoadTestJob.getDescription())
                        .value("environment", ciLoadTestJob.getEnvironment())
                        .value("host_name", ciLoadTestJob.getHostName())
                        .value("ramp_vuser_end_delay", ciLoadTestJob.getRampVuserEndDelay())
                        .value("ramp_vuser_start_delay", ciLoadTestJob.getRampVuserStartDelay())
                        .value("run_duration", ciLoadTestJob.getRunDuration())
                        .value("sla_group_id", ciLoadTestJob.getSlaGroupId())
                        .value("test_path", ciLoadTestJob.getTestPath())
                        .value("test_type", ciLoadTestJob.getTestType())
                        .value("test_name", ciLoadTestJob.getTestName())
                        .value("test_sub_name", ciLoadTestJob.getTestSubName())
                        .value("test_data_type", ciLoadTestJob.getTestDataType())
                        .value("test_tool", ciLoadTestJob.getTestTool())
                        .value("test_tool_version", ciLoadTestJob.getTestToolVersion())
                        .value("vuser_count", ciLoadTestJob.getVuserCount())
                        .build(conn, Statement.RETURN_GENERATED_KEYS),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }

    /**
     * Update or patch load test job map
     * @param id
     * @param ciLoadTestJob
     */
    public void update(int id, CiLoadTestJob ciLoadTestJob) {
        jdbcTemplate.update(
                conn -> UpdateBuilder.updateInto(TABLE_NAME)
                        .value("app_under_test", ciLoadTestJob.getAppUnderTest())
                        .value("app_under_test_version", ciLoadTestJob.getAppUnderTestVersion())
                        .value("comments", ciLoadTestJob.getComments())
                        .value("cron_schedule", ciLoadTestJob.getCronSchedule())
                        .value("ct_additional_options", ciLoadTestJob.getCtAdditionalOptions())
                        .value("description", ciLoadTestJob.getDescription())
                        .value("environment", ciLoadTestJob.getEnvironment())
                        .value("host_name", ciLoadTestJob.getHostName())
                        .value("ramp_vuser_end_delay", ciLoadTestJob.getRampVuserEndDelay())
                        .value("ramp_vuser_start_delay", ciLoadTestJob.getRampVuserStartDelay())
                        .value("run_duration", ciLoadTestJob.getRunDuration())
                        .value("sla_group_id", ciLoadTestJob.getSlaGroupId())
                        .value("test_path", ciLoadTestJob.getTestPath())
                        .value("test_type", ciLoadTestJob.getTestType())
                        .value("test_name", ciLoadTestJob.getTestName())
                        .value("test_sub_name", ciLoadTestJob.getTestSubName())
                        .value("test_data_type", ciLoadTestJob.getTestDataType())
                        .value("test_tool", ciLoadTestJob.getTestTool())
                        .value("test_tool_version", ciLoadTestJob.getTestToolVersion())
                        .value("vuser_count", ciLoadTestJob.getVuserCount())
                        .whereColumnEqualsValue("id", id)
                        .build(conn)
        );
    }

    /**
     * Get load test job by id
     * @paramload test jobMapId
     * @return
     */
    public Optional<CiLoadTestJob> findById(int id){
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(CiLoadTestJob.class),id));
        } catch(DataAccessException ex){
            return Optional.empty();
        }
    }

    /**
     * Get all load test jobs by test name
     * @param testName
     * @return
     */
    public Optional<CiLoadTestJob> findByTestName(String testName) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_TEST_NAME, new BeanPropertyRowMapper<>(CiLoadTestJob.class),testName));
        } catch(DataAccessException ex){
            return Optional.empty();
        }
    }

    /**
     * Find all load test jobs
     * @return
     */
    public List<CiLoadTestJob> findAll() {
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME,
                new BeanPropertyRowMapper<>(CiLoadTestJob.class)).stream().collect(toList()));
    }

    /**
     * Delete load test job by id
     * @param id
     */
    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM "+ TABLE_NAME+" WHERE id = ?", id);
    }

    public List<CiLoadTestJob> search(CiLoadTestJob ciLoadTestJob) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE";
        if(!ciLoadTestJob.getTestName().isEmpty()){
            query += " test_name = :testName " ;
            params.addValue("testName", ciLoadTestJob.getTestName() );
        }
        if(!ciLoadTestJob.getTestName().isEmpty()){
            if((params.getValues().size() > 0)) query += " and";
            query += " test_type = :testType " ;
            params.addValue("testType", ciLoadTestJob.getTestType() );
        }

        // remove where from query if there are no params
        String finalQuery = (params.getValues().size() == 0) ? query.replace(" WHERE", "") : query;

        return getOrReturnEmpty(() ->
                namedParameterJdbcTemplate.query(finalQuery, params, new BeanPropertyRowMapper<>(CiLoadTestJob.class)).stream().collect(toList()));
    }
}
