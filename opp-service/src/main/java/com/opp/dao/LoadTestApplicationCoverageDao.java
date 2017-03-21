package com.opp.dao;

import com.opp.domain.LoadTestApplicationCoverage;
import com.opp.dto.LoadTestApplicationCoverageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static com.opp.dao.util.InsertBuilder.insertInto;
import static com.opp.dao.util.SelectUtils.getOrReturnEmpty;
import static java.util.stream.Collectors.toList;

/**
 * Created by ctobe on 8/8/16.
 */
@Component
public class LoadTestApplicationCoverageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "load_test_application_coverage";

    /**
     * Add new Load Test Application Coverage
     * @param loadTestApplicationCoverage
     * @return
     */
    public int insert(LoadTestApplicationCoverage loadTestApplicationCoverage) {
        int affectedRows = jdbcTemplate.update(
                conn -> insertInto(TABLE_NAME)
                        .value("application_id", loadTestApplicationCoverage.getApplicationId())
                        .value("load_test_name", loadTestApplicationCoverage.getLoadTestName())
                        .build(conn, Statement.NO_GENERATED_KEYS)
        );
        return affectedRows;
    }

    /**
     * Get LoadTestApplicationCoverageDto by loadTestName
     * @param loadTestName
     * @return
     */
    public List<LoadTestApplicationCoverage> findByLoadTestName(String loadTestName){
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME + " where load_test_name = ?",
                new BeanPropertyRowMapper<>(LoadTestApplicationCoverage.class), loadTestName).stream().collect(toList()));
    }

    /**
     * Get LoadTestApplicationCoverageDto by application id
     * @param applicationId
     * @return
     */
    public List<LoadTestApplicationCoverage> findByApplicationId(int applicationId){
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME + " where application_id = ?",
                new BeanPropertyRowMapper<>(LoadTestApplicationCoverage.class), applicationId).stream().collect(toList()));
    }

    /**
     * Get LoadTestApplicationCoverageDto by loadTestName and applicationId
     * @param loadTestName
     * @return
     */
    public List<LoadTestApplicationCoverage> findByLoadTestNameAndApplicationId(String loadTestName, int applicationId){
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME + " where load_test_name = ? AND application_id = ?",
                new BeanPropertyRowMapper<>(LoadTestApplicationCoverage.class), loadTestName, applicationId).stream().collect(toList()));
    }

    /**
     * Find by filter
     * @param filter
     * @return
     */
    public List<LoadTestApplicationCoverage> findByFilter(LoadTestApplicationCoverageFilter filter) {
        if(filter.getApplicationId().isPresent()){
            if(filter.getLoadTestName().isPresent()){
                // delete by both
                return findByLoadTestNameAndApplicationId(filter.getLoadTestName().get(), filter.getApplicationId().get());
            } else {
                // delete by app id
                return findByApplicationId(filter.getApplicationId().get());
            }
        }

        if(filter.getLoadTestName().isPresent()){
            // delete by loadTestId
            return findByLoadTestName(filter.getLoadTestName().get());
        }

        return Collections.emptyList() ; // if i'm here, neither are present, will not run any query

    }

    /**
     * Find all Load Test Application Coverages
     * @return
     */
    public List<LoadTestApplicationCoverage> findAll() {
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME,
                new BeanPropertyRowMapper<>(LoadTestApplicationCoverage.class)).stream().collect(toList()));
    }


    /**
     * Delete Load Test Application Coverage by Load Test Name
     * @param loadTestName
     * @return
     */
    public int deleteByLoadTestName(String loadTestName) {
        return jdbcTemplate.update("DELETE FROM "+TABLE_NAME+" WHERE load_test_name = ?", loadTestName);
    }

    /**
     * Delete Load Test Application Coverage by application id
     * @param applicationId
     * @return
     */
    public int deleteByApplicationId(int applicationId) {
        return jdbcTemplate.update("DELETE FROM "+TABLE_NAME+" WHERE application_id = ?", applicationId);
    }

    /**
     * Delete Load Test Application Coverage by load test name and application id
     * @param loadTestName
     * @param applicationId
     * @return
     */
    public int deleteByLoadTestNameAndApplicationId(String loadTestName, int applicationId) {
        return jdbcTemplate.update("DELETE FROM "+TABLE_NAME+" WHERE load_test_name = ? and application_id = ?", loadTestName, applicationId);
    }

    /**
     * Delete by filter
     * @param filter
     * @return
     */
    public int deleteByFilter(LoadTestApplicationCoverageFilter filter) {
        if(filter.getApplicationId().isPresent()){
            if(filter.getLoadTestName().isPresent()){
                // delete by both
                return deleteByLoadTestNameAndApplicationId(filter.getLoadTestName().get(), filter.getApplicationId().get());
            } else {
                // delete by app id
                return deleteByApplicationId(filter.getApplicationId().get());
            }
        }

        if(filter.getLoadTestName().isPresent()){
            // delete by loadTestId
            return deleteByLoadTestName(filter.getLoadTestName().get());
        }

        return 0; // if i'm here, neither are present, will not run any query

    }
}
