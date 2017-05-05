package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.service.ApplicationMapExternalService;
import com.opp.util.RestUtil;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by ctobe on 4/28/17.
 */
public class CiLoadTestJobControllerTest extends BaseIntegrationTest {

    private static final String TEST_NAME = "integration-test-name-delete";

    @After
    public void tearDown(){
        jdbcTemplate.update("DELETE from ci_load_test_job where test_name = ?", TEST_NAME);

    }

    @Test
    public void testCrud() throws Exception {
        String apiUrl = getBaseUrl() + "/loadsvc/v1/ci/loadtestjobs";
        JSONObject createObj = getCreateObj();
        RestUtil.testFullCrud(apiUrl, apiUrl + "/{id}", "id", createObj, getUpdateObj());

        // create record
        RestUtil.verifyPost(apiUrl, createObj, "id");
        // search
        String queryStr = String.format("?testName=%s&testType=%s", createObj.getString("testName"), createObj.getString("testType"));
        RestUtil.verifyGetAll(apiUrl + queryStr);

    }

    private JSONObject getCreateObj(){
        JSONObject createObj = new JSONObject();
        createObj.put("appUnderTest", "my app");
        createObj.put("appUnderTestVersion", "v 1.000");
        createObj.put("comments", "I don't have any comments");
        createObj.put("cronSchedule", "");
        createObj.put("description", "The description");
        createObj.put("environment", "Dee Environment");
        createObj.put("hostName", "");
        createObj.put("rampVuserEndDelay", 200);
        createObj.put("rampVuserStartDelay", 1000);
        createObj.put("runDuration", 20000);
        createObj.put("slaGroupId", 20);
        createObj.put("testPath", "/my/path/to/my/test");
        createObj.put("testType", "resultcollection");
        createObj.put("testName", TEST_NAME);
        createObj.put("testSubName", "test.sub.name");
        createObj.put("testDataType", "soasta");
        createObj.put("vuserCount", 200);
        return createObj;
    }

    private JSONObject getUpdateObj(){
        JSONObject updateObj = getCreateObj();
        updateObj.put("appUnderTest", "my app2");
        updateObj.put("appUnderTestVersion", "v 1.0002");
        updateObj.put("comments", "I don't have any comment2s");
        updateObj.put("cronSchedule", "2 2 2 2 *");
        updateObj.put("description", "The description2");
        updateObj.put("environment", "Dee Environmen2t");
        updateObj.put("hostName", "myhost.com");
        updateObj.put("rampVuserEndDelay", 2000);
        updateObj.put("rampVuserStartDelay", 10000);
        updateObj.put("runDuration", 200000);
        updateObj.put("slaGroupId", 200);
        updateObj.put("testPath", "/my/path/to/my/test2");
        updateObj.put("testType", "resultcollection2");
        updateObj.put("testName", TEST_NAME);
        updateObj.put("testSubName", "test.sub.name2");
        updateObj.put("testDataType", "soasta2");
        updateObj.put("vuserCount", 100);
        return updateObj;
    }


}