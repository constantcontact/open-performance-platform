package com.opp.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.opp.BaseIntegrationTest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;


public class WptServiceTest extends BaseIntegrationTest {


    @Autowired
    private WptService wptService;

    @Autowired
    private TransportClient client;


    @Test
    public void esClientWorks() throws UnirestException {

        SearchResponse response = client.prepareSearch("wpt-summary")
                .setTypes("result")
                .setQuery(QueryBuilders.termQuery("id", "blah"))
                .get();
        assertTrue("its working and getting a response back", response.getHits().getTotalHits() == 0);

    }

    @Test
    public void getTestTrendTable() throws UnirestException {

        wptService.getTrendTableData("l1.em-ui.editor.cc-us-east.chrome.cable", "firstView", "median");
        // assertTrue("its working and getting a response back", response.getHits().getTotalHits() == 0);

    }

    @Test
    public void getTestTrendHistogram() throws UnirestException {

        wptService.getTrendChartData("l1.em-ui.editor.cc-us-east.chrome.cable", "firstView", "1d");
        // assertTrue("its working and getting a response back", response.getHits().getTotalHits() == 0);

    }

    @Test
    public void getUserTimings(){
        String run = "median";
        String view = "firstView";
        String interval = "1d";
        wptService.getCustomUserTimingData("l1.em-ui.editor.cc-us-east.chrome.cable", run, view, false, interval);
    }


}
