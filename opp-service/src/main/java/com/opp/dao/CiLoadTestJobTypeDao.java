package com.opp.dao;

import com.opp.dao.util.UpdateBuilder;
import com.opp.domain.CiLoadTestJobType;
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
public class CiLoadTestJobTypeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "ci_load_test_job_type";
    private static final String SELECT_BY_ID = "select * from " +TABLE_NAME+ " where id = ?";
    private static final String SELECT_BY_JOB_TYPE = "select * from "+TABLE_NAME+" where job_type = ?";


    /**
     * Add newl oad test job map
     * @param ciLoadTestJobType
     * @return
     */
    public int insert(CiLoadTestJobType ciLoadTestJobType) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> insertInto(TABLE_NAME)
                        .value("job_type", ciLoadTestJobType.getJobType())
                        .value("additional_options", ciLoadTestJobType.getAdditionalOptions())
                        .value("test_tool", ciLoadTestJobType.getTestTool())
                        .value("test_tool_version", ciLoadTestJobType.getTestToolVersion())
                        .build(conn, Statement.RETURN_GENERATED_KEYS),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }

    /**
     * Update or patch load test job map
     * @param id
     * @param ciLoadTestJobType
     */
    public void update(int id, CiLoadTestJobType ciLoadTestJobType) {
        jdbcTemplate.update(
                conn -> UpdateBuilder.updateInto(TABLE_NAME)
                        .value("job_type", ciLoadTestJobType.getJobType())
                        .value("additional_options", ciLoadTestJobType.getAdditionalOptions())
                        .value("test_tool", ciLoadTestJobType.getTestTool())
                        .value("test_tool_version", ciLoadTestJobType.getTestToolVersion())
                        .whereColumnEqualsValue("id", id)
                        .build(conn)
        );
    }

    /**
     * Get load test job by id
     * @paramload test jobMapId
     * @return
     */
    public Optional<CiLoadTestJobType> findById(int id){
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(CiLoadTestJobType.class),id));
        } catch(DataAccessException ex){
            return Optional.empty();
        }
    }

    /**
     * Get all load test jobs by test name
     * @param testName
     * @return
     */
    public Optional<CiLoadTestJobType> findByJobType(String jobType) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_JOB_TYPE, new BeanPropertyRowMapper<>(CiLoadTestJobType.class),jobType));
        } catch(DataAccessException ex){
            return Optional.empty();
        }
    }

    /**
     * Find all load test jobs
     * @return
     */
    public List<CiLoadTestJobType> findAll() {
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME,
                new BeanPropertyRowMapper<>(CiLoadTestJobType.class)).stream().collect(toList()));
    }

    /**
     * Delete load test job by id
     * @param id
     */
    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM "+ TABLE_NAME+" WHERE id = ?", id);
    }


}
