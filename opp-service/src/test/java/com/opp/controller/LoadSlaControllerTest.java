package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.domain.LoadSlaGroup;
import com.opp.service.DataGenService;
import com.opp.service.LoadSlaGroupService;
import com.opp.util.RestUtil;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ctobe on 8/10/16.
 */
public class LoadSlaControllerTest extends BaseIntegrationTest {

    @Autowired
    DataGenService dataGenService;

    @Autowired
    private LoadSlaGroupService loadSlaGroupService;

    private String GROUP_NAME = "integration-test-group";
    private Integer groupId;

    @Before
    public void setUp() throws Exception {
        // need to create group for SLA
        groupId = loadSlaGroupService.add(new LoadSlaGroup(GROUP_NAME)).get().getId();
    }

    @After
    public void tearDown(){
        loadSlaGroupService.delete(groupId); // destroy SLA group
    }

    @Test
    public void testCrud() throws Exception {
        String apiUrl = getBaseUrl() + "/loadsvc/v1/slas";
        RestUtil.testFullCrud(apiUrl, apiUrl + "/{id}", "id", getCreateObj(), getUpdateObj());
    }

    @Test
    @Ignore
    public void testGetAllByLoadTestId() throws Exception {
        //todo: to write test for this
    }


    private JSONObject getCreateObj(){
        JSONObject createObj = new JSONObject();
        createObj.put("loadSlaGroupId",groupId);
        //  createObj.put("customName", "");
        //  createObj.put("customValue", 0);
        createObj.put("marginOfError",0.10);
        createObj.put("max",1000);
        createObj.put("median", 300);
        createObj.put("min",100);
        createObj.put("name", "000 Junit Test SLA 000");
        createObj.put("avg", 400);
        createObj.put("pct75", 500);
        createObj.put("pct90", 700);
        return createObj;
    }

    private JSONObject getUpdateObj(){
        JSONObject createObj = new JSONObject();
        createObj.put("loadSlaGroupId",groupId);
      //  createObj.put("customName", "");
      //  createObj.put("customValue", 0);
        createObj.put("marginOfError",0.20);
        createObj.put("max",2000);
        createObj.put("median", 600);
        createObj.put("min",200);
        createObj.put("name", "000 Junit Test SLA 000");
        createObj.put("avg", 800);
        createObj.put("pct75", 1000);
        createObj.put("pct90", 1400);
        return createObj;
    }


}