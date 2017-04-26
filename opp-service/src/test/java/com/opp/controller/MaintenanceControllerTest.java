package com.opp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.opp.BaseIntegrationTest;
import com.opp.domain.CiLoadTestJob;
import com.opp.domain.ux.WptResult;
import com.opp.domain.ux.WptTestLabel;
import com.opp.dto.datagen.DataGenRequest;
import com.opp.dto.datagen.DataGenResponse;
import com.opp.service.DataGenService;
import com.opp.service.LoadTestAggregateService;
import com.opp.service.LoadTestDataService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ctobe on 6/30/16.
 */
public class MaintenanceControllerTest extends BaseIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataGenService dataGenService;

    @Autowired
    private LoadTestAggregateService loadTestAggregateService;

    @Autowired
    private LoadTestDataService loadTestDataService;

    @Autowired
    private Gson gson;


    @After
    public void tearDown() throws Exception {
        //int recordsDeleted = jdbcTemplate.update("delete from load_test where id = 1");
        //System.out.println("Deleted " + recordsDeleted + " records");
    }

    /**
     * Re-aggregate all data.  Don't run this on a large system or while other tests are running as it can break them
     *
     * @throws Exception
     */
    @Test
    @Ignore
    public void reAggregateData() throws Exception {

        // reaggregate data
        // Create - POST
        HttpResponse<JsonNode> jsonCreateResp = Unirest.post(getBaseUrl() + "/loadsvc/v1/maintenance/reaggregatedata").asJson();
        assertTrue("Reaggregate Data - Verifying Response Code", jsonCreateResp.getStatus() == 201);
        // could definitely addAll more tests here but this should really get run unless absolutely needed
    }

    @Test
    public void cleanApiLogs() throws Exception {

        // reaggregate data
        // Create - POST
        HttpResponse<JsonNode> jsonCleanLogsResp = Unirest.post(getBaseUrl() + "/loadsvc/v1/maintenance/cleanlogs").asJson();
        assertTrue("Clean Logs - Verifying Response Code", jsonCleanLogsResp.getStatus() == 202);

    }

    @Test
    public void deleteTransactions() throws Exception {
        // generate data
        int loadTests = 3, minRawData = 9, maxRawData = 9, minTrans = 3, maxTrans = 3;
        List<DataGenResponse> dataGenResponseList = dataGenService.generateData(new DataGenRequest(loadTests, minTrans, maxTrans, minRawData, maxRawData, true));

        // get generated test and data for later comparison
        DataGenResponse generatedTest = dataGenResponseList.get(0);
        int rawDataCount = generatedTest.getLoadTestData().size();
        int transactionCount = generatedTest.getTransactionBreakDown().size();
        String transNameToDelete = generatedTest.getTransactionBreakDown().entrySet().iterator().next().getKey();

        // build delete transaction request object
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("testName", generatedTest.getLoadTest().getTestName());
        jsonObj.put("transactionName", transNameToDelete);
        // make API call
        HttpResponse<JsonNode> jsonResp = Unirest.delete(getBaseUrl() + "/loadsvc/v1/maintenance/transaction").body(jsonObj).asJson();
        // verify response code
        assertTrue("Delete Transactions - Verifying Response Code", jsonResp.getStatus() == 202);

        // get response data
        int rawDataRowsRemoved = jsonResp.getBody().getObject().getInt("rawDataRowsRemoved");
        int aggregateDataRowsRemoved = jsonResp.getBody().getObject().getInt("aggregateDataRowsRemoved");

        // assert correct data was removed
        assertEquals("Delete Transactions - Raw data removed", 3, rawDataRowsRemoved);
        assertEquals("Delete Transactions - Aggregate data removed", 1, aggregateDataRowsRemoved);
        assertEquals("Delete Transactions - Total aggregates left in test", transactionCount - aggregateDataRowsRemoved, loadTestAggregateService.getByLoadTestId(generatedTest.getLoadTestId()).size());
        assertEquals("Delete Transactions - Total raw data left in test", rawDataCount - rawDataRowsRemoved, loadTestDataService.getAllByLoadTestId(generatedTest.getLoadTestId()).size());

    }

    /**
     * Cleans all the test data.  By default, do NOT run this test as it can break other tests as it deletes the data created by other tests
     *
     * @throws Exception
     */
    @Test
    @Ignore
    public void cleanTestData() throws Exception {
        // verify 202 response and baseline system at 0 tests
        HttpResponse<JsonNode> jsonResp = Unirest.delete(getBaseUrl() + "/loadsvc/v1/maintenance/cleanTestData").asJson();
        assertEquals("Maint. - Delete test data - Verify Response Code", 202, jsonResp.getStatus());
        System.out.println("Maint. - Number of tests deleted: " + jsonResp.getBody().getObject().getInt("testsDeleted"));

        // the 2nd call should result in 3 tests deleted
        int loadTests = 3, minRawData = 9, maxRawData = 9, minTrans = 3, maxTrans = 3;
        dataGenService.generateData(new DataGenRequest(loadTests, minTrans, maxTrans, minRawData, maxRawData, true));
        HttpResponse<JsonNode> jsonResp2 = Unirest.delete(getBaseUrl() + "/loadsvc/v1/maintenance/cleanTestData").asJson();
        assertEquals("Maint. - Delete test data - Verify deleted in response", 3, jsonResp2.getBody().getObject().getInt("testsDeleted"));

        // last call to make sure nothing is left
        HttpResponse<JsonNode> jsonResp3 = Unirest.delete(getBaseUrl() + "/loadsvc/v1/maintenance/cleanTestData").asJson();
        assertEquals("Maint. - Delete test data - Verify no tests left", 0, jsonResp3.getBody().getObject().getInt("testsDeleted"));
    }








    /***************** Migration Code from CouchDB *****************/
    @Test
    public void couchDBJenkinsMigration() {
        // pull data down first
        // curl -X GET http://{couchdburl}:5984/jenkins-soasta/_design/all_docs/_view/All%20Docs > /tmp/soasta.json
        try {
            String json = new String(Files.readAllBytes(Paths.get("/tmp/soasta.json")));
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("rows");
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jenkinsObj = jsonArray.getJSONObject(i).getJSONObject("value");
                JSONObject paramsObj = jenkinsObj.getJSONObject("params");
                paramsObj.put("test", jenkinsObj.getString("test"));
                if(paramsObj.has("test_type"))
                    paramsObj.put("test_type", jenkinsObj.getString("test_type"));
                CiLoadTestJob ciLoadTestJob = new CiLoadTestJob();
                if(paramsObj.has("app_under_test"))
                    ciLoadTestJob.setAppUnderTest(paramsObj.getString("app_under_test"));
                if(paramsObj.has("app_under_test_version"))
                    ciLoadTestJob.setAppUnderTestVersion(paramsObj.getString("app_under_test_version"));
                if(paramsObj.has("comments"))
                    ciLoadTestJob.setComments(paramsObj.getString("comments"));
                if(paramsObj.has("cron_schedule"))
                    ciLoadTestJob.setCronSchedule(paramsObj.getString("cron_schedule"));
                if(paramsObj.has("ct_additional_options"))
                    ciLoadTestJob.setCtAdditionalOptions(paramsObj.getString("ct_additional_options"));
                if(paramsObj.has("description"))
                    ciLoadTestJob.setDescription(paramsObj.getString("description"));
                if(paramsObj.has("environment"))
                    ciLoadTestJob.setEnvironment(Arrays.asList(paramsObj.getJSONArray("environment").getString(0)));
                if(paramsObj.has("host_name"))
                    ciLoadTestJob.setHostName(paramsObj.getString("host_name"));
                if(paramsObj.has("ramp_vuser_end_delay"))
                    ciLoadTestJob.setRampVuserEndDelay(getInt(paramsObj.getString("ramp_vuser_end_delay")));
                if(paramsObj.has("ramp_vuser_start_delay"))
                    ciLoadTestJob.setRampVuserStartDelay(getInt(paramsObj.getString("ramp_vuser_start_delay")));
                if(paramsObj.has("run_duration"))
                    ciLoadTestJob.setRunDuration(Long.valueOf(paramsObj.getString("run_duration"))/1000);
                if(paramsObj.has("sla_group_id"))
                    ciLoadTestJob.setSlaGroupId(getInt(paramsObj.getString("sla_group_id")));
                if(paramsObj.has("test"))
                    ciLoadTestJob.setTest(paramsObj.getString("test"));
                if(paramsObj.has("test_type"))
                    ciLoadTestJob.setTestType(paramsObj.getString("test_type"));
                if(paramsObj.has("test_name"))
                    ciLoadTestJob.setTestName(paramsObj.getString("test_name"));
                if(paramsObj.has("test_sub_name"))
                    ciLoadTestJob.setTestSubName(paramsObj.getString("test_sub_name"));
                if(paramsObj.has("test_data_type"))
                    ciLoadTestJob.setTestDataType(paramsObj.getString("test_data_type"));
                if(paramsObj.has("test_tool"))
                    ciLoadTestJob.setTestTool(paramsObj.getString("test_tool"));
                if(paramsObj.has("test_tool_version"))
                    ciLoadTestJob.setTestToolVersion(paramsObj.getString("test_tool_version"));
                if(paramsObj.has("vuser_count"))
                    ciLoadTestJob.setVuserCount(getInt(paramsObj.getString("vuser_count")));



                System.out.println(ciLoadTestJob);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    Integer getInt(String str){
        return (str.trim().isEmpty()) ? null : Integer.valueOf(str);

    }



    @Test
    @Ignore
    public void couchDBWptMigration() {
        // pull data down first
        // curl -X GET http://{couchdburl}:5984/wpt-summary/_design/wptid/_view/list-data > /tmp/docs.json
        try {
            parseAndLoadFile(Paths.get("/tmp/docs.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void parseAndLoadFile(Path filePath) throws IOException {
        InputStream in = new FileInputStream(filePath.toFile());
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.beginObject();
        int counter = 0;
        String importList = "";

        while (reader.hasNext()) {
            reader.nextName();
            reader.nextInt();
            reader.nextName();
            reader.nextInt();
            reader.nextName(); // should be at "rows"
            reader.beginArray();
            while (reader.hasNext()) {
                com.google.gson.JsonObject data = gson.fromJson(reader, com.google.gson.JsonObject.class);
                com.google.gson.JsonObject row = data.getAsJsonObject().get("value").getAsJsonObject();
                String id = row.get("id").getAsString();
                String json;

                try {
                    if (row.get("successfulFVRuns").getAsInt() == 0) {
                        continue; // bad test
                    }

                    if (row.get("successfulRVRuns").getAsInt() == 0) {
                        // for some reason WPT makes this an array.  seems like a bug cause median doesn't have a repeat view if there is none
                        row.get("average").getAsJsonObject().remove("repeatView");
                        row.get("standardDeviation").getAsJsonObject().remove("repeatView");
                    }

                } catch (Exception ex) {
                    // swallow
                    // no idea, but gotten a null pointer here.  Might just be a bad all around test
                    continue;
                }

                try {
                    WptResult wptResult = convertToWptResult(row);
                    json = gson.toJson(wptResult);
                } catch (Exception ex) {
                    System.out.println("invalid row");
                    System.out.println(row);
                    continue;
                }
                //writeJsonToFile("/tmp/jsonfiles/" + id + ".json", json);

//
                String createStatement = "{ \"create\" : {\"_index\": \"wpt-summary\", \"_type\":\"result\", \"_id\":\"" + id + "\" } }\n";
                importList += createStatement + json + "\n";
                if (counter > 0 && counter % 200 == 0) {
                    bulkLoad(importList);
                    importList = ""; // reset
                }

                // load single item
                // postToES(id, json);


                counter++;

            }
            bulkLoad(importList);
            reader.endArray();
        }
        reader.endObject();
        reader.close();
        in.close();
    }

    private void bulkLoad(String postData) {
        try {
            HttpResponse<String> res = Unirest.post("http://localhost:9200/_bulk").body(postData).asString();
            System.out.println(res.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void postToES(String id, String json, String index, String type) {
        try {
            HttpResponse<String> res = Unirest.post("http://localhost:9200/"+index+"/"+type+"/" + id).body(json).asString();
            System.out.println(res.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void writeJsonToFile(String fileName, String content) {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private WptResult convertToWptResult(com.google.gson.JsonObject wptData) {
        // convert label to new format
        WptTestLabel wptTestLabel = new WptTestLabel(wptData.get("label").getAsString());
        wptData.remove("label");

        // set user timings
        wptData = setUserTimings(wptData);

        WptResult wptResult = gson.fromJson(wptData, WptResult.class);
        wptResult.setLabel(wptTestLabel);

        return wptResult;
    }

    /**
     * Detect all users timings and add them to the userTimes object in the view while also replacing dots with underscores
     *
     * @param dataNodeObject
     * @return
     */
    private com.google.gson.JsonObject setUserTimings(com.google.gson.JsonObject dataNodeObject) {

        List<String> runs = Arrays.asList("min", "max", "average", "median");
        List<String> views = Arrays.asList("firstView", "repeatView");

        // iterate over runs then views to get down into the tree
        // ie   min -> firstView -> userTime
        for (String runName : runs) {
            if (dataNodeObject.has(runName) && !dataNodeObject.get(runName).isJsonNull()) {
                boolean runChanged = false;
                JsonObject runNodeObject = dataNodeObject.get(runName).getAsJsonObject();
                for (String viewName : views) {
                    if (runNodeObject.has(viewName) && !runNodeObject.get(viewName).isJsonNull()) {
                        JsonObject viewNodeObject = runNodeObject.get(viewName).getAsJsonObject();

                        Map<String, Long> userTimes = new HashMap<>();

                        if (viewNodeObject.has("userTimes")) {
                            // --- newer WPT runs have a userTimes object
                            // replace all the dots in the hashmap keys with _ by streaming to a new map
                            userTimes = viewNodeObject.get("userTimes").getAsJsonObject().entrySet().stream()
                                    .collect(Collectors.toMap((m) -> m.getKey().replace(".", "_"), (m) -> m.getValue().getAsNumber().longValue()));
                        } else {
                            // -- supporting older runs without this userTimes object
                            // if it doesn't have a userTimes object, scan for "userTime." and build hashmap of all those values
                            userTimes = viewNodeObject.entrySet().stream()
                                    .filter((m) -> m.getKey().startsWith("userTime.")) // make sure it starts with userTime
                                    .collect(Collectors.toMap((m) -> m.getKey().replace("userTime.", "").replace(".", "_"), (m) -> m.getValue().getAsNumber().longValue()));
                        }
                        if (userTimes.size() > 0) {
                            // if values were set, add a thew userTimes object to the runViewNode
                            viewNodeObject.add("userTimes", gson.toJsonTree(userTimes)); // overwrite userTimes with new value
                            runNodeObject.add(viewName, viewNodeObject); // overwrite view with new values
                            runChanged = true;
                        }
                    }
                }
                if (runChanged) {
                    dataNodeObject.add(runName, runNodeObject); // overwrite run name with new run value
                }
            }
        }
        return dataNodeObject;
    }

}