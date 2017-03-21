package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.dto.datagen.DataGenRequest;
import com.opp.dto.datagen.DataGenResponse;
import com.opp.service.DataGenService;
import com.opp.service.ReportService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Created by ctobe on 7/20/16.
 */
public class ReportControllerTest extends BaseIntegrationTest {

    @Autowired
    DataGenService dataGenService;
    @Autowired
    ReportService reportService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String TEST_NAME_KEY = "junit-test-app-reports";

    @Before
    public void setUp() throws Exception {
        // clean out any existing instances of this app key
        jdbcTemplate.update("DELETE from load_test where test_name like ?", "%"+TEST_NAME_KEY);
    }

    @Test
    public void testRunHistory() throws Exception {

        // generate 3 load tests
        List<DataGenResponse> dataGenResponseList = dataGenService.generateData(new DataGenRequest(3, 1, 1, 1, 1, false, TEST_NAME_KEY));

         // GET today
        final int daysBack = 1;
        HttpResponse<JsonNode> resp = Unirest.get(getBaseUrl() + "/loadsvc/v1/reports/testrunhistory")
                .queryString("daysBack", daysBack).asJson();
        System.out.println(resp.getBody().getArray());
        assertEquals("Get Status", 200, resp.getStatus());
        assertEquals("Get is Array", true, resp.getBody().isArray());
        assertEquals("At least 3", true, resp.getBody().getArray().length() >= 3);
        // check to make sure all tests are after the specified days back
        IntStream.range(0, resp.getBody().getArray().length()).forEach(i ->
                assertTrue("Date is after", Instant.parse(resp.getBody().getArray().getJSONObject(i).getString("lastRun") + "Z").isAfter(Instant.now().minus(daysBack, DAYS)))
        );

        // GET today
        int daysBackFuture = -1;
        HttpResponse<JsonNode> resp2 = Unirest.get(getBaseUrl() + "/loadsvc/v1/reports/testrunhistory")
                .queryString("daysBack", daysBackFuture).asJson();
        System.out.println(resp2.getBody().getArray());
        assertEquals("Get Status", 200, resp2.getStatus());
        assertEquals("Get is Array", true, resp2.getBody().isArray());
        assertEquals("The future, should get none back", true, resp2.getBody().getArray().length() == 0);

    }


    @Test
    public void loadTestReportTest() throws Exception {

        // generate 3 load tests
        List<DataGenResponse> dataGenResponseList = dataGenService.generateData(new DataGenRequest(3, 3, 3, 10, 10, true, TEST_NAME_KEY));

        int loadTestId = dataGenResponseList.get(dataGenResponseList.size()-1).getLoadTestId();


        reportService.getLoadTestTrendReport(loadTestId);

        HttpResponse<JsonNode> resp = Unirest.get(getBaseUrl() + "/loadsvc/v1/loadtests/"+loadTestId+"/trendreport").asJson();
        System.out.println(resp.getBody().getArray());
        assertEquals("Get Status", 200, resp.getStatus());
        assertEquals("Load Test matches", loadTestId, resp.getBody().getObject().getJSONObject("loadTest").getInt("id"));
        assertEquals("Got aggregate data", 3, resp.getBody().getObject().getJSONArray("aggregateData").length());
        assertTrue("Has valid data", resp.getBody().getObject().getJSONArray("aggregateData").getJSONObject(0).getInt("respPct90") > 0);
        // check to make sure all tests are after the specified days back


    }



}