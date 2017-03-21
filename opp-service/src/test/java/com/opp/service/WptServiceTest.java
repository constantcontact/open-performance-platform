package com.opp.service;

import com.opp.BaseIntegrationTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WptServiceTest extends BaseIntegrationTest {

    @Autowired
    private WptService wptService;


    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Ignore
    public void testing() throws Exception {
        JsonNode jsonNode = objectMapper.readTree("{ \"name\":\"craig\", \"city\": \"Boston\" }");

        //CouchDbActionResp couchDbActionResp = wptService.storeRawResults(jsonNode);
        //assertTrue("store raw results", couchDbActionResp.isOk());

       // wptService.getTrendDataForTest("p2.galileo.preview-document-toolkit.cc-us-east.chrome.native.announcement-layout-2", "firstView", "median", false);

       // wptService.importTest("161014_NX_D", true);


    }
}
