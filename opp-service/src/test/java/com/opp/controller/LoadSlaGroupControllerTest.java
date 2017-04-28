package com.opp.controller;

import com.mashape.unirest.http.Unirest;
import com.opp.BaseIntegrationTest;
import com.opp.service.DataGenService;
import com.opp.util.RestUtil;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ctobe on 8/10/16.
 */

public class LoadSlaGroupControllerTest extends BaseIntegrationTest {

    @Autowired
    DataGenService dataGenService;

    @BeforeClass
    public static void preSetUp() throws Exception {
        Unirest.setDefaultHeader("accept", "application/json");
        Unirest.setDefaultHeader("content-type", "application/json");
    }

    @Test
    public void testCrud() throws Exception {
        String apiUrl = getBaseUrl() + "/loadsvc/v1/slagroups";
        RestUtil.testFullCrud(apiUrl, apiUrl + "/{id}", "id", getCreateObj(), getUpdateObj());
    }


    private JSONObject getCreateObj(){
        JSONObject createObj = new JSONObject();
        createObj.put("name","==JUnit SLA Group==");
        return createObj;
    }

    private JSONObject getUpdateObj(){
        JSONObject createObj = new JSONObject();
        createObj.put("name","==JUnit SLA Group Changed==");
        return createObj;
    }


}