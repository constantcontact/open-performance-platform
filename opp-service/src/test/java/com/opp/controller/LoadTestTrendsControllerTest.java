package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.dto.datagen.DataGenRequest;
import com.opp.dto.datagen.DataGenResponse;
import com.opp.service.DataGenService;
import com.opp.service.LoadTestAggregateService;
import com.google.common.util.concurrent.AtomicDouble;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by ctobe on 9/7/16.
 */
public class LoadTestTrendsControllerTest extends BaseIntegrationTest {
    @Autowired
    private DataGenService dataGenService;

    @Autowired
    private LoadTestAggregateService loadTestAggregateService;

    @Test
    public void getSummaryGroupTrendTest() throws Exception {
        String testName = "Generated Integration Test Summary Group Trend";
        int testsToCreate = 5;

        // cleanup first
        jdbcTemplate.update("DELETE from load_test where test_name = ?", testName);

        // create data
        List<DataGenResponse> dataGenResponses = createData(testName, testsToCreate);

        // get and verify resp
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("testName", "Generated Integration Test Summary Group Trend");
        HttpResponse<JsonNode> resp = getSummaryTrendGroup(queryParams, testsToCreate);

        // verify sparklines
        verifySparkline(resp, "sparkline90", "respPct90");
        verifySparkline(resp, "sparkline50", "respMedian");

    }



    @Test
    public void getSummaryTrendTest() throws Exception {

        String testName = "Generated Integration Test Summary Trend";
        int testsToCreate = 5;

        // cleanup first
        jdbcTemplate.update("DELETE from load_test where test_name = ?", testName);

        // create data
        List<DataGenResponse> dataGenResponses = createData(testName, testsToCreate);

        // get all test ids
        String idList = dataGenResponses.stream().map(r -> String.valueOf(r.getLoadTest().getId())).collect(joining(","));

        // trend data by filter and validate
        HttpResponse<JsonNode> resp = getSummaryTrendByFilter(testName, testsToCreate);

        // validate getting trend data back and initial trend is 0
        validateBasicTrends(resp.getBody().getArray(), testsToCreate); // by filter
        resp = getSummaryTrendById(idList, testsToCreate);
        validateBasicTrends(resp.getBody().getArray(), testsToCreate); // by id

        // test decreasing trend  -> decrease by 33%
        double trendExpected = 33.0;
        setCustomTrending(dataGenResponses, trendExpected);
        validateCustomTrendingByFilter(testName, testsToCreate, trendExpected); // by filter
        validateCustomTrendingById(idList, testsToCreate, trendExpected); // by id

        // test increasing trend  -> increase by 50%
        trendExpected = 150.0;
        setCustomTrending(dataGenResponses, trendExpected);
        validateCustomTrendingByFilter(testName, testsToCreate, trendExpected); // by filter
        validateCustomTrendingById(idList, testsToCreate, trendExpected); // by id

        // test no trend  -> 0%
        trendExpected = 0.0;
        setCustomTrending(dataGenResponses, trendExpected);
        validateCustomTrendingByFilter(testName, testsToCreate, trendExpected); // by filter
        validateCustomTrendingById(idList, testsToCreate, trendExpected); // by id


    }

    private void validateCustomTrendingByFilter(String testName, int testsToCreate, double trendExpected) throws UnirestException {
        HttpResponse<JsonNode> resp = getSummaryTrendByFilter(testName, testsToCreate);
        testCustomTrending(resp.getBody().getArray(), trendExpected);
    }

    private void validateCustomTrendingById(String ids, int testsToCreate, double trendExpected) throws UnirestException {
        HttpResponse<JsonNode> resp = getSummaryTrendById(ids, testsToCreate);
        testCustomTrending(resp.getBody().getArray(), trendExpected);
    }

    private void validateBasicTrends(JSONArray respArr, int testsToCreate) {
        String initialValue = "0 | 0%";
        String[] trends = {"totalCallCountTrend", "respAvgTrend", "respMedianTrend", "respPct90Trend"}; //, "totalBytesTrend", "tpsMedianTrend", "tpsMaxTrend" };
        // test the initial trend is always 0 (should be the last one returned since the order is reversed)
        Arrays.stream(trends).forEach(trend -> assertEquals("Initailized to 0", respArr.getJSONObject(testsToCreate - 1).getString(trend), initialValue));

        // test the 1st one in the list has a trend
        Arrays.stream(trends).forEach(trend -> {
            // totalCallCountTrend as that should be the same each time
            if (trend.equals("totalCallCountTrend")) {
                assertEquals("Hasn't changed", respArr.getJSONObject(0).getString(trend), initialValue);
            } else {
                // should not equal 0
                //System.out.printf("Trend for %s is %s%n", trend, respArr.getJSONObject(0).getString(trend));
                assertNotEquals("Not 0", respArr.getJSONObject(0).getString(trend), initialValue);
            }
        });
    }

