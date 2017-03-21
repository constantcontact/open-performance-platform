package com.opp.dao;

import com.opp.domain.LoadTest;
import com.opp.domain.LoadTestAggregateView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by jhermida on 9/1/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoadTestAggregateDaoTest {

    private LoadTestAggregateDao loadTestAggregateDao;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        loadTestAggregateDao = new LoadTestAggregateDao();
        Field field = loadTestAggregateDao.getClass().getDeclaredField("jdbcTemplate");
        field.setAccessible(true);
        field.set(loadTestAggregateDao, jdbcTemplate);
    }

    @Test
    public void findByTrends_happyPath() {
        Set<String> trends = new HashSet<>();

        trends.add("vuser_count");
        trends.add("test_sub_name");

        LoadTest loadTest = new LoadTest();
        loadTest.setVuserCount(20);
        loadTest.setTestSubName("Register single event");

        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyVararg()))
                .thenReturn(new ArrayList<>());

        List<LoadTestAggregateView> loadTestAggregates = loadTestAggregateDao.findByTrends(trends, loadTest);

        assertTrue(CollectionUtils.isEmpty(loadTestAggregates));
    }

}
