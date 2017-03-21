package com.opp.service;

import com.opp.domain.WebPageTestResult;
import com.opp.domain.ux.WptNavCategory;
import com.opp.dto.ux.*;
import com.opp.dto.ux.couchdb.CouchDbActionResp;
import com.opp.dto.ux.couchdb.CouchDbViewResp;
import com.opp.dto.ux.couchdb.DeleteWptDocResp;
import com.opp.util.FileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

/**
 * Created by ctobe on 9/15/16.
 */
@Service
public class WptService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CouchDbService couchDbService;

    @Autowired
    private GraphiteService graphiteService;

    @Value("${opp.ux.dataStorePath}")
    private String uxDataStorePath;

    @Value("${opp.ux.wptUrl}")
    private String wptUrl;

    @Value("${opp.ux.ui.maxTestsToShow}")
    private int uiMaxTestsToShow;

    @Value("${opp.ux.ui.alwaysShowLastXNumTests}")
    private int uiAlwaysShowLastXNumTests;



     /**
     * Will store either a string or a json object
     * @param rawData
     */
    public CouchDbActionResp storeRawResults(JsonNode rawData){
        CouchDbActionResp delResp = null;
        try {
            delResp = couchDbService.addDoc("wpt-raw", rawData);
            if(!delResp.isOk()){
                log.error("Error adding document to coucnDBService.  Message: " + delResp.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return delResp;
        }
    }

    /**
     * Takes a wpt json object and trims the fat and stores a lightweight results object
     * @param wptRawObject
     * @return mixed
     */
    public CouchDbActionResp storeSummaryResults(JsonNode wptRawObject) throws Exception {

        ObjectNode dataNode = (ObjectNode) wptRawObject.get("data");

        long min =1000000000;
        int max = 0;
        String minId = "1";
        String maxId = "1";

        // figure out min and max runs
        JsonNode runsNode = dataNode.get("runs");
        Iterator<String> fieldIterator = runsNode.fieldNames();
        while(fieldIterator.hasNext()){
            String fieldName = fieldIterator.next();
            JsonNode run = runsNode.get(fieldName);
            // calculate total weight to decide which is the min and max run
            int weight = run.get("firstView").get("TTFB").asInt() + run.get("firstView").get("visualComplete").asInt() + run.get("firstView").get("SpeedIndex").asInt();
           // System.out.println(fieldName + ": " + weight);
            if (weight > max) {
                max = weight;
                maxId = fieldName;
            }
            if (weight < min) {
                min = Long.valueOf(min);
                minId = fieldName;
            }
        }
        //System.out.println("max: " + maxId);
       // System.out.println("min: " + minId);

        // get counts before deleting data
        dataNode.set("runCount", objectMapper.createObjectNode().numberNode(runsNode.size()));
        dataNode.set("requestCount", objectMapper.createObjectNode().numberNode(dataNode.at("/median/firstView/requests").size()));

        // create new min and max run objects and cleanup stuff i don't need in them
        // need to copy these now since I will be deleting the runs object later to save lots of space
        if(dataNode.get("successfulFVRuns").asInt() > 2){
            dataNode.set("max", cleanUpWptRun(runsNode.get(maxId)));
            dataNode.set("min", cleanUpWptRun(runsNode.get(minId)));
        }

        // delete stuff I don't need to save lots of space
        dataNode.set("average", cleanUpWptRun(dataNode.get("average")));
        dataNode.set("median", cleanUpWptRun(dataNode.get("median")));
        dataNode.remove("runs"); // huge savings

        // save the full data reference ID in case I ever need more info
        dataNode.set("rawDataId", wptRawObject.get("_id"));

        // add status code and status text
        dataNode.set("statusCode", wptRawObject.get("statusCode"));
        dataNode.set("statusText", wptRawObject.get("statusText"));

        CouchDbActionResp resp = null;

        try {
            resp = couchDbService.addDoc("wpt-summary", dataNode);
            if(!resp.isOk()){
                log.error("Error adding document to couchDBService.  Message: " + resp.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resp;
        }

    }


    private void sendDataToGraphite(JsonNode jsonObj) {
        List<String> graphiteData = new ArrayList<>();
        JsonNode dataNode = jsonObj.get("data");
        String testLabel = dataNode.get("label").asText();
        Long runDate = dataNode.at("/median/firstView/date").asLong();

        // iterate over values
        JsonNode firstViewNode = dataNode.at("/median/firstView");
        Iterator<String> fieldIterator = firstViewNode.fieldNames();
        while(fieldIterator.hasNext()){
            String metricName = fieldIterator.next();
            JsonNode metricNode = firstViewNode.get(metricName);

            // only load numeric values into graphite
            if(StringUtils.isNumeric(metricNode.asText())){
                if(metricName.equals("userTime")) metricName = "userTime.last";
                graphiteData.add(graphiteService.buildUxMessage(testLabel+"."+metricName, metricNode.asLong(), runDate));
            }
        }


        // values are created in storeSummaryResults method
        int runCount = dataNode.get("runCount").asInt();
        int requestCount = dataNode.get("requestCount").asInt();


        graphiteData.add(graphiteService.buildUxMessage(testLabel + "." + "runCount", runCount, runDate));
        graphiteData.add(graphiteService.buildUxMessage(testLabel + "." + "requestCount", requestCount, runDate));
        graphiteData.add(graphiteService.buildUxMessage(testLabel + "." + "network_bwDown", dataNode.get("bwDown").asLong(), runDate));
        graphiteData.add(graphiteService.buildUxMessage(testLabel + "." + "network_bwUp", dataNode.get("bwUp").asLong(), runDate));
        graphiteData.add(graphiteService.buildUxMessage(testLabel + "." + "network_latency", dataNode.get("latency").asLong(), runDate));
        graphiteData.add(graphiteService.buildUxMessage(testLabel + "." + "network_plr", dataNode.get("plr").asLong(), runDate));
        graphiteData.add(graphiteService.buildUxMessage(testLabel + "." + "successfulFVRuns", dataNode.get("successfulFVRuns").asLong(), runDate));
        graphiteService.logToGraphite(graphiteData);
    }


    private JsonNode cleanUpWptRun(JsonNode runNode)
    {
        // convert to object so I can delete stuff
        ObjectNode runNodeObj = (ObjectNode) runNode;

        // clean up min and max
        ObjectNode firstView = (ObjectNode)runNodeObj.get("firstView");
        firstView.remove("requests");
        firstView.remove("videoFrames");
        runNodeObj.set("firstView", firstView);

        if(runNodeObj.has("repeatView")) {
            ObjectNode repeatView = (ObjectNode) runNodeObj.get("repeatView");
            repeatView.remove("requests");
            repeatView.remove("videoFrames");
            runNodeObj.set("repeatView", repeatView);
        }

        return runNodeObj;
    }

    public List<DeleteWptDocResp> deleteByName(String name) throws UnirestException {
        CouchDbViewResp documentsByViewQuery = couchDbService.getDocumentsByViewQuery("wpt-summary/_design/labels/_view/list-data?key=\"" + name + "\"");
        return documentsByViewQuery.getRows().stream().map( r -> {
            String wptId = r.getValue().get("id").asText();
            String docId = r.getValue().get("_id").asText();
            try {
                return deleteById(wptId, docId);
            } catch (UnirestException e) {
                e.printStackTrace();
                // todo: handle this better
                return new DeleteWptDocResp();
            }
        }).collect(toList());
    }


    public DeleteWptDocResp deleteById(String wptId, String summaryDocId) throws UnirestException {
        JsonNode docSummary = couchDbService.getDocumentById("wpt-summary", summaryDocId);
        JsonNode docRaw = couchDbService.getDocumentById("wpt-raw", docSummary.asText("rawDataId"));
        CouchDbActionResp resSummary = couchDbService.deleteDoc("wpt-summary", docSummary.asText("_id"), docSummary.asText("_rev"));
        CouchDbActionResp resRaw = couchDbService.deleteDoc("wpt-raw", docRaw.asText("_id"), docRaw.asText("_rev"));

        DeleteWptDocResp deleteWptDocResp = new DeleteWptDocResp();

        // todo: figure out what this is
       if(resSummary.isOk()){
           deleteWptDocResp.setSummaryData(true);
           deleteWptDocResp.setSummaryResp(resSummary);
       }

        if(resSummary.isOk()){
            deleteWptDocResp.setRawData(true);
            deleteWptDocResp.setRawDataResp(resRaw);
        }

        if(deleteFiles(wptId)){
            deleteWptDocResp.setFiles(true);
        }

        return deleteWptDocResp;

    }


    public boolean deleteFiles(String wptId)
    {
        try {
            int fileCount = 0;
            int deletedCount = 0;
            List<String> filesList = FileUtil.getFilesFromDirectoryRecusively(uxDataStorePath + "results/", "glob:" + wptId + ".*");
            for(String filePath : filesList){
                if(Files.deleteIfExists(Paths.get(filePath))){
                    deletedCount++;
                }
                fileCount++;
            }
            return (fileCount == deletedCount && fileCount > 0); // if i deleted all the files i should have
        } catch (Exception $e) {
            return false;
        }
    }

    public int deleteBadRuns() throws UnirestException {
        CouchDbViewResp docs = couchDbService.getDocumentsByViewQuery("wpt-summary/_design/cleanup/_view/bad-tests");
        int count = 0;
        for(CouchDbViewResp.Row doc : docs.getRows()) {
            DeleteWptDocResp deleteWptDocResp = deleteById(doc.getKey().toString(), doc.getId());
            if(deleteWptDocResp.isSummaryData()){
                count++;
            }
        }
        return count;
    }


    @Cacheable(value="wptTrendData", key="{#testName, #view, #runDuration, #isUserTimingBaseLine}")
    public WptTrendDataResp getTrendDataForTest(String testName, String view, String runDuration, boolean isUserTimingBaseLine) throws UnirestException {
        // view is either repeatView or firstView

        CouchDbViewResp resp = couchDbService.getDocumentsByViewQuery("/wpt-summary/_design/labels/_view/list-data?key=%22" + testName + "%22");

        // create a map of Map<key, TreeMap<runDate, wptData>
        Map<String, Map<Long, JsonNode>> wptPages = resp.getRows().stream()
                .collect(
                        groupingBy(r -> r.getKey().toString(), TreeMap::new,
                                toMap(r -> r.getValue().get("completed").asLong(), CouchDbViewResp.Row::getValue, (v1,v2)->v1, TreeMap::new)));


        String defaultRunDuration = "median";
        List<String> validRunDurations = Arrays.asList("median", "min", "max", "average");

        // set or override run duration if its invalid
        if (!validRunDurations.contains(runDuration))
            runDuration = defaultRunDuration;  // if the value doesn't exist, set to default


        List<Map<String, List<Long>>> userTimingsTempList = new ArrayList<>();
        List<Map<String, List<Long>>> userTimingsRangeTempList = new ArrayList<>();

        WptTrendDataResp wptTrendDataResp = new WptTrendDataResp();

        for (Map.Entry<String, Map<Long, JsonNode>> page : wptPages.entrySet()) {
            String pageName = page.getKey();
            if (pageName.contains(testName)) {

                // get page data
                Map<Long, JsonNode> pageData = page.getValue();

                // reduce records for easy UI display
                pageData = reduceRecords(pageData, uiMaxTestsToShow, uiAlwaysShowLastXNumTests);


                for(JsonNode dataByDate : pageData.values()){
                    // there may not always be a min and max if only one iteration was run.  Default to median since that will be the 1st run.
                    String minKey = "min";
                    String maxKey = "max";
                    if(!dataByDate.has("max")) {
                        runDuration = defaultRunDuration;
                        minKey = defaultRunDuration;
                        maxKey = defaultRunDuration;
                    }

                    if(!dataByDate.has(runDuration) || !dataByDate.get(runDuration).has(view)) {
                        continue; // skip this as this is null and we don't have data
                    }

                    // get testData based on the runDuration (i.e. median) and view (i.e. firstView)
                    JsonNode testData = dataByDate.at(String.format("/%s/%s", runDuration, view));

                    long timestamp = dataByDate.get("completed").asLong() * 1000; // convert to milli for JS

                    // set test run: data for datatable list of test info in the UI
                    wptTrendDataResp.getDataTable().add(getTestRunData(dataByDate, testData, pageName, timestamp)); // add data to response object


                    // --- set main chart data ---
                    wptTrendDataResp = setChartData(wptTrendDataResp, dataByDate, testData, timestamp, minKey, maxKey, view);


                    // --- set custom user timing chart data ---
                    Map<String, Map<String, List<Long>>> customUserTimingData = getCustomUserTimingData(dataByDate, testData, isUserTimingBaseLine, minKey, maxKey, view, timestamp);
                    // add user timing maps list
                    userTimingsTempList.add(customUserTimingData.get("timings"));
                    userTimingsRangeTempList.add(customUserTimingData.get("rangeTimings"));

                }

            }
        }

        // merge user timings and add to response object --- not this will probably only work right for one test
        wptTrendDataResp.getChart().setUserTimings(mergeUserTimings(userTimingsTempList));
        wptTrendDataResp.getChart().setUserTimingsRange(mergeUserTimings(userTimingsRangeTempList));

        System.out.println(wptTrendDataResp);
        return wptTrendDataResp;
    }

    /**
     * Get the custom user timings.
     * @param dataByDate
     * @param testData
     * @param isUserTimingBaseLine
     * @param minKey
     * @param view
     * @param timestamp
     * @return Hashmap with "timings" and "rangeTimings"
     */
    private Map<String, Map<String, List<Long>>> getCustomUserTimingData(JsonNode dataByDate, JsonNode testData, boolean isUserTimingBaseLine, String minKey, String maxKey, String view, long timestamp) {
        JsonNode viewDataNode = dataByDate.at("/" + minKey + "/" + view);

        // if isUserTimingBaseLine is set, recalculate the userTimingsMin baseline
        long userTimingsMin = calculateMinUserTimings(viewDataNode, testData, isUserTimingBaseLine);

        // build user timings array
        Map<String, List<Long>> userTimingsRange = new HashMap<>();
        Map<String, List<Long>> userTimings = new HashMap<>();
        Iterator<String> fieldIterator = viewDataNode.fieldNames();
        while(fieldIterator.hasNext()){
            String fieldName = fieldIterator.next();
            if(fieldName.startsWith("userTime.")){
                String userTimingName = fieldName.replace("userTime.", "");
                userTimingsRange.put(userTimingName, Arrays.asList(timestamp, dataByDate.at(String.format("/%s/%s/%s", minKey, view, fieldName)).asLong(), dataByDate.at(String.format("/%s/%s/%s", maxKey, view, fieldName)).asLong()));
                try {
                    userTimings.put(userTimingName, Arrays.asList(timestamp, testData.get(fieldName).asLong() - userTimingsMin));
                } catch (Exception ex) {
                    // not sure why this happens, but sometimes the selected view data like median doesn't have the user timing
                    System.out.printf("User timing '%s' missing from select run ('%s') data%n", userTimingName, view);
                }
            }
        }

        Map<String, Map<String, List<Long>>> userTimingMap = new HashMap<>();
        userTimingMap.put("timings", userTimings);
        userTimingMap.put("rangeTimings", userTimingsRange);
        return userTimingMap;
    }

    /**
     * Set chart data in WptTrendDataResp object and return
     * @param wptTrendDataResp
     * @param dataByDate
     * @param testData
     * @param timestamp
     * @param minKey
     * @param maxKey
     * @param view
     * @return
     */
    private WptTrendDataResp setChartData(WptTrendDataResp wptTrendDataResp, JsonNode dataByDate, JsonNode testData, long timestamp, String minKey, String maxKey, String view) {
        // Time to First Byte line chart plus min and max range for the chart background
        wptTrendDataResp.getChart().getTtfbRange().add(Arrays.asList(timestamp, dataByDate.at(String.format("/%s/%s/TTFB", minKey, view)).asLong(), dataByDate.at(String.format("/%s/%s/TTFB", maxKey, view)).asLong()));
        wptTrendDataResp.getChart().getTtfbLine().add(Arrays.asList(timestamp, testData.get("TTFB").asLong()));
        // Visually Complete line chart plus min and max range for the chart background
        wptTrendDataResp.getChart().getVcRange().add(Arrays.asList(timestamp, dataByDate.at(String.format("/%s/%s/visualComplete", minKey, view)).asLong(), dataByDate.at(String.format("/%s/%s/visualComplete", maxKey, view)).asLong()));
        wptTrendDataResp.getChart().getVcLine().add(Arrays.asList(timestamp, testData.get("visualComplete").asLong()));
        // Speed Index line chart plus min and max range for the chart background
        wptTrendDataResp.getChart().getSiRange().add(Arrays.asList(timestamp, dataByDate.at(String.format("/%s/%s/SpeedIndex", minKey, view)).asLong(), dataByDate.at(String.format("/%s/%s/SpeedIndex", maxKey, view)).asLong()));
        wptTrendDataResp.getChart().getSiLine().add(Arrays.asList(timestamp, testData.get("SpeedIndex").asLong()));

        return wptTrendDataResp;
    }

    /**
     * Build and return TestRunData object for a UX test.  This is the data displayed in the grid on a test trend page.
     * @param dataByDate
     * @param testData
     * @param pageName
     * @param timestamp
     * @return
     */
    private TestRunData getTestRunData(JsonNode dataByDate, JsonNode testData, String pageName, long timestamp) {
        // data for the data table
        TestRunData testRunData = new TestRunData();
        testRunData.set_id(dataByDate.get("_id").asText());
        testRunData.setPage(pageName);
        testRunData.setRev(dataByDate.get("_rev").asText());
        testRunData.setTimestamp(new SimpleDateFormat("yyyy-MM-dd H:m").format(new Date(timestamp)));
        testRunData.setWptId(dataByDate.get("id").asText());
        testRunData.setConnectivity(dataByDate.get("connectivity").asText());
        testRunData.setViewData(testData);
        return testRunData;
    }

    /**
     * When using the userTimingBaseLine option, this will return the min custom user timing to subtract from the rest of the user timings to baseline it at 0.
     * @param viewDataNode
     * @param testData
     * @param isUserTimingBaseLine
     * @return
     */
    private long calculateMinUserTimings(JsonNode viewDataNode, JsonNode testData, boolean isUserTimingBaseLine) {
        Iterator<String> fieldIterator = viewDataNode.fieldNames();
        long userTimingsMin = 10000000; // init to a crazy max value so I can derive it

        // find the min value for each one so if needed, I can baseline
        // this works great for cases where you want to track from the initial user timing or series of events without pre-loading of other stuff interfer with times
        if(isUserTimingBaseLine){
            while(fieldIterator.hasNext()){
                String fieldName = fieldIterator.next();
                if(fieldName.startsWith("userTime.")){
                    Long userTimingValue = testData.get(fieldName).asLong();
                    if(userTimingsMin > userTimingValue){
                        userTimingsMin = userTimingValue;
                    }
                }
            }
        }
        if(userTimingsMin == 10000000) userTimingsMin = 0;  // didn't find any user timings... set to 0.
        return userTimingsMin;
    }

    /**
     * Convert user timings to a map joining all the List<Long> to a List of List<Long>
     * @param timingsList
     * @return
     */
    private Map<String,List<List<Long>>> mergeUserTimings(List<Map<String, List<Long>>> timingsList){
        return timingsList.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(toMap(
                        Map.Entry::getKey,
                        e -> Collections.singletonList(e.getValue()),
                        (currentList, newList) -> {
                            List<List<Long>> merged = new ArrayList<>();
                            merged.addAll(currentList);
                            merged.addAll(newList);
                            return merged;
                        },
                        TreeMap::new
                ));
    }


    /**
     * The UI will struggle with too much data.  Reduce the records to protect the UI and make it fast
     * @param pageData
     * @return
     */
    private Map<Long, JsonNode> reduceRecords(Map<Long, JsonNode> pageData, int maxRecordsToShow, int lastXRecordsToAlwaysShow){

        int records = pageData.size();

        AtomicInteger iter = new AtomicInteger(-1);
        // set trim factor so I know what to divide by to slice up the records
        // get the delta of (maxRecordsToShow-lastXRecordsToAlwaysShow) cause i need to account for the bottom X records i'm showing
        long trimFactor = Math.round((double)records / (maxRecordsToShow-lastXRecordsToAlwaysShow));

        // start to filter if records are greater than 50
        if(records > maxRecordsToShow){
            pageData = pageData.entrySet().stream().filter( r -> {
                iter.incrementAndGet(); // increment
                // reduce records.  Always show the last 20 tests
                System.out.println(iter.get() + " at factor " + trimFactor + " = " + (iter.get() % trimFactor == 0 || iter.get() >= (records - lastXRecordsToAlwaysShow)));

                return (iter.get() % trimFactor == 0 || iter.get() >= (records - lastXRecordsToAlwaysShow));
            }).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (v1,v2)->v1, TreeMap::new));
        }

        return pageData;
    }


    /**
     * Import test from WPT
     * @param id
     * @param sendToGraphite
     * @return
     * @throws Exception
     */
    public WptImportResult importTest(String id, boolean sendToGraphite) throws Exception {
        log.info("======= New Test ID: " + id);

        // write queue file
        Path queueFilePath = Paths.get(uxDataStorePath + "queue/" + id);
        Files.write(queueFilePath, Collections.emptyList());
        log.info("File moved to queue");

        // set urls
        String jsonUrl = String.format("%s/jsonResult.php?test=%s", wptUrl, id);
        String harUrl = String.format("%s/export.php?test=%s", wptUrl, id);
        String resultFileName = String.format("%sresults/%s", uxDataStorePath, id);

        // get json results from wpt
        String jsonContents = Unirest.get(jsonUrl).asString().getBody();
        // save to local file
        FileUtil.writeToFile(resultFileName + ".json", jsonContents);
        // gzip file
        FileUtil.gzipFile(resultFileName + ".json", resultFileName + ".json.gz");
        Files.deleteIfExists(Paths.get(resultFileName + ".json")); // delete original
        log.info("Got json results");

        // grab har file contents
        String harContents = Unirest.get(harUrl).asString().getBody();
        // save to local file
        FileUtil.writeToFile(resultFileName + ".har", harContents);
        // gzip file
        FileUtil.gzipFile(resultFileName + ".har", resultFileName + ".har.gz");
        Files.deleteIfExists(Paths.get(resultFileName + ".har")); // delete original
        log.info("Got har file");

        // push json to couch db
        JsonNode jsonNodeWptResults = objectMapper.readTree(jsonContents);
        CouchDbActionResp summaryResults = storeSummaryResults(jsonNodeWptResults);
        CouchDbActionResp rawResults = storeRawResults(jsonNodeWptResults);
        log.info("UX Summary Results: " + summaryResults.isOk());
        log.info("UX Raw Results: " + rawResults.isOk());
        log.info("Pushed to couchdb");

        // send to graphite
        if(sendToGraphite) {
            sendDataToGraphite(jsonNodeWptResults);
        }

        // mark this processed and move to different directory
        Path processedFilePath = Paths.get(uxDataStorePath + "processed/" + id);
        try {
            Files.move(queueFilePath, processedFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex){
            log.error("Error moving file - " + queueFilePath + "\n Error: " + ex.getMessage());
        }
        log.info("Marked as processed");

        return new WptImportResult(summaryResults, rawResults);
    }

    /**
     * Validate SLAs
     * @param slas
     * @param doc
     * @return
     * @throws JsonProcessingException
     */
    public WptSlaResults validateSlas(JsonNode slas, JsonNode doc) throws JsonProcessingException {
        // flatten json to maps to make this easier to compare
        Map<String, Object> slaFlatMap = JsonFlattener.flattenAsMap(objectMapper.writeValueAsString(slas));
        Map<String, Object> docFlatMap = JsonFlattener.flattenAsMap(objectMapper.writeValueAsString(doc));

        // init return objects
        WptSlaResults wptSlaResults = new WptSlaResults();
        List<WptSlaResults.WptSlaResultDetails> wptSlaResultDetailsList = new ArrayList<>();

        // int counters to 0
        int totalPassed = 0;
        int totalFailed = 0;

        // loop through all SLAs
        for(Map.Entry<String, Object> slaEntry : slaFlatMap.entrySet()){
            String slaName = slaEntry.getKey();
            Integer slaValue = Integer.valueOf(slaEntry.getValue().toString());
            Integer docValue = Integer.valueOf(docFlatMap.get(slaName).toString());

            // build sla details object
            WptSlaResults.WptSlaResultDetails slaDetails = new WptSlaResults.WptSlaResultDetails();
            slaDetails.setName(slaName);
            slaDetails.setSla(slaValue);
            if(docValue != null) {
                // doc value exists, so calculate sla results
                slaDetails.setActual(docValue);
                slaDetails.setDiff(docValue - slaValue);
                slaDetails.setDiffPct(((double)Math.round(((double) slaDetails.getDiff() / slaDetails.getActual()) * 100)*100)/100); // * 100 to move to a percent and * 100 so i can found and later divide
                // todo: some slas may be higher to pass.  may need a way to figure this out.  currently only lower values pass slas.  So an sla on # of test runs completed, won't really work right now.
                slaDetails.setSlaPass(slaDetails.getDiff() <= 0); // sla will pass if the difference is below or equal to 0.  meaning it was under the sla.  NOTE: this could have a limitation on SLAs that are supposed to be higher.
            } else {
                // default to null or false since there are no slas
                slaDetails.setActual(null);
                slaDetails.setDiff(null);
                slaDetails.setSlaPass(false);
            }

            // count pass and fails
            if(slaDetails.isSlaPass()){
                totalPassed++;
            } else {
                totalFailed++;
            }
            // add to list
            wptSlaResultDetailsList.add(slaDetails);
        }

        // build return object and return
        wptSlaResults.setTotalPass(totalPassed);
        wptSlaResults.setTotalFailed(totalFailed);
        wptSlaResults.setTotalSlas(slaFlatMap.size());
        wptSlaResults.setTotalPassPct(((double)Math.round(((double)totalPassed/slaFlatMap.size()) * 100 * 100))/100);
        wptSlaResults.setSlaDetails(wptSlaResultDetailsList);
        return wptSlaResults;
    }


    /**
     * Get WPT navigation options
     * @return
     */
    @Cacheable("wptNavCategories")
    public Map<String, List<WptNavCategory>> getCategories() {

        Map<String, List<WptNavCategory>> navCategories = new HashMap<>();
        CouchDbViewResp documentsByViewQuery;
        try {
            documentsByViewQuery = couchDbService.getDocumentsByViewQuery("wpt-summary/_design/categories/_view/list?group=true");

            navCategories = documentsByViewQuery.getRows().stream().
                    map(r -> objectMapper.convertValue(r.getKey(), WptNavCategory[].class)[0]).collect( // map out the key object which is WptNavCategory
                    groupingBy(WptNavCategory::getApp, TreeMap::new, // group by app
                            collectingAndThen(toList(), (List<WptNavCategory> l) -> { l.sort(comparing(WptNavCategory::getPage)); return l; }))); // toList and sort the list of apps in each list

        } catch (UnirestException ex){
            ex.printStackTrace();
            log.error("Failed to get categories from couchdb");
        }

        return navCategories;
    }

    /**
     * Get WPT navigation options
     * @return
     */
    @Cacheable("wptNavCategoriesV2")
    public List<WptNavApp> getCategoriesV2() {

        List<WptNavApp> wptNavApps = new ArrayList<>();
        CouchDbViewResp documentsByViewQuery;
        try {
            documentsByViewQuery = couchDbService.getDocumentsByViewQuery("wpt-summary/_design/categories/_view/list?group=true");

            TreeMap<String, List<WptNavCategory>> navCats = documentsByViewQuery.getRows().stream().
                    map(r -> objectMapper.convertValue(r.getKey(), WptNavCategory[].class)[0]).collect( // map out the key object which is WptNavCategory
                    groupingBy(WptNavCategory::getApp, TreeMap::new, // group by app
                            collectingAndThen(toList(), (List<WptNavCategory> l) -> {
                                l.sort(comparing(WptNavCategory::getPage)); // toList and sort the list of apps in each list
                                return l;
                            })));

            wptNavApps = navCats.entrySet().stream().map(r -> new WptNavApp(r.getKey(), r.getValue())).collect(toList());

        } catch (UnirestException ex){
            ex.printStackTrace();
            log.error("Failed to get categories from couchdb");
        }

        return wptNavApps;
    }

    /**
     * Gets data from all the wptJsonFiles stored in the system.  Alternative to using CouchDB.  Don't think this is still used.
     * @param dir
     * @return
     */
    public Map<String, Map<Long, WebPageTestResult>> getWptJsonFiles(String dir){

        List<String> files = FileUtil.getFilesFromDirectoryRecusively(dir, "glob:json.gz");

        Map<String, Map<Long, WebPageTestResult>> pages = new HashMap<>();
        //Map<String, String> wptFiles = new HashMap<>();

        files.forEach(filePath -> {
            try {
                String fileContents = FileUtil.readGzipFile(Paths.get(filePath)).collect(Collectors.joining());
                //String fileContents = new String(Files.readAllBytes(Paths.get(filePath)));
                JsonNode jsonNode = objectMapper.valueToTree(fileContents);

                if(jsonNode.has("data") && jsonNode.get("data").has("label")) {
                    JsonNode dataNode = jsonNode.get("data");
                    JsonNode firstView = dataNode.get("runs").get("1").get("firstView");

                    WebPageTestResult wptRes = new WebPageTestResult();
                    wptRes.setUrl(firstView.get("URL").asText());
                    wptRes.setLabel(dataNode.get("label").asText());
                    wptRes.setCompleted(dataNode.get("completed").asLong());
                    wptRes.setId(dataNode.get("id").asText());
                    wptRes.setWpt_summary(dataNode.get("summary").asText());
                    wptRes.setStatusCode(dataNode.get("statusCode").asInt());
                    wptRes.setLocation(dataNode.get("location").asText());
                    wptRes.setConnectivity(dataNode.get("connectivity").asText());
                    wptRes.setLoadTime(firstView.get("loadTime").asInt());
                    wptRes.setTtfb(firstView.get("ttfb").asInt());
                    wptRes.setFullyLoaded(firstView.get("fullyLoaded").asInt());
                    wptRes.setLastVisualChange(firstView.get("lastVisualChange").asInt());
                    wptRes.setSpeedIndex(firstView.get("SpeedIndex").asInt());
                    wptRes.setVisualComplete(firstView.get("visualComplete").asInt());
                    wptRes.setThumbs(objectMapper.convertValue(firstView.get("thumbnails"), WebPageTestResult.Images.class));
                    wptRes.setImages(objectMapper.convertValue(firstView.get("images"), WebPageTestResult.Images.class));
                    wptRes.setWpt_pages(objectMapper.convertValue(firstView.get("pages"), WebPageTestResult.WptPages.class));

                    // replaces
                    // $pages[$indexOn][$completed]['wpt_data'];   // indexOn is the label
                    Map<Long, WebPageTestResult> dataMap = new HashMap<>();
                    dataMap.put(wptRes.getCompleted(), wptRes);
                    pages.put(wptRes.getLabel(), dataMap);


                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        });

        return pages;
    }


}
