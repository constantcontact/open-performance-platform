package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.dto.datagen.DataGenRequest;
import com.opp.dto.datagen.DataGenResponse;
import com.opp.service.DataGenService;
import com.opp.util.MathUtil;
import com.opp.util.RestUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by ctobe on 6/22/16.
 */
public class LoadTestDataControllerTest extends BaseIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataGenService dataGenService;

    @Test
    public void testCrud() throws Exception {
        List<DataGenResponse> dataGenResponses = dataGenService.generateData(new DataGenRequest(1, 1, 1, 5, 5, false));
        int loadTestId = dataGenResponses.get(0).getLoadTest().getId();
        String apiUrl = String.format("%s/loadsvc/v1/loadtests/%d/data", getBaseUrl(), loadTestId);

        // Create - POST
        HttpResponse<JsonNode> httpResponse = Unirest.post(apiUrl).body(getDataArray(loadTestId)).asJson();
        assertTrue("POST - Verifying Response Code", httpResponse.getStatus() == 201);

        // Get all - GET
        HttpResponse<JsonNode> response = RestUtil.verifyGetAll(apiUrl);
        int id = response.getBody().getArray().getJSONObject(0).getInt("id");

        String apiUrlWithId = apiUrl + "/" + id;

        // Update - PUT
        JSONObject putData = getDataObj(loadTestId);
        RestUtil.verifyPut(apiUrlWithId, putData);

        // Get by id - GET
        RestUtil.verifyGet(apiUrlWithId, putData);

        // Delete - DELETE
        RestUtil.verifyDelete(apiUrlWithId);

        // Delete All - DELETE
        response = Unirest.delete(apiUrl).asJson();
        assertTrue("DELETE - Verifying Response Code", response.getStatus() == 202);
        response = Unirest.get(apiUrl).asJson(); // confirming
        assertTrue("DELETE - Content no longer exists", response.getBody().getArray().length() == 0);

    }

    @After
    public void tearDown() throws Exception {

    }


    private JSONArray getDataArray(int loadTestId){
        JSONArray jsonArray = new JSONArray();
        return jsonArray.put(getDataObj(loadTestId));
    }

    private JSONObject getDataObj(int loadTestId){
       JSONObject obj = new JSONObject();
        obj.put("loadTestId", loadTestId);
        obj.put("transactionName", "junit-create-load-test-data");
        obj.put("responseTime", MathUtil.getRandomInt(800, 1500));
        obj.put("bytesReceived", MathUtil.getRandomInt(200, 800));
        obj.put("bytesSent", MathUtil.getRandomInt(200, 800));
        obj.put("connectionTime", MathUtil.getRandomInt(200, 800));
        obj.put("dnsLookup", MathUtil.getRandomInt(200, 800));
        obj.put("errorCount", MathUtil.getRandomInt(200, 800));
        obj.put("location", "My house");
        obj.put("operation", "GET");
        obj.put("receiveTime", MathUtil.getRandomInt(200, 800));
        obj.put("sendTime", MathUtil.getRandomInt(200, 800));
        obj.put("server", "My Server");
        obj.put("startTime", Instant.now().getEpochSecond());
        obj.put("target", "www.constantcontact.com");
        obj.put("ttfb", MathUtil.getRandomInt(200, 300));
        obj.put("ttlb", MathUtil.getRandomInt(800, 1500));
        return obj;
    }


}