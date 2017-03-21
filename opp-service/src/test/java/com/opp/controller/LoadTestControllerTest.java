package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.util.MathUtil;
import com.opp.util.RestUtil;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.HOURS;

/**
 * Created by ctobe on 6/22/16.
 */
public class LoadTestControllerTest extends BaseIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() throws Exception {
        // clean out any existing instances of this app key
    //    jdbcTemplate.update("DELETE from application where app_key = ?", APP_KEY);
    }


    @Test
    public void testCrud() throws Exception {
        String apiUrl = getBaseUrl() + "/loadsvc/v1/loadtests";
        RestUtil.testFullCrud(apiUrl, apiUrl + "/{id}", "id", getData(), getData());
    }

    @After
    public void tearDown() throws Exception {

    }


    private JSONObject getData(){
        JSONObject obj = new JSONObject();
        obj.put("appUnderTest", "Fake App");
        obj.put("appUnderTestVersion", "1.0." + MathUtil.getRandomInt(1, 100));
        obj.put("comments", "deez comments " + MathUtil.getRandomInt(1, 100));
        obj.put("description", "this is my description " + MathUtil.getRandomInt(1, 100));
        obj.put("startTime", Instant.now().getEpochSecond());
        obj.put("endTime", Instant.now().plus(1, HOURS).getEpochSecond());
        obj.put("environment", "my env  " + MathUtil.getRandomInt(1, 100));
        obj.put("testName", "Generated Data Test " + MathUtil.getRandomInt(1, 100));
        obj.put("testSubName", "Test Sub Name "  + MathUtil.getRandomInt(1, 100));
        obj.put("testTool", "CloudTest" + MathUtil.getRandomInt(1, 100));
        obj.put("testToolVersion", "1.0." + MathUtil.getRandomInt(1, 100));
        obj.put("vuserCount", MathUtil.getRandomInt(1, 10));
        //obj.put("sla_group_id", );
        //obj.put("external_test_id", loadTest.getExternalTestId());

        return obj;
    }


}