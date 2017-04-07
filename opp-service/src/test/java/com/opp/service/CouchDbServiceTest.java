package com.opp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.opp.BaseIntegrationTest;
import com.opp.domain.ux.WptResult;
import com.opp.domain.ux.WptTestLabel;
import com.opp.dto.ux.couchdb.CouchDbActionResp;
import com.opp.dto.ux.couchdb.CouchDbViewResp;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class CouchDbServiceTest extends BaseIntegrationTest {

    @Autowired
    private CouchDbService couchDbService;


    @Autowired
    ObjectMapper objectMapper;

    private static Gson gson = new Gson();

    @Test
    @Ignore
    public void crudTest() throws Exception {

        String dbName = "craig";

        // create database
        // todo: fix access errors
        HttpResponse<JsonNode> addDbResp = couchDbService.addDatabase(dbName);
        assertEquals("create db", 201, addDbResp.getStatus());

        HashMap<String, String> data = new HashMap<>();
        data.put("name", "craig");
        data.put("address", "123 lane");

        // add doc
        CouchDbActionResp createResp = couchDbService.addDoc(dbName, data);
        assertTrue("Add doc", createResp.isOk());

        JsonNode getResp = couchDbService.getDocumentById(dbName, createResp.getId());
        assertEquals("get doc", "craig", getResp.asText("name"));

        // update doc
        data.put("name", "john");
        // todo: fix 409 conflict error
        CouchDbActionResp updateResp = couchDbService.updateDocumentById(dbName, createResp.getId(), data);
        assertTrue("get doc", updateResp.isOk());

        // get doc
        getResp = couchDbService.getDocumentById(dbName, updateResp.getId());
        assertEquals("get doc", "john", getResp.asText("name"));

        System.out.println("Got doc");
        System.out.println(getResp);

        // get all docs
        CouchDbViewResp allDocs = couchDbService.getAllDocumentIds(dbName);
        assertTrue("getting all docs", allDocs.getRows().size() > 0);
        System.out.println(allDocs);


        // delete by rev
        System.out.println("deleting by rev");
        System.out.println(couchDbService.deleteDoc(dbName, createResp.getId(), createResp.getRev()));

        // delete by id
        // todo: fix 409 conflict
        System.out.println("deleting no rev");
        System.out.println(couchDbService.deleteDocNoRev(dbName, createResp.getId()));

    }


    @Test
    public void couchDBMigration(){
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
                com.google.gson.JsonObject  row = data.getAsJsonObject().get("value").getAsJsonObject();
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

                } catch (Exception ex){
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
                String createStatement = "{ \"create\" : {\"_index\": \"wpt-summary\", \"_type\":\"result\", \"_id\":\""+id+"\" } }\n";
                importList += createStatement + json + "\n";
                if(counter > 0 && counter % 200 == 0){
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

    private void bulkLoad(String postData){
        try {
            HttpResponse<String> res = Unirest.post("http://localhost:9200/_bulk").body(postData).asString();
            System.out.println(res.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void postToES(String id, String json){
        try {
            HttpResponse<String> res = Unirest.post("http://localhost:9200/wpt-summary/result/" + id).body(json).asString();
            System.out.println(res.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void writeJsonToFile(String fileName, String content){
        try(  PrintWriter out = new PrintWriter( fileName )  ){
            out.println( content );
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
     * @param dataNodeObject
     * @return
     */
    private com.google.gson.JsonObject setUserTimings(com.google.gson.JsonObject dataNodeObject){

        List<String> runs = Arrays.asList("min", "max", "average", "median");
        List<String> views = Arrays.asList("firstView", "repeatView");

        // iterate over runs then views to get down into the tree
        // ie   min -> firstView -> userTime
        for(String runName : runs) {
            if(dataNodeObject.has(runName) && !dataNodeObject.get(runName).isJsonNull()) {
                boolean runChanged = false;
                JsonObject runNodeObject = dataNodeObject.get(runName).getAsJsonObject();
                for(String viewName : views) {
                    if(runNodeObject.has(viewName) && !runNodeObject.get(viewName).isJsonNull()) {
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
                if(runChanged) {
                    dataNodeObject.add(runName, runNodeObject); // overwrite run name with new run value
                }
            }
        }
        return dataNodeObject;
    }



}
