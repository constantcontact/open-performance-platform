package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.dto.datagen.DataGenRequest;
import com.opp.dto.datagen.DataGenResponse;
import com.opp.service.DataGenService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ctobe on 7/20/16.
 */
public class LoadTestAggregateControllerTest extends BaseIntegrationTest {

    @Autowired
    DataGenService dataGenService;

    @Test
    public void getAndPostAggData() throws Exception {

        // generate data set
        List<DataGenResponse> dataGenResponseList = createDataSet(true);

        // ======== GET aggData ==========
        HttpResponse<JsonNode> jsonAggResp = Unirest.get(getBaseUrl() + "/loadsvc/v1/loadtests/{load_test_id}/aggData")
                .routeParam("load_test_id", String.valueOf(dataGenResponseList.get(0).getLoadTestId())).asJson();
        if(!jsonAggResp.getBody().isArray()) throw new Exception("Failed to get correct response back");

        // validate response
        assertEquals("Load Test Id", dataGenResponseList.get(0).getLoadTestId(), jsonAggResp.getBody().getArray().getJSONObject(0).get("loadTestId"));
        assertEquals("Aggregated Transaction Count", dataGenResponseList.get(0).getTransactionBreakDown().size(), jsonAggResp.getBody().getArray().length());
        System.out.println("Resp Obj Array: " + jsonAggResp.getBody().getArray());


        // ======== POST aggData =========
        // build post of aggregate data
        int RECORDS_TO_CREATE = 20;
        List<JSONObject> jsonPost = IntStream.range(0, RECORDS_TO_CREATE).mapToObj(i -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("loadTestId", dataGenResponseList.get(0).getLoadTestId());
            jsonObject.put("transactionName", "Test Transaction - " + i);
            jsonObject.put("callCount", 5000);
            jsonObject.put("respAvg", 400);
            jsonObject.put("respMax", 5000);
            jsonObject.put("respMedian", 600);
            jsonObject.put("respMin", 50);
            jsonObject.put("respPct75", 700);
            jsonObject.put("respPct90", 1000);
            jsonObject.put("respStddev", 100);
            jsonObject.put("totalBytesReceived", 10000000);
            jsonObject.put("totalBytesSent", 10000000);
            jsonObject.put("tpsMedian", 200);
            jsonObject.put("tpsMax", 400);
            jsonObject.put("errorCount", 5);
            return jsonObject;
        }).collect(toList());

        // make call
        HttpResponse<JsonNode> jsonAggCreateResp = Unirest.post(getBaseUrl() + "/loadsvc/v1/loadtests/{load_test_id}/aggData")
                .routeParam("load_test_id", String.valueOf(dataGenResponseList.get(0).getLoadTestId()))
                .header("Content-Type", "application/json")
                .body(new JSONArray(jsonPost))
                .asJson();
        // validate
        JSONObject resp = jsonAggCreateResp.getBody().getObject();
        assertEquals("loadTestId", resp.get("loadTestId"), dataGenResponseList.get(0).getLoadTestId());
        assertEquals("total records", resp.get("totalRecords"), RECORDS_TO_CREATE);
        assertEquals("total created", resp.get("totalCreated"), RECORDS_TO_CREATE);

    }



    @Test
    public void aggregateLoadTest() throws Exception {

        // generate data --- NOT aggregated
        List<DataGenResponse> dataGenResponseList = createDataSet(false);

        // aggregate the data
        HttpResponse<JsonNode> jsonAggResp = Unirest.post(getBaseUrl() + "/loadsvc/v1/loadtests/{load_test_id}/aggregate")
                .routeParam("load_test_id", String.valueOf(dataGenResponseList.get(0).getLoadTestId())).asJson();

        // verify data is aggregated
        /* resp: { loadTestId, transactionsAggregated, startTime, endTime } */
        JSONObject obj = jsonAggResp.getBody().getObject();
        assertEquals("Load Test Id", dataGenResponseList.get(0).getLoadTestId(), obj.getInt("loadTestId"));
        assertEquals("Aggregated Transaction Count", dataGenResponseList.get(0).getTransactionBreakDown().size(), obj.getInt("transactionsAggregated"));
        assertTrue("End Time after Start Time", obj.getLong("endTime") > obj.getLong("startTime"));
    }

    /**
     * Generates a load test with data
     * @param aggregated
     * @return
     */
    private List<DataGenResponse> createDataSet(boolean aggregated){
        // add the data
        int loadTests = 1;
        int minRawData = 10;
        int maxRawData = 10;
        int minTrans = 2;
        int maxTrans = 2;
        DataGenRequest dataGenRequest = new DataGenRequest(loadTests, minTrans, maxTrans, minRawData, maxRawData, aggregated);
        return dataGenService.generateData(dataGenRequest);
    }

}