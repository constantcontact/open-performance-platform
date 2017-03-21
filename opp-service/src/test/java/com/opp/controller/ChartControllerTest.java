package com.opp.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.opp.domain.LoadTest;
import com.opp.domain.LoadTestAggregateView;
import com.opp.domain.LoadTestTimeChartData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by jhermida on 9/8/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChartControllerTest {

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Value("${server.port}")
    private String port;
    private String domain = "http://localhost";
    private String aggregateResource = "/loadsvc/v1/charts/aggregate/loadtests";
    private String timeseriesResource = "/loadsvc/v1/charts/timeseries/loadtests";
    private String baseUrl;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        baseUrl = domain + ":" + port;
    }

    @Test
    public void aggregateChartDataByLoadTestId_happyPath() throws UnirestException, IOException {

        String url = baseUrl + aggregateResource + "/4888?yaxis=resp_pct90";

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyVararg()))
                .thenReturn(new ArrayList<>(Arrays.asList(buildAggregateView())));


        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(200, httpResponse.getStatus());
    }

    @Test
    public void aggregateChartDataByLoadTestId_invalidId() throws UnirestException, IOException {

        String url = baseUrl + aggregateResource + "/?yaxis=resp_pct90";

        List<Object> loadTestAggregateViews = new ArrayList<>();
        loadTestAggregateViews.add(buildAggregateView());

        LoadTestAggregateView loadTestAggregateView = buildAggregateView();
        loadTestAggregateView.setTransactionName("Test Transaction");

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyVararg()))
                .thenReturn(loadTestAggregateViews);

        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(404, httpResponse.getStatus());
    }

    @Test
    public void aggregateChartDataByLoadTestId_invalidTrendOn() throws UnirestException, IOException {

        String url = baseUrl + aggregateResource + "/4888?yaxis=resp_pct90&trendOn=trendOn";

        List<Object> loadTestAggregateViews = new ArrayList<>();
        loadTestAggregateViews.add(buildAggregateView());

        LoadTestAggregateView loadTestAggregateView = buildAggregateView();
        loadTestAggregateView.setTransactionName("Test Transaction");

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyVararg()))
                .thenReturn(loadTestAggregateViews);

        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(400, httpResponse.getStatus());
    }

    @Test
    public void aggregateChartDataByLoadTestId_invalidYAxis() throws UnirestException, IOException {

        String url = baseUrl + aggregateResource + "/4888?yaxis=yaxis";

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyVararg()))
                .thenReturn(new ArrayList<>(Arrays.asList(buildAggregateView())));


        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(400, httpResponse.getStatus());
    }


    @Test
    public void aggregateChartDataByLoadTestId_invalidXAxis() throws UnirestException, IOException {

        String url = baseUrl + aggregateResource + "/4888?yaxis=resp_pct90&xaxis=xaxis";

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyVararg()))
                .thenReturn(new ArrayList<>(Arrays.asList(buildAggregateView())));


        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(400, httpResponse.getStatus());
    }

    @Test
    public void aggregateChartDataByLoadTestId_invalidPlot() throws UnirestException, IOException {

        String url = baseUrl + aggregateResource + "/4888?yaxis=resp_pct90&plot=plot";

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyVararg()))
                .thenReturn(new ArrayList<>(Arrays.asList(buildAggregateView())));


        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(400, httpResponse.getStatus());
    }


    @Test
    public void timeSeriesChartDataByLoadTestId_happyPath() throws UnirestException, IOException {

        String url = baseUrl + timeseriesResource + "/4888?yaxis=resp_pct90";

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class)))
                .thenReturn(new ArrayList<>(Arrays.asList(buildLoadTestTimeChartData())));


        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(200, httpResponse.getStatus());
    }

    @Test
    public void timeSeriesChartDataByLoadTestId_invalidId() throws UnirestException, IOException {

        String url = baseUrl + timeseriesResource + "/?yaxis=resp_pct90";

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class)))
                .thenReturn(new ArrayList<>(Arrays.asList(buildLoadTestTimeChartData())));


        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(404, httpResponse.getStatus());
    }

    @Test
    public void timeSeriesChartDataByLoadTestId_invalidYAxis() throws UnirestException, IOException {

        String url = baseUrl + timeseriesResource + "/4888?yaxis=yaxis";

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class)))
                .thenReturn(new ArrayList<>(Arrays.asList(buildLoadTestTimeChartData())));


        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(400, httpResponse.getStatus());
    }

    @Test
    public void timeSeriesChartDataByLoadTestId_invalidXAxis() throws UnirestException, IOException {

        String url = baseUrl + timeseriesResource + "/4888?yaxis=resp_pct90&xaxis=xaxis";

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class)))
                .thenReturn(new ArrayList<>(Arrays.asList(buildLoadTestTimeChartData())));


        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(400, httpResponse.getStatus());
    }

    @Test
    public void timeSeriesChartDataByLoadTestId_invalidPlot() throws UnirestException, IOException {

        String url = baseUrl + timeseriesResource + "/4888?yaxis=resp_pct90&plot=plot";

        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class), Mockito.anyInt())).thenReturn(buildLoadTest());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(BeanPropertyRowMapper.class)))
                .thenReturn(new ArrayList<>(Arrays.asList(buildLoadTestTimeChartData())));


        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertEquals(400, httpResponse.getStatus());
    }


    private LoadTestAggregateView buildAggregateView() {
        LoadTestAggregateView loadTestAggregateView = new LoadTestAggregateView();
        loadTestAggregateView.setLoadTestId(4888);
        loadTestAggregateView.setAppUnderTest("form-svc");
        loadTestAggregateView.setAppUnderTestVersion("N/A");
        loadTestAggregateView.setEnvironment("s1");
        loadTestAggregateView.setStartTime(1l);
        loadTestAggregateView.setEndTime(2l);
        loadTestAggregateView.setTestName("form-svc.api.regression.s1");
        loadTestAggregateView.setTestSubName("");
        loadTestAggregateView.setTestTool("SOASTA Cloud Test");
        loadTestAggregateView.setTestToolVersion("7732.159");
        loadTestAggregateView.setVuserCount(10);
        loadTestAggregateView.setExternalTestId("21575");
        loadTestAggregateView.setTransactionName("Transaction Name");
        loadTestAggregateView.setCallCount(1);
        loadTestAggregateView.setRespAvg(2);
        loadTestAggregateView.setRespMax(3);
        loadTestAggregateView.setRespMin(1);
        loadTestAggregateView.setRespMedian(2);
        loadTestAggregateView.setRespPct75(2);
        loadTestAggregateView.setRespPct90(2);
        loadTestAggregateView.setRespStddev(1.0);
        loadTestAggregateView.setTotalBytesSent(1l);
        loadTestAggregateView.setTotalBytesReceived(1l);
        loadTestAggregateView.setErrorCount(0);

        return loadTestAggregateView;
    }

    private LoadTest buildLoadTest() {
        LoadTest loadTest = new LoadTest();
        loadTest.setId(4888);
        loadTest.setAppUnderTest("form-svc");
        loadTest.setAppUnderTestVersion("N/A");
        loadTest.setEnvironment("s1");
        loadTest.setStartTime(1l);
        loadTest.setEndTime(2l);
        loadTest.setTestName("form-svc.api.regression.s1");
        loadTest.setTestSubName("");
        loadTest.setTestTool("SOASTA Cloud Test");
        loadTest.setTestToolVersion("7732.159");
        loadTest.setVuserCount(10);
        loadTest.setSlaGroupId(46);
        loadTest.setExternalTestId("21575");

        return loadTest;
    }

    private LoadTestTimeChartData buildLoadTestTimeChartData() {
        LoadTestTimeChartData loadTestTimeChartData = new LoadTestTimeChartData();
        loadTestTimeChartData.setRespAvg(2);
        loadTestTimeChartData.setRespMax(3);
        loadTestTimeChartData.setRespMin(1);
        loadTestTimeChartData.setRespMedian(2);
        loadTestTimeChartData.setRespPct75(2);
        loadTestTimeChartData.setRespPct90(2);
        loadTestTimeChartData.setRespStddev(1);
        loadTestTimeChartData.setTotalBytesSent(1);
        loadTestTimeChartData.setTotalBytesReceived(1);
        loadTestTimeChartData.setTransactionName("Transaction Name");

        return loadTestTimeChartData;

    }

}
