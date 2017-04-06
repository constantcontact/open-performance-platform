package com.opp.dao;

import com.opp.dao.util.UpdateBuilder;
import com.opp.domain.LoadSla;
import com.opp.dto.LoadTestSla;
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

/**
 * Created by ctobe on 8/8/16.
 */
@Component
public class LoadSlaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "load_sla";

    /**
     * Add new Load SLA
     * @param loadSla
     * @return
     */
    public int insert(LoadSla loadSla) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> insertInto(TABLE_NAME)
                        .value("load_sla_group_id", loadSla.getLoadSlaGroupId())
                        .value("margin_of_error", loadSla.getMarginOfError())
                        .value("max", loadSla.getMax())
                        .value("median", loadSla.getMedian())
                        .value("min", loadSla.getMin())
                        .value("name", loadSla.getName())
                        .value("pct75", loadSla.getPct75())
                        .value("pct90", loadSla.getPct90())
                        .value("avg", loadSla.getAvg())
                        .value("custom_name", loadSla.getCustomName())
                        .value("custom_value", loadSla.getCustomValue())
                        .build(conn, Statement.RETURN_GENERATED_KEYS),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }

    /**
     * Update or patch Load SLA
     * @param loadSlaId
     * @param loadSla
     */
    public void update(int loadSlaId, LoadSla loadSla) {
        jdbcTemplate.update(
                conn -> UpdateBuilder.updateInto(TABLE_NAME)
                        .value("load_sla_group_id", loadSla.getLoadSlaGroupId())
                        .value("margin_of_error", loadSla.getMarginOfError())
                        .value("max", loadSla.getMax())
                        .value("median", loadSla.getMedian())
                        .value("min", loadSla.getMin())
                        .value("name", loadSla.getName())
                        .value("pct75", loadSla.getPct75())
                        .value("pct90", loadSla.getPct90())
                        .value("avg", loadSla.getAvg())
                        .value("custom_name", loadSla.getCustomName())
                        .value("custom_value", loadSla.getCustomValue())
                        .whereColumnEqualsValue("id", loadSlaId)
                        .build(conn)
        );
    }

    /**
     * Get Sla by id
     * @param loadSlaId
     * @return
     */
    public Optional<LoadSla> findById(int loadSlaId){
        return getOptional(()->
                jdbcTemplate.queryForObject("select * from " + TABLE_NAME + " where id = ?", new BeanPropertyRowMapper<>(LoadSla.class), loadSlaId));
    }

    /**
     * Get Slas by loadTestId
     * @param loadTestId
     * @return
     */
    public List<LoadTestSla> findAllByLoadTestId(int loadTestId){
        return getOrReturnEmpty(()->
                jdbcTemplate.query("select\n" +
                        "                      lsg.id groupId, lsg.name groupName, ls.*, lstg.load_test_id\n" +
                        "                    from load_sla_group lsg, load_sla ls, load_sla_test_group lstg\n" +
                        "                    where lstg.load_test_id = ? \n" +
                        "                        and lsg.id = lstg.load_sla_group_id\n" +
                        "                        and lstg.is_active = true\n" +
                        "                        and ls.load_sla_group_id = lsg.id", new BeanPropertyRowMapper<>(LoadTestSla.class), loadTestId));
    }
    

    /**
     * Find all Load SLAs
     * @return
     */
    public List<LoadSla> findAll() {
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME,
                new BeanPropertyRowMapper<>(LoadSla.class)).stream().collect(toList()));
    }


    /**
     * Delete Load SLA by id
     * @param id
     * @return
     */
    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM "+TABLE_NAME+" WHERE id = ?", id);
    }
}
