package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.util.RestUtil;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

/**
 * Created by ctobe on 4/28/17.
 */
public class CiLoadTestJobTypeControllerTest extends BaseIntegrationTest {

    private static final String JOB_TYPE = "integration-test-job-type-delete";

    @After
    public void tearDown(){
        jdbcTemplate.update("DELETE from ci_load_test_job_type where job_type = ?", JOB_TYPE);

    }

    @Test
    public void testCrud() throws Exception {
        String apiUrl = getBaseUrl() + "/loadsvc/v1/ci/loadtestjobtypes";
        JSONObject createObj = getCreateObj();
        RestUtil.testFullCrud(apiUrl, apiUrl + "/{id}", "id", createObj, getUpdateObj());

        // create record
        RestUtil.verifyPost(apiUrl, createObj, "id");
        // search by jobType
        String queryStr = String.format("?jobType=%s", createObj.getString("jobType"));
        RestUtil.verifyGetAll(apiUrl + queryStr);

    }

    private JSONObject getCreateObj(){
        JSONObject createObj = new JSONObject();
        createObj.put("jobType", JOB_TYPE);
        createObj.put("additionalOptions", "blah balh balh");
        createObj.put("testTool", "My Test Tool");
        createObj.put("testToolVersion", "234234.234");
        return createObj;
    }

    private JSONObject getUpdateObj(){
        JSONObject updateObj = getCreateObj();
        updateObj.put("jobType", JOB_TYPE);
        updateObj.put("additionalOptions", "blah balh balh2");
        updateObj.put("testTool", "My Test Tool2");
        updateObj.put("testToolVersion", "234234.2342");
        return updateObj;
    }


}