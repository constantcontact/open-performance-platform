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
import com.opp.service.WptService;
import com.opp.util.RestUtil;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * Created by ctobe on 6/22/16.
 */
public class WptResultControllerTest extends BaseIntegrationTest {

    @Autowired
    private WptService wptService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrud() throws Exception {
        String rawWptResultTestFile = "wptresult.test.json";
        String wptTestId = "170322_XT_17K4"; // this ID should match whats in the above file
        String label = "l1.em-ui.editor5555.cc-us-east.chrome.cable";
        String apiUrl = String.format("%s/uxsvc/v1/wpt/tests", getBaseUrl());
        String importUrl = apiUrl.replace("tests", "import");



        // Import from WPT - POST --- commented out because it relies on 3rd party WPT to be up and running but a good test if needed
        // /uxsvc/v1/wpt/import
        // HttpResponse<JsonNode> importResponse1 = Unirest.post(importUrl).body(new WptTestImport(wptTestId, label)).asJson();
        // assertTrue("POST - Verifying Response Code", importResponse1.getStatus() == 201);

        // Import from Raw WPT JSON - POST
        String wptRawTestResult = getRawWptResultFromFile(rawWptResultTestFile);

        String postData = objectMapper.writeValueAsString(new WptTestImport(wptTestId, label, wptRawTestResult));
        HttpResponse<JsonNode> importResponse2 = Unirest.post(importUrl).body(postData).asJson();
        assertTrue("POST - Verifying Response Code", importResponse2.getStatus() == 201);

        // Create - POST
        // get a WptResult object from a sample wpt raw data file
        WptResult wptTest = getWptTest(rawWptResultTestFile, label);

        HttpResponse<JsonNode> createResponse = Unirest.post(apiUrl).body(objectMapper.writeValueAsString(wptTest)).asJson();
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

    private String getRawWptResultFromFile(String fileName){
        URL url = Resources.getResource(fileName);
        try {
            return Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private WptResult getWptTest(String fileName, String label){
        try {
            return wptService.importTestFromJson(getRawWptResultFromFile(fileName), new WptTestLabel(label), false).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @After
    public void tearDown() throws Exception {

    }




}