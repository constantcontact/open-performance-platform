package com.opp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.opp.BaseIntegrationTest;
import com.opp.domain.ux.WptResult;
import com.opp.domain.ux.WptTestImport;
import com.opp.domain.ux.WptTestLabel;
import com.opp.service.DataGenService;
import com.opp.service.WptService;
import com.opp.util.RestUtil;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * Created by ctobe on 6/22/16.
 */
public class WptResultControllerTest extends BaseIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataGenService dataGenService;

    @Autowired
    private WptService wptService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrud() throws Exception {
        String wptTestId = "170322_XT_17K4";
        String label = "l1.em-ui.editor5555.cc-us-east.chrome.cable";
        String apiUrl = String.format("%s/uxsvc/v1/wpt/tests", getBaseUrl());

        // Import from WPT - POST
        // /uxsvc/v1/wpt/import
        HttpResponse<JsonNode> importResponse = Unirest.post(apiUrl.replace("tests", "import")).body(new WptTestImport(wptTestId, label)).asJson();
        assertTrue("POST - Verifying Response Code", importResponse.getStatus() == 201);

        // Create - POST
        WptResult wptTest = getWptTest(wptTestId, label);
        HttpResponse<JsonNode> createResponse = Unirest.post(apiUrl).body(wptTest).asJson();
        assertTrue("POST - Verifying Response Code", createResponse.getStatus() == 201);

        // Get all - GET
        HttpResponse<JsonNode> response = RestUtil.verifyGetAll(apiUrl);
        String id = response.getBody().getArray().getJSONObject(0).getString("id");

        String apiUrlWithId = apiUrl + "/" + wptTestId;

        // Update - PUT
        wptTest.setRequestCount(200); // change something
        RestUtil.verifyPut(apiUrlWithId, new JSONObject(wptTest), Arrays.asList("id", "requestCount"));

        // Get by id - GET
        RestUtil.verifyGetSerialized(apiUrlWithId, wptTest, WptResult.class);

        // Delete - DELETE
        RestUtil.verifyDelete(apiUrlWithId);

    }

    private WptResult getWptTest(String id, String label){
        URL url = Resources.getResource("wptresult.test.json");
        try {
            String wptJson = Resources.toString(url, Charsets.UTF_8);
            WptResult wptResult = wptService.importTestFromJson(wptJson, new WptTestLabel(label), false).get();
            return wptResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @After
    public void tearDown() throws Exception {

    }




}