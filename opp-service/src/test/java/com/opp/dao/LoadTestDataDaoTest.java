package com.opp.dao;

import com.opp.domain.ChartDetails;
import com.opp.domain.LoadTest;
import com.opp.domain.LoadTestTimeChartData;
import com.opp.exception.BadRequestException;
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
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by jhermida on 9/8/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoadTestDataDaoTest {
    private LoadTestDataDao loadTestDataDao;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private ChartDetails chartDetails;
    private LoadTest loadTest;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        loadTestDataDao  = new LoadTestDataDao();
        Field field = loadTestDataDao.getClass().getDeclaredField("jdbcTemplate");
        field.setAccessible(true);
        field.set(loadTestDataDao, jdbcTemplate);

        chartDetails = new ChartDetails();
        loadTest = new LoadTest();
        loadTest.setId(1);
    }

    @Test
    public void getLineTimeSeries_respPct() {

        chartDetails.setyAxis("resp_pct90");

        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class))).thenReturn(new ArrayList<>());

        List<LoadTestTimeChartData> timeChartDataList = loadTestDataDao.getLineTimeSeries(chartDetails, loadTest);

        assertTrue(CollectionUtils.isEmpty(timeChartDataList));
    }

    @Test
    public void getLineTimeSeries_successfulSwitch() {

        chartDetails.setyAxis("resp_min");

        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class))).thenReturn(new ArrayList<>());

        List<LoadTestTimeChartData> timeChartDataList = loadTestDataDao.getLineTimeSeries(chartDetails, loadTest);

        assertTrue(CollectionUtils.isEmpty(timeChartDataList));
    }

    @Test(expected = BadRequestException.class)
    public void getLineTimeSeries_invalidYAxis() {

        chartDetails.setyAxis("crap");

        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class))).thenReturn(new ArrayList<>());

        loadTestDataDao.getLineTimeSeries(chartDetails, loadTest);
    }
}
