package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.domain.ApplicationMap;
import com.opp.dto.datagen.DataGenRequest;
import com.opp.dto.datagen.DataGenResponse;
import com.opp.service.ApplicationMapService;
import com.opp.service.DataGenService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ctobe on 7/20/16.
 */
public class LoadTestApplicationCoverageControllerTest extends BaseIntegrationTest {

    @Autowired
    DataGenService dataGenService;
    @Autowired
    ApplicationMapService applicationMapService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String APP_KEY = "junit-test-app-coverage";


    @Before
    public void setUp() throws Exception {
        // clean out any existing instances of this app key
        jdbcTemplate.update("DELETE from application where app_key = ?", APP_KEY);
    }

    @Test
    public void testLoadTestCoverage() throws Exception {

        // generate 2 load tests
        List<DataGenResponse> dataGenResponseList = dataGenService.generateData(new DataGenRequest(2, 1, 1, 1, 1, false));

        // get loadtest name and app id
        String loadTestName1 = dataGenResponseList.get(0).getLoadTest().getTestName();
        String loadTestName2 = dataGenResponseList.get(1).getLoadTest().getTestName();
        int applicationId = createApplication();

        JSONObject postObject = new JSONObject();
        postObject.put("loadTestName", loadTestName1);
        postObject.put("applicationId", applicationId);

        // POST - add new one
        HttpResponse<JsonNode> postResp = Unirest.post(getBaseUrl() + "/loadsvc/v1/loadtestapplicationcoverages")
            .body(postObject).asJson();
        System.out.println(postResp.getBody().getObject());
        assertEquals("Created object status", 201, postResp.getStatus());
        assertEquals("LoadTest Name", loadTestName1, postResp.getBody().getObject().getString("loadTestName"));
        assertEquals("Application Id", applicationId, postResp.getBody().getObject().getInt("applicationId"));

        // GET All
        HttpResponse<JsonNode> getAllResp = Unirest.get(getBaseUrl() + "/loadsvc/v1/loadtestapplicationcoverages").asJson();
        System.out.println(getAllResp.getBody().getArray());
        assertEquals("Get All Back Status", 200, getAllResp.getStatus());
        assertEquals("Get All Back", true, getAllResp.getBody().isArray());

        // GET by loadtestname and applicationId
        HttpResponse<JsonNode> getFilteredResp = Unirest.get(getBaseUrl() + "/loadsvc/v1/loadtestapplicationcoverages")
                .queryString("loadTestName", loadTestName1).queryString("applicationId", applicationId).asJson();
        System.out.println(getFilteredResp.getBody().getArray());
        assertEquals("Get filtered status", 200, getFilteredResp.getStatus());
        assertEquals("Only 1 back", 1, getFilteredResp.getBody().getArray().length());


        // add 2nd loadtestcoverage one so the next test can return 2 records
        postResp = Unirest.post(getBaseUrl() + "/loadsvc/v1/loadtestapplicationcoverages")
                .body(postObject.put("loadTestName", loadTestName2)).asJson();
        System.out.println(postResp.getBody().getObject());
        assertEquals("Created object 2 status", 201, postResp.getStatus());

        // GET by ApplicationId -- should return 2 since i just created one above with the same app id
        getFilteredResp = Unirest.get(getBaseUrl() + "/loadsvc/v1/loadtestapplicationcoverages")
                .queryString("applicationId", applicationId).asJson();
        System.out.println(getFilteredResp.getBody().getArray());
        assertEquals("Get filtered status", 200, getFilteredResp.getStatus());
        assertEquals("Only 2 back", 2, getFilteredResp.getBody().getArray().length());

        // GET by LoadTestName
        getFilteredResp = Unirest.get(getBaseUrl() + "/loadsvc/v1/loadtestapplicationcoverages")
                .queryString("loadTestName", loadTestName1).asJson();
        System.out.println(getFilteredResp.getBody().getArray());
        assertEquals("Get filtered status", 200, getFilteredResp.getStatus());
        assertEquals("Only 1 back", 1, getFilteredResp.getBody().getArray().length());
        assertEquals("LoadTest Name", loadTestName1, getFilteredResp.getBody().getArray().getJSONObject(0).getString("loadTestName"));
        assertEquals("Application Id", applicationId, getFilteredResp.getBody().getArray().getJSONObject(0).getInt("applicationId"));




        // DELETE by LoadTestName
        JSONObject deleteByTestName = new JSONObject();
        deleteByTestName.put("loadTestName", loadTestName1);
        HttpResponse<JsonNode> deleteResp = Unirest.delete(getBaseUrl() + "/loadsvc/v1/loadtestapplicationcoverages")
                .body(deleteByTestName).asJson();
        assertEquals("Delete object status", 202, deleteResp.getStatus());

        // DELETE by ApplicationId
        JSONObject deleteByApplicationid = new JSONObject();
        deleteByApplicationid.put("applicationId", applicationId);
        deleteResp = Unirest.delete(getBaseUrl() + "/loadsvc/v1/loadtestapplicationcoverages")
                .body(deleteByApplicationid).asJson();
        assertEquals("Delete object status", 202, deleteResp.getStatus());

        // Create another one
        postResp = Unirest.post(getBaseUrl() + "/loadsvc/v1/loadtestapplicationcoverages")
                .body(postObject.put("loadTestName", loadTestName2)).asJson();
        System.out.println(postResp.getBody().getObject());
        assertEquals("Created object 3 status", 201, postResp.getStatus());

        // DELETE by Both
        JSONObject deleteByAppAndTestName = new JSONObject();
        deleteByAppAndTestName.put("applicationId", applicationId);
        deleteByAppAndTestName.put("loadTestName", loadTestName2);
        deleteResp = Unirest.delete(getBaseUrl() + "/loadsvc/v1/loadtestapplicationcoverages")
                .body(deleteByAppAndTestName).asJson();
        assertEquals("Delete object status", 202, deleteResp.getStatus());
    }



    private int createApplication(){
        ApplicationMap applicationMap = new ApplicationMap();
         // add the object to insert
        applicationMap.setIsClientSide(true);
        applicationMap.setAppKey(APP_KEY);
        applicationMap.setInCdPipelineClient(true);
        applicationMap.setKqiAppName("test-kqi-app-name");
        applicationMap.setSecurityId(234234234);
        applicationMap.setNewrelic("my-nr-name");
        return applicationMapService.add(applicationMap).get().getId();

    }






}