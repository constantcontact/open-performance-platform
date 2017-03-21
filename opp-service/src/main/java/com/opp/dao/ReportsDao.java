package com.opp.dao;

import com.opp.dto.LoadTestRunHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.opp.dao.util.SelectUtils.getOrReturnEmpty;

/**
 * Created by ctobe on 8/18/16.
 */
@Component
public class ReportsDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<LoadTestRunHistory> findLoadTestRunsByDaysBack(int daysBack){
        String sql = "SELECT\n" +
                    "  FROM_UNIXTIME(MAX(start_time)) AS last_run,\n" +
                    "  COUNT(*) AS totalRuns,\n" +
                    "  test_name,\n" +
                    "  test_sub_name,\n" +
                    "  app_under_test,\n" +
                    "  environment,\n" +
                    "  IF(sla_group_id IS NULL,\n" +
                    "      'false',\n" +
                    "      'true') AS has_sla,\n" +
                    "  description,\n" +
                    "  comments\n" +
                    "    FROM\n" +
                    "  load_test lt\n" +
                    "    WHERE start_time > ? \n" +
                    "    GROUP BY app_under_test , test_name , test_sub_name\n" +
                    "    ORDER BY last_run";
        Long epochPast = LocalDateTime.now(ZoneOffset.UTC).minusDays(daysBack).toEpochSecond(ZoneOffset.UTC);

        return getOrReturnEmpty(() ->
                jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LoadTestRunHistory.class), epochPast)
        );

    }
}
