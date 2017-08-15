package com.opp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.opp.config.settings.SummaryTrendsConfiguration;
import com.opp.dao.LoadTestSummaryTrendDao;
import com.opp.domain.LoadTestSummaryTrend;
import com.opp.dto.LoadTestSummaryTrendGet;
import com.opp.dto.SummaryTrendByGroup;
import com.opp.exception.ResourceNotFoundException;
import com.opp.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by ctobe on 9/1/16.
 */
@Service
public class LoadTestTrendService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    LoadTestSummaryTrendDao dao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SummaryTrendsConfiguration summaryTrendsConfiguration;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String zeroTrend = "0 | 0%";

    /**
     * Creates a summary trend result by group where the criterion is an array of regexp filters against database columns
     * @param criterion
     * @return
     */
  //  @Cacheable(value="getSummaryTrendByGroupFilter", key = "#criterion")
    public List<SummaryTrendByGroup> getSummaryTrendByGroupFilter(Map<String, String> criterion){

        String[] ignoreKeys = {"_dc", "limit", "page", "start"}; // EXTJS and paging keys

        List<LoadTestSummaryTrendDao.AbstractGroupSummaryRow> groupSummaryRows = dao.findSummaryTrendsMatchingCriteria(criterion, ignoreKeys);

        List<SummaryTrendByGroup> summaryTrendByGroups = groupSummaryRows.stream().map(groupedTrend -> {
            SummaryTrendByGroup summaryTrendByGroup = new SummaryTrendByGroup(); // init return
            summaryTrendByGroup.setTestName(groupedTrend.getTestName());
            summaryTrendByGroup.setAppUnderTest(groupedTrend.getAppUnderTest());
            summaryTrendByGroup.setTestSubName(groupedTrend.getTestSubName());
            summaryTrendByGroup.setVuserCount(groupedTrend.getVuserCount());

            // grab the latest test run's summary trend
            String latestId = (groupedTrend.getLoadTestIds().split(","))[0];
            LoadTestSummaryTrend ltSummaryTrendLatest = dao.findByLoadTestId(Integer.parseInt(latestId)).orElseThrow(()->new ResourceNotFoundException("Load Test Id is missing.  This shouldn't happen as we just got the load test id from a prior query"));

            // set the latest test run
            summaryTrendByGroup.setLatestTest(ltSummaryTrendLatest);

            // get and set the summary trend by ids
            List<LoadTestSummaryTrendGet> loadTestSummaryTrendGets = getSummaryTrendByIds(groupedTrend.getLoadTestIds());
            summaryTrendByGroup.setSummaryTrend(loadTestSummaryTrendGets);

            // create sparklines for median and 90pct.  Also reversing list so its from oldest to newest so the sparkline is in chronological order
            summaryTrendByGroup.setSparkline50(loadTestSummaryTrendGets.stream().map(LoadTestSummaryTrendGet::getRespMedian).collect(toList()));
            summaryTrendByGroup.setSparkline90(loadTestSummaryTrendGets.stream().map(LoadTestSummaryTrendGet::getRespPct90).collect(toList()));

            return summaryTrendByGroup;

        }).collect(toList());

        return summaryTrendByGroups;
    }

    /**
     * Get sample summary trend data grouped by criterion.  This is used when generating a report and you want to see the sample data returned by your criterion
     * @param criterion
     */
  //  @Cacheable(value="getSummaryTrendByGroupSampleData", key = "#criterion")
    public List<SummaryTrendByGroup> getSummaryTrendByGroupSampleData(Map<String, String> criterion){

        String[] ignoreKeys = {"_dc", "limit", "page", "start"}; // EXTJS and paging keys

        List<LoadTestSummaryTrendDao.AbstractGroupSummaryRow> groupSummaryRows = dao.findSummaryTrendsMatchingCriteria(criterion, ignoreKeys);

        List<SummaryTrendByGroup> summaryTrendByGroups = groupSummaryRows.stream().map(groupedTrend -> {
            SummaryTrendByGroup summaryTrendByGroup = new SummaryTrendByGroup(); // init return
            summaryTrendByGroup.setTestName(groupedTrend.getTestName());
            summaryTrendByGroup.setAppUnderTest(groupedTrend.getAppUnderTest());
            summaryTrendByGroup.setTestSubName(groupedTrend.getTestSubName());
            summaryTrendByGroup.setVuserCount(groupedTrend.getVuserCount());

            // grab the latest test run's summary trend
            String latestId = (groupedTrend.getLoadTestIds().split(","))[0];
            LoadTestSummaryTrend ltSummaryTrendLatest = dao.findByLoadTestId(Integer.parseInt(latestId)).orElseThrow(()->new ResourceNotFoundException("Load Test Id is missing.  This shouldn't happen as we just got the load test id from a prior query"));

            // set the latest test run
            summaryTrendByGroup.setLatestTest(ltSummaryTrendLatest);

            return summaryTrendByGroup;

        }).collect(toList());

        return summaryTrendByGroups;
    }



    /**
     * Trend LoadTests by filter.  The filter will search the DB fields specified in the settings file (opp.summaryTrends.trendOnColumns)
     * By default, tests will trend on the setting: opp.summaryTrends.searchableColumns
     * @param filter
     * @return
     */
  //  @Cacheable(value="getSummaryTrendByFilter", key = "#filter")
    public List<LoadTestSummaryTrendGet> getSummaryTrendsByFilter(String filter){
        List<LoadTestSummaryTrend> loadTestSummaryTrends = dao.findSummaryTrendsByFilter(filter);
        // reverse list so its oldest to newest
        Collections.reverse(loadTestSummaryTrends);
        return trendLoadTests(loadTestSummaryTrends, summaryTrendsConfiguration.getTrendOnColumns());
    }




    /**
     * Trend on all or any comma delimited list of load test ids.
     * By default, tests will trend on the setting: opp.summaryTrends.searchableColumns
     * @param ids
     * @return
     */
   // @Cacheable(value="getSummaryTrendByIds", key = "#ids")
    public List<LoadTestSummaryTrendGet> getSummaryTrendByIds(String ids){
        int[] idList = Arrays.stream(ids.split(",")).filter(str -> StringUtils.isNumeric(str.trim())).mapToInt(str -> Integer.parseInt(str.trim())).toArray();
        return trendLoadTests(dao.findSummaryTrendsByLoadTestIds(idList), summaryTrendsConfiguration.getTrendOnColumns());
    }





    /**
     * Takes a List of LoadTestSummaryTrend and converts it to a LoadTestSummaryTrendGet which contains the trending fields for the metric values.
     * This function also calculates the trending on the metric values by looking at the last test that ran that matches the trendOn criteria.
     * Returns a list of LoadTestSummaryTrendGet in Newest to Oldest order
     * @param loadTestSummaryTrends - should be a list of LoadTestSummaryTrends from oldest to newest
     * @param trendOn - A comma delimited list of database columns from the load_test_summary_trend table
     * @return - reverses the order on the way out
     */
    public List<LoadTestSummaryTrendGet> trendLoadTests(List<LoadTestSummaryTrend> loadTestSummaryTrends, List<String> trendOn){

        // convert collection upfront to LoadTestSummaryTrendGet which contains the trend fields that LoadTestSummaryTrend doesn't have
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, LoadTestSummaryTrendGet.class);
        List<LoadTestSummaryTrendGet> loadTestSummaryTrendsGet = objectMapper.convertValue(loadTestSummaryTrends, listType);

        // map to keep track of the last LoadTestSummary that matches the trendOn criteria
        Map<String, LoadTestSummaryTrendGet> trendingMap = new HashMap<>();

        // create a new list to return
        List<LoadTestSummaryTrendGet> newLoadTestSummaryTrendGetList = new ArrayList<>();

        // loop through summary trends
        for(LoadTestSummaryTrendGet summaryTrend : loadTestSummaryTrendsGet){
            // get unique key based on trendOn data
            String keyName = getUniqueTrendingKey(summaryTrend, trendOn);
            // get the entry in the map that matches this key criteria
            LoadTestSummaryTrendGet prevSummaryTrend = trendingMap.get(keyName);
            // update summary trend object by adding trending field values
            summaryTrend = addTrending(prevSummaryTrend, summaryTrend);
            // save this getSummaryTrend to the trendingMap for later lookup in this loop
            trendingMap.put(keyName, summaryTrend);
            // add summary trend to return list
            newLoadTestSummaryTrendGetList.add(summaryTrend);
        }
        // reverse list so its newest to oldest
        Collections.reverse(newLoadTestSummaryTrendGetList);

        return newLoadTestSummaryTrendGetList;

    }

    /**
     * Adds trending to the LoadTestSummaryTrendGet by comparing two of them.
     * @param prev
     * @param cur
     * @return
     */
    private LoadTestSummaryTrendGet addTrending(LoadTestSummaryTrendGet prev, LoadTestSummaryTrendGet cur){
        if(prev == null) {
            // first iteration
            cur.setRespAvgTrend(zeroTrend);
            cur.setRespMedianTrend(zeroTrend);
            cur.setRespPct90Trend(zeroTrend);
            cur.setTpsMedianTrend(zeroTrend);
            cur.setTpsMaxTrend(zeroTrend);
            cur.setTotalBytesTrend(zeroTrend);
            cur.setTotalCallCountTrend(zeroTrend);
            cur.setErrorCountTrend(zeroTrend);
        } else {
            cur.setRespAvgTrend(calcIntTrend(prev.getRespAvg(), cur.getRespAvg()));
            cur.setRespMedianTrend(calcIntTrend(prev.getRespMedian(), cur.getRespMedian()));
            cur.setRespPct90Trend(calcIntTrend(prev.getRespPct90(), cur.getRespPct90()));
            cur.setTpsMedianTrend(calcDoubleTrend(prev.getTpsMedian(), cur.getTpsMedian()));
            cur.setTpsMaxTrend(calcDoubleTrend(prev.getTpsMax(), cur.getTpsMax()));
            cur.setTotalBytesTrend(calcLongTrend(prev.getTotalBytes(), cur.getTotalBytes()));
            cur.setTotalCallCountTrend(calcIntTrend(prev.getTotalCallCount(), cur.getTotalCallCount()));
            cur.setErrorCountTrend(calcIntTrend(prev.getErrorCount(), cur.getErrorCount()));
        }
        return cur;
    }

    private String getUniqueTrendingKey(LoadTestSummaryTrendGet loadTestSummaryTrendGet, List<String> trendOn) {
        String keyName = "";
        for(String trend : trendOn){
            keyName += ReflectionUtil.getPropertyValueFromObject(loadTestSummaryTrendGet, trend, true).orElse("");
        }
        return keyName;
    }

    private String calcIntTrend(int oldVal, int newVal){
        String trend = zeroTrend;
        if(oldVal != newVal) {
            int diff = newVal - oldVal;
            trend = (oldVal == 0) ? String.format("%d | 0%%", diff) : String.format("%d | %s%%", diff, diff / (double) oldVal * 100);
        }
        return trend;
    }

    private String calcDoubleTrend(double oldVal, double newVal){
        String trend = zeroTrend;
        if(oldVal != newVal) {
            double diff = newVal - oldVal;
            trend = (oldVal == 0) ? String.format("%s | 0%%", diff) : String.format("%s | %s%%", diff, diff/oldVal * 100);
        }
        return trend;
    }

    private String calcLongTrend(long oldVal, long newVal){
        String trend = zeroTrend;
        if(oldVal != newVal) {
            long diff = newVal - oldVal;
            trend = (oldVal == 0) ? String.format("%d | 0%%", diff) : String.format("%d | %s%%", diff, diff / (double) oldVal * 100);
        }
        return trend;
    }

}
