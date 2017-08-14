package com.opp.dao;

import com.opp.dao.util.UpdateBuilder;
import com.opp.domain.CiLoadTestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class CiLoadTestTypeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "ci_load_test_type";
    private static final String SELECT_BY_ID = "select * from " +TABLE_NAME+ " where id = ?";
    private static final String SELECT_BY_JOB_TYPE = "select * from "+TABLE_NAME+" where test_type = ?";


    /**
     * Add new
     * @param ciLoadTestType
     * @return
     */
    public int insert(CiLoadTestType ciLoadTestType) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> insertInto(TABLE_NAME)
                        .value("test_type", ciLoadTestType.getTestType())
                        .value("additional_options", ciLoadTestType.getAdditionalOptions())
                        .value("test_tool", ciLoadTestType.getTestTool())
                        .value("test_tool_version", ciLoadTestType.getTestToolVersion())
                        .build(conn, Statement.RETURN_GENERATED_KEYS),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }

    /**
     * Update or patch
     * @param id
     * @param ciLoadTestType
     */
    public void update(int id, CiLoadTestType ciLoadTestType) {
        jdbcTemplate.update(
                conn -> UpdateBuilder.updateInto(TABLE_NAME)
                        .value("test_type", ciLoadTestType.getTestType())
                        .value("additional_options", ciLoadTestType.getAdditionalOptions())
                        .value("test_tool", ciLoadTestType.getTestTool())
                        .value("test_tool_version", ciLoadTestType.getTestToolVersion())
                        .whereColumnEqualsValue("id", id)
                        .build(conn)
        );
    }

    /**
     * Get by id
     * @paramload test jobMapId
     * @return
     */
    public Optional<CiLoadTestType> findById(int id){
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(CiLoadTestType.class),id));
        } catch(DataAccessException ex){
            return Optional.empty();
        }
    }

    /**
     * Get all by test type
     * @param testType
     * @return
     */
    public Optional<CiLoadTestType> findByTestType(String testType) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_JOB_TYPE, new BeanPropertyRowMapper<>(CiLoadTestType.class),testType));
        } catch(DataAccessException ex){
            return Optional.empty();
        }
    }

    /**
     * Find all
     * @return
     */
    public List<CiLoadTestType> findAll() {
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME,
                new BeanPropertyRowMapper<>(CiLoadTestType.class)).stream().collect(toList()));
    }

    /**
     * Delete by id
     * @param id
     */
    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM "+ TABLE_NAME+" WHERE id = ?", id);
    }


}
