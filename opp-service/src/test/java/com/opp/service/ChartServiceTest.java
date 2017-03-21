package com.opp.service;

import com.opp.dao.LoadTestAggregateDao;
import com.opp.dao.LoadTestDao;
import com.opp.dao.LoadTestDataDao;
import com.opp.domain.ChartDetails;
import com.opp.domain.LoadTest;
import com.opp.domain.LoadTestAggregateView;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by jhermida on 9/8/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChartServiceTest {

    private ChartService chartService;

    @Mock
    private LoadTestDao loadTestDao;
    @Mock
    private LoadTestDataDao loadTestDataDao;
    @Mock
    private LoadTestAggregateDao loadTestAggregateDao;

    private ChartDetails chartDetails;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        chartService = new ChartService();

        Field loadTestDaoField = chartService.getClass().getDeclaredField("loadTestDao");
        loadTestDaoField.setAccessible(true);
        loadTestDaoField.set(chartService, loadTestDao);

        Field loadTestDataDaoField = chartService.getClass().getDeclaredField("loadTestDataDao");
        loadTestDataDaoField.setAccessible(true);
        loadTestDataDaoField.set(chartService, loadTestDataDao);

        Field loadTestAggregateDaoField = chartService.getClass().getDeclaredField("loadTestAggregateDao");
        loadTestAggregateDaoField.setAccessible(true);
        loadTestAggregateDaoField.set(chartService, loadTestAggregateDao);

        chartDetails = new ChartDetails();

    }


    @Test
    public void aggregateChartDataByLoadTestId_happyPath() {

        when(loadTestDao.findById(Mockito.anyInt())).thenReturn(Optional.of(new LoadTest()));
        when(loadTestAggregateDao.findByTrends(Mockito.any(HashSet.class), Mockito.any(LoadTest.class)))
                .thenReturn(new ArrayList<>());

        List<LoadTestAggregateView> loadTestAggregateViewList = chartService.aggregateChartDataByLoadTestId(chartDetails);

        assertTrue(CollectionUtils.isEmpty(loadTestAggregateViewList));
    }

    @Test
    public void aggregateChartDataByLoadTestId_emptyLoadTest() {

        when(loadTestDao.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        when(loadTestAggregateDao.findByTrends(Mockito.any(HashSet.class), Mockito.any(LoadTest.class)))
                .thenReturn(new ArrayList<>());

        List<LoadTestAggregateView> loadTestAggregateViewList = chartService.aggregateChartDataByLoadTestId(chartDetails);

        Mockito.verifyZeroInteractions(loadTestAggregateDao);
        assertTrue(CollectionUtils.isEmpty(loadTestAggregateViewList));
    }
}
