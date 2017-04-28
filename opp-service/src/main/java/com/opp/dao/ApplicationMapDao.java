package com.opp.dao;

import com.opp.dao.util.UpdateBuilder;
import com.opp.domain.ApplicationMap;
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
 * Created by ctobe on 6/13/16.
 */
@Component
public class ApplicationMapDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_BY_ID = "select * from application where id = ?";
    private static final String SELECT_BY_APP_KEY = "select * from application where app_key = ?";


    /**
     * Add new application map
     * @param applicationMap
     * @return
     */
    public int insert(ApplicationMap applicationMap) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> insertInto("application")
                        .value("app_key", applicationMap.getAppKey())
                        .value("newrelic", applicationMap.getNewrelic())
                        .value("appdynamics", applicationMap.getAppdynamics())
                        .value("webpagetest", applicationMap.getWebpagetest())
                        .value("splunk", applicationMap.getSplunk())
                        .value("dynatrace", applicationMap.getDynatrace())
                        .value("is_client_side", applicationMap.getIsClientSide())
                        .value("is_server_side", applicationMap.getIsServerSide())
                        .value("in_cd_pipeline_client", applicationMap.isInCdPipelineClient())
                        .value("in_cd_pipeline_server", applicationMap.isInCdPipelineServer())
                        .value("code_coverage_id", applicationMap.getCodeCoverageId())
                        .value("security_id", applicationMap.getSecurityId())
                        .value("regression_results_id", applicationMap.getRegressionResultsId())
                        .value("kqi_app_name", applicationMap.getKqiAppName())
                        .value("code_coverage_id", applicationMap.getCodeCoverageId())
                        .value("team_name", applicationMap.getTeamName())
                        .build(conn, Statement.RETURN_GENERATED_KEYS),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }

    /**
     * Update or patch application map
     * @param applicationMapId
     * @param applicationMap
     */
    public void update(int applicationMapId, ApplicationMap applicationMap) {
        jdbcTemplate.update(
                conn -> UpdateBuilder.updateInto("application")
                        .value("app_key", applicationMap.getAppKey())
                        .value("newrelic", applicationMap.getNewrelic())
                        .value("appdynamics", applicationMap.getAppdynamics())
                        .value("webpagetest", applicationMap.getWebpagetest())
                        .value("splunk", applicationMap.getSplunk())
                        .value("dynatrace", applicationMap.getDynatrace())
                        .value("is_client_side", applicationMap.getIsClientSide())
                        .value("is_server_side", applicationMap.getIsServerSide())
                        .value("in_cd_pipeline_client", applicationMap.isInCdPipelineClient())
                        .value("in_cd_pipeline_server", applicationMap.isInCdPipelineServer())
                        .value("code_coverage_id", applicationMap.getCodeCoverageId())
                        .value("security_id", applicationMap.getSecurityId())
                        .value("regression_results_id", applicationMap.getRegressionResultsId())
                        .value("kqi_app_name", applicationMap.getKqiAppName())
                        .value("code_coverage_id", applicationMap.getCodeCoverageId())
                        .value("team_name", applicationMap.getTeamName())
                        .whereColumnEqualsValue("id", applicationMapId)
                        .build(conn)
        );
    }

    /**
     * Get application by id
     * @param applicationMapId
     * @return
     */
    public Optional<ApplicationMap> findById(int applicationMapId){
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(ApplicationMap.class), applicationMapId));
        } catch(DataAccessException ex){
            return Optional.empty();
        }
    }

    /**
     * Get all applications by app key
     * @param applicationKey
     * @return
     */
    public List<ApplicationMap> findAllByName(String applicationKey) {
        return getOrReturnEmpty(() ->
             jdbcTemplate.query(SELECT_BY_APP_KEY, new BeanPropertyRowMapper<>(ApplicationMap.class), applicationKey)
        );
    }

    /**
     * Find all application maps
     * @return
     */
    public List<ApplicationMap> findAll() {
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM application",
                new BeanPropertyRowMapper<>(ApplicationMap.class)).stream().collect(toList()));
    }

    /**
     * Delete application map by id
     * @param id
     */
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM application WHERE id = ?", id);
    }
}
