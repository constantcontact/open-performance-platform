package com.opp.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.opp.BaseIntegrationTest;
import com.opp.domain.ux.WptTestImport;
import com.opp.domain.ux.WptTestLabel;
import com.opp.domain.ux.WptUINavigation;
import com.opp.service.DataGenService;
import com.opp.service.WptService;
import com.opp.util.MathUtil;
import com.opp.util.RestUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by ctobe on 6/22/16.
 */
public class WptResultControllerTest extends BaseIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataGenService dataGenService;

    @Autowired
    WptService wptService;

    @Test
    public void testCrud() throws Exception {
        String wptTestId = "170322_XT_17K4";
        String label = "l1.em-ui.editor5555.cc-us-east.chrome.cable";
        String apiUrl = String.format("%s/uxsvc/v1/wpt/tests", getBaseUrl());


//        // Create - POST
        HttpResponse<JsonNode> httpResponse = Unirest.post(apiUrl).body(new WptTestImport(wptTestId, label)).asJson();
        assertTrue("POST - Verifying Response Code", httpResponse.getStatus() == 201);

//
//        // Get all - GET
        HttpResponse<JsonNode> response = RestUtil.verifyGetAll(apiUrl);
        int id = response.getBody().getArray().getJSONObject(0).getInt("id");

        String apiUrlWithId = apiUrl + "/" + id;
//
//        // Update - PUT
//        JSONObject putData = getDataObj(loadTestId);
//        RestUtil.verifyPut(apiUrlWithId, putData);
//
//        // Get by id - GET
//        RestUtil.verifyGet(apiUrlWithId, putData);
//
//        // Delete - DELETE
//        RestUtil.verifyDelete(apiUrlWithId);
//
//        // Delete All - DELETE
//        response = Unirest.delete(apiUrl).asJson();
//        assertTrue("DELETE - Verifying Response Code", response.getStatus() == 202);
//        response = Unirest.get(apiUrl).asJson(); // confirming
//        assertTrue("DELETE - Content no longer exists", response.getBody().getArray().length() == 0);

    }


    @After
    public void tearDown() throws Exception {

    }




}