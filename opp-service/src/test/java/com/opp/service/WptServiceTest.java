package com.opp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.opp.BaseIntegrationTest;
import com.opp.domain.ux.WptTestImport;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class WptServiceTest extends BaseIntegrationTest {


    @Autowired
    private WptService wptService;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransportClient client;


    @Test
    @Ignore
    public void testing() throws Exception {
        JsonNode jsonNode = objectMapper.readTree("{ \"name\":\"craig\", \"city\": \"Boston\" }");

        WptTestImport wptTestImport = new WptTestImport();
        wptTestImport.setWptId("170322_XT_17K4");
        wptTestImport.setWptTestLabel("l1.galileo.load-editor-toolkit.cc-us-east.chrome.cable.announcement-layout-2");
        wptService.importTest(wptTestImport, false);


    }

    @Test
    public void esClientWorks() throws UnirestException {

        SearchResponse response = client.prepareSearch("wpt-summary")
                .setTypes("result")
                .setQuery(QueryBuilders.termQuery("id", "blah"))
                .get();
        assertTrue("its working and getting a response back", response.getHits().getTotalHits() == 0);

    }


}