    private List<DataGenResponse> createData(String testName, int testsToCreate) {
        // create data to trend on
        Map<String, Object> loadTestOverrides = new HashMap<>();
        // set static testname and vuser count for all tests
        loadTestOverrides.put("testName", testName);
        loadTestOverrides.put("vuserCount", 10);
        DataGenRequest request = new DataGenRequest(testsToCreate, 4, 4, 3, 3, true);
        request.setLoadTestObjOverrides(loadTestOverrides);
        return dataGenService.generateData(request);
    }

    private HttpResponse<JsonNode> getSummaryTrendByFilter(String filter, int testsToCreate) throws UnirestException {
        HttpResponse<JsonNode> resp = Unirest.get(getBaseUrl() + "/loadsvc/v1/loadtesttrends/summarytrend")
                .queryString("filter", filter).asJson();
        assertEquals("Get Status", 200, resp.getStatus());
        assertEquals("Get is Array", true, resp.getBody().isArray());
        assertEquals("Correct return count", true, resp.getBody().getArray().length() == testsToCreate);
        return resp;
    }

    private HttpResponse<JsonNode> getSummaryTrendById(String ids, int testsToCreate) throws UnirestException {
        HttpResponse<JsonNode> resp = Unirest.get(getBaseUrl() + "/loadsvc/v1/loadtesttrends/summarytrend")
                .queryString("ids", ids).asJson();
        assertEquals("Get Status", 200, resp.getStatus());
        assertEquals("Get is Array", true, resp.getBody().isArray());
        assertEquals("Correct return count", true, resp.getBody().getArray().length() == testsToCreate);
        return resp;
    }

    private void testCustomTrending(JSONArray respArray, double trendExpected){
        // loop through the response trends and make sure they are correct
        for (int i = 0; i < respArray.length() - 1; i++) {
            // take "3333 | 25.34343%" and parse so I just have the double value 25.34343
            double trendActual = Double.valueOf(respArray.getJSONObject(i).getString("respMedianTrend").split("\\|")[1].replace("%", "").trim());
            System.out.printf("Test id  #%d - expected: %s  |  actual: %s%n", respArray.getJSONObject(i).getInt("loadTestId"), trendExpected, trendActual);
            assertEquals("Correct trend pct", trendExpected, trendActual, 0.5); // 0.5 to account for rounding i'm doing from double to int in my test
        }
    }

    private void setCustomTrending(List<DataGenResponse> dataGenResponses, double trendFactorPercentage) {
        // double each test
        AtomicDouble baseResponseTime = new AtomicDouble(100);
        dataGenResponses.forEach(test -> {
            // double each time
            double trendFactor = (trendFactorPercentage >= 0) ? trendFactorPercentage / 100 + 1 : trendFactorPercentage / 100;
            String updateSql  = "update load_test_data set response_time = ? where load_test_id = ?";
            jdbcTemplate.update(updateSql, (int) Math.round(baseResponseTime.get()), test.getLoadTestId());
            loadTestAggregateService.aggregateLoadTestData(test.getLoadTestId()); // reaggregate data
            baseResponseTime.set(baseResponseTime.get() * trendFactor); // apply trended response time for next test
        });
    }



    // ---------- summary group trend
    private HttpResponse<JsonNode> getSummaryTrendGroup(Map<String, Object> queryStrings, int testsToCreate) throws UnirestException {
        HttpResponse<JsonNode> resp = Unirest.get(getBaseUrl() + "/loadsvc/v1/loadtesttrends/summarytrendgroup")
                .queryString(queryStrings).asJson();
        assertEquals("Get Status", 200, resp.getStatus());
        assertEquals("Get is Array", true, resp.getBody().isArray());
        assertEquals("Correct return count", true, resp.getBody().getArray().getJSONObject(0).getJSONArray("summaryTrend").length() == testsToCreate);
        return resp;
    }

    private void verifySparkline(HttpResponse<JsonNode> resp, String sparklineName, String fieldName) {
        // verify spark line
        JSONArray jsonArray = resp.getBody().getArray().getJSONObject(0).getJSONArray(sparklineName);
        List<Integer> actualList = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            actualList.add(jsonArray.getInt(i));
        }
        List<Integer> expectedList = new ArrayList<>();
        JSONArray summaryTrendArr = resp.getBody().getArray().getJSONObject(0).getJSONArray("summaryTrend");
        for(int i=0; i<jsonArray.length(); i++){
            expectedList.add(summaryTrendArr.getJSONObject(i).getInt(fieldName));
        }

        assertEquals("list are equal", expectedList, actualList);
    }




}