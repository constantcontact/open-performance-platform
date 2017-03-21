package com.opp.dao;


import com.opp.domain.LoadSlaTestGroup;
import com.opp.dao.util.InsertBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Statement;

@Component
public class LoadSlaTestGroupDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TABLE_NAME = "load_sla_test_group";

    public int insert(LoadSlaTestGroup loadSlaTestGroup) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> InsertBuilder.insertInto(TABLE_NAME)
                        .value("is_active", loadSlaTestGroup.getIsActive())
                        .value("load_sla_group_id", loadSlaTestGroup.getLoadSlaGroupId())
                        .value("load_test_id", loadSlaTestGroup.getLoadTestId())
                        .build(conn, Statement.RETURN_GENERATED_KEYS),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }

}
