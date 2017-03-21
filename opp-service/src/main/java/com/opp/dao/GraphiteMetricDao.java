package com.opp.dao;

import com.opp.domain.GraphiteMetric;
import com.opp.dao.util.UpdateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static com.opp.dao.util.InsertBuilder.insertInto;
import static com.opp.dao.util.SelectUtils.getOptional;
import static com.opp.dao.util.SelectUtils.getOrReturnEmpty;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by ctobe on 9/13/16.
 */
@Component
public class GraphiteMetricDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "graphite_metric";


    /**
     * Get GraphiteMetric by id
     * @param graphiteMetricId
     * @return
     */
    public Optional<GraphiteMetric> findById(int graphiteMetricId){
        return getOptional(()->
                jdbcTemplate.queryForObject("select * from " + TABLE_NAME + " where graphite_id =  ?", new BeanPropertyRowMapper<>(GraphiteMetric.class), graphiteMetricId));
    }


    /**
     * Find all Graphite Metrics
     * @return
     */
    public List<GraphiteMetric> findAll() {
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME,
                new BeanPropertyRowMapper<>(GraphiteMetric.class)).stream().collect(toList()));
    }


    /**
     * Delete Graphite Metric by id
     * @param id
     * @return
     */
    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM "+TABLE_NAME+" WHERE graphite_id =  ?", id);
    }



    /**
     * Add new Graphite Metric
     * @param graphiteMetric
     * @return
     */
    public int insert(GraphiteMetric graphiteMetric) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> insertInto(TABLE_NAME)
                        .value("application_id", graphiteMetric.getApplicationId())
                        .value("app_key", graphiteMetric.getAppKey())
                        .value("name", graphiteMetric.getName())
                        .value("graphite_path", graphiteMetric.getGraphitePath())
                        .build(conn, Statement.RETURN_GENERATED_KEYS),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }

    /**
     * Update or patch Graphite Metric
     * @param graphiteMetricId
     * @param graphiteMetric
     */
    public void update(int graphiteMetricId, GraphiteMetric graphiteMetric) {
        jdbcTemplate.update(
                conn -> UpdateBuilder.updateInto(TABLE_NAME)
                        .value("application_id", graphiteMetric.getApplicationId())
                        .value("app_key", graphiteMetric.getAppKey())
                        .value("name", graphiteMetric.getName())
                        .value("graphite_path", graphiteMetric.getGraphitePath())
                        .whereColumnEqualsValue("graphite_id", graphiteMetricId)
                        .build(conn)
        );
    }



}
