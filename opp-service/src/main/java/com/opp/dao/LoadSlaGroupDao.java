package com.opp.dao;

import com.opp.domain.LoadSlaGroup;
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

/**
 * Created by ctobe on 8/8/16.
 */
@Component
public class LoadSlaGroupDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "load_sla_group";

    /**
     * Add new Load SLA Group
     * @param loadSlaGroup
     * @return
     */
    public int insert(LoadSlaGroup loadSlaGroup) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> insertInto(TABLE_NAME)
                        .value("name", loadSlaGroup.getName())
                        .build(conn, Statement.RETURN_GENERATED_KEYS),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }

    /**
     * Update or patch Load SLA Group
     * @param loadSlaGroupId
     * @param loadSlaGroup
     */
    public void update(int loadSlaGroupId, LoadSlaGroup loadSlaGroup) {
        jdbcTemplate.update(
                conn -> UpdateBuilder.updateInto(TABLE_NAME)
                        .value("name", loadSlaGroup.getName())
                        .whereColumnEqualsValue("id", loadSlaGroupId)
                        .build(conn)
        );
    }

    /**
     * Get application by id
     * @param loadSlaGroupId
     * @return
     */
    public Optional<LoadSlaGroup> findById(int loadSlaGroupId){
        return getOptional(()->
                jdbcTemplate.queryForObject("select * from " + TABLE_NAME + " where id = ?", new BeanPropertyRowMapper<>(LoadSlaGroup.class), loadSlaGroupId));
    }
    

    /**
     * Find all Load SLA Groups
     * @return
     */
    public List<LoadSlaGroup> findAll() {
        return getOrReturnEmpty(() -> jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME,
                new BeanPropertyRowMapper<>(LoadSlaGroup.class)).stream().collect(toList()));
    }

    /**
     * Delete Load SLA Group by id
     * @param id
     */
    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM "+TABLE_NAME+" WHERE id = ?", id);
    }
}
