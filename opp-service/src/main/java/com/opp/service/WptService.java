package com.opp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.mashape.unirest.http.Unirest;
import com.opp.dao.WptTestDao;
import com.opp.domain.ux.WptResult;
import com.opp.domain.ux.WptTestImport;
import com.opp.domain.ux.WptTestLabel;
import com.opp.domain.ux.WptUINavigation;
import com.opp.dto.ux.WptDeleteResp;
import com.opp.dto.ux.WptSlaResults;
import com.opp.dto.ux.WptTestRunData;
import com.opp.dto.ux.WptTrendMetric;
import com.opp.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toMap;

/**
 * Created by ctobe on 9/15/16.
 */
@Service
public class WptService {

   private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WptTestDao dao;
    @Autowired
    private ObjectMapper objectMapper;
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

    @Value("${opp.elasticsearch.apiUrl}")
    private String esApiUrl;


    // ========= CRUD Operations ==========


    /**
     * Import test from JSON
     * @param json
     * @param wptLabel
     * @param sendToGraphite
     * @return
     * @throws Exception
     */
    public Optional<WptResult> importTestFromJson(String json, WptTestLabel wptLabel, boolean sendToGraphite) throws Exception {
        return importTest(objectMapper.readTree(json), wptLabel, sendToGraphite);
    }


    /**
     * Import test from WPT
     * @param wptTestImport
     * @param sendToGraphite
     * @return
     * @throws Exception
     */
    public Optional<WptResult> importTestFromWpt(WptTestImport wptTestImport, boolean sendToGraphite) throws Exception {

        // set urls
        String jsonUrl = String.format("%s/jsonResult.php?test=%s", wptUrl, wptTestImport.getWptId());
        String harUrl = String.format("%s/export.php?test=%s", wptUrl, wptTestImport.getWptId());
        String resultFileName = String.format("%sresults/%s", uxDataStorePath, wptTestImport.getWptId());

        // get json results from wpt
        String jsonContents = Unirest.get(jsonUrl).asString().getBody();

        // push json elasticsearch
        JsonNode jsonNodeWptResults = objectMapper.readTree(jsonContents);
        return importTest(jsonNodeWptResults, new WptTestLabel(wptTestImport.getWptTestLabel()));
    }


    /**
     * Base import test method
     * @param jsonNodeWptResults
     * @param wptTestLabel
     * @param sendToGraphite
     * @return
     * @throws Exception
     */
    public Optional<WptResult> importTest(JsonNode jsonNodeWptResults, WptTestLabel wptTestLabel, boolean sendToGraphite) throws Exception {

        Optional<WptResult> wptResult = storeRawWptResults(jsonNodeWptResults, wptTestLabel);

//        // send to graphite
//        if(sendToGraphite) {
//            sendDataToGraphite(jsonNodeWptResults);
//        }

        return wptResult;
    }
    public Optional<WptResult> importTest(JsonNode jsonNodeWptResults, WptTestLabel wptTestLabel) throws Exception {
        return importTest(jsonNodeWptResults, wptTestLabel, false);
    }

    public Optional<WptResult> add(WptResult wptResult) {
        dao.insert(wptResult);
        return dao.findById(wptResult.getId());
    }

    public Optional<WptResult> update(String wptId, WptResult wptResult) throws ExecutionException, InterruptedException {
        dao.update(wptResult);
        return dao.findById(wptResult.getId());
    }

    public WptDeleteResp deleteById(String wptTestId) {
        return new WptDeleteResp(dao.deleteById(wptTestId));
    }

    public Optional<WptResult> getById(String wptTestId) {
        return dao.findById(wptTestId);
    }

    public List<WptResult> getByLabel(String labelName) {
        return dao.findByLabel(labelName);
    }

    public List<WptResult> getAll(String esSearchQuery) {
        return dao.findAll(esSearchQuery);
    }

    public WptDeleteResp deleteByLabel(String testName) {
        List<WptResult> wptResults = getByLabel(testName);
        AtomicInteger counter = new AtomicInteger(0);
        wptResults.forEach(t -> {
            if (deleteById(t.getId()).getDeleteCount() >= 1) {
                counter.getAndIncrement();
            }
        });
        return new WptDeleteResp(counter.get());
    }

    // ========== END CRUD =============





    public List<WptUINavigation> getNavigation() {
        return dao.getNavigation();
    }


    /**
     * Takes a wpt json object and trims the fat and stores a lightweight results object
     * @param wptRawObject
     * @return mixed
     */
    public Optional<WptResult> storeRawWptResults(JsonNode wptRawObject, WptTestLabel wptTestLabel) throws Exception {

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
        dataNode.remove("label");  // need to delete cause its replaced with a better label

        WptResult wptResult = objectMapper.convertValue(dataNode, WptResult.class);

        // add status code and status text
        wptResult.setStatusCode(wptRawObject.get("statusCode").asText());
        wptResult.setStatusText(wptRawObject.get("statusText").asText());
        wptResult.setLabel(wptTestLabel);


        return add(wptResult);

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


        // values are created in storeRawWptResults method
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

        // clean delete unneeded stuff to maybe make serialization faster
        ObjectNode firstView = (ObjectNode)runNodeObj.get("firstView");
        if(firstView.has("requests")) firstView.remove("requests");
        if(firstView.has("videoFrames"))firstView.remove("videoFrames");
        if(firstView.has("domains")) firstView.remove("domains");
        if(firstView.has("consoleLog")) firstView.remove("consoleLog");
        // set user timings
        runNodeObj.set("firstView", setUserTimings(firstView));

        if(runNodeObj.has("repeatView")) {
            ObjectNode repeatView = (ObjectNode) runNodeObj.get("repeatView");
            if(repeatView.has("requests")) repeatView.remove("requests");
            if(repeatView.has("videoFrames"))repeatView.remove("videoFrames");
            if(repeatView.has("domains")) repeatView.remove("domains");
            if(repeatView.has("consoleLog")) repeatView.remove("consoleLog");
            // set user timings
            runNodeObj.set("repeatView", setUserTimings(repeatView));
        }

        return runNodeObj;
    }

    private ObjectNode setUserTimings(ObjectNode runViewNodeObject){
        if(!runViewNodeObject.has("userTimes")){
            Map<String, Long> userTimes = new HashMap<>();
            Iterator<String> fieldIterator = runViewNodeObject.fieldNames();
            while(fieldIterator.hasNext()){
                String fieldName = fieldIterator.next();
                if(fieldName.startsWith("userTime.")){
                    String userTimingName = fieldName.replace("userTime.", "");
                    userTimes.put(userTimingName, runViewNodeObject.get(fieldName).asLong());
                }
            }
            runViewNodeObject.set("userTimes", objectMapper.valueToTree(userTimes));
        }
        return runViewNodeObject;
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

    public long deleteBadRuns() {
        return dao.deleteBadRuns();
    }

    @Cacheable(value="wptTrendTableData", key="{#testName, #view, #runDuration}")
    public List<WptTestRunData> getTrendTableData(String testName, String view, String runDuration){

        List<WptTestRunData> runData = dao.getTrendTableData(testName, view, runDuration);
        return runData;

    }

    @Cacheable(value="wptTrendChartData", key="{#testName, #view, #isUserTimingBaseLine, #interval}")
    public List<WptTrendMetric> getTrendChartData(String testName, String view, boolean isUserTimingBaseLine, String interval){
        return dao.getTrendChartData(testName, view, isUserTimingBaseLine, interval);
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
     * Build and return TestRunData object for a UX test.  This is the data displayed in the grid on a test trend page.
     * @param dataByDate
     * @param testData
     * @param pageName
     * @param timestamp
     * @return
     */
    private WptTestRunData getTestRunData(JsonNode dataByDate, JsonNode testData, String pageName, long timestamp) {
        // data for the data table
        WptTestRunData wptTestRunData = null;
//        testRunData.set_id(dataByDate.get("_id").asText());
//        testRunData.setPage(pageName);
//        testRunData.setRev(dataByDate.get("_rev").asText());
//        testRunData.setTimestamp(new SimpleDateFormat("yyyy-MM-dd H:m").format(new Date(timestamp)));
//        testRunData.setWptId(dataByDate.get("id").asText());
//        testRunData.setConnectivity(dataByDate.get("connectivity").asText());
//        testRunData.setViewData(testData);
        return wptTestRunData;
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
     * Initializes ElasticSearch with required indexes and aliases
     * @return
     */
    public boolean initES(){
        return dao.initWptSummaryIndex();
    }


    /**
     * Import test from WPT
     * @param id
     * @param sendToGraphite
     * @return
     * @throws Exception
     */
//    public WptImportResult importTestCouch(String id, boolean sendToGraphite) throws Exception {
//        log.info("======= New Test ID: " + id);
//
//        // write queue file
//        Path queueFilePath = Paths.get(uxDataStorePath + "queue/" + id);
//        Files.write(queueFilePath, Collections.emptyList());
//        log.info("File moved to queue");
//
//        // set urls
//        String jsonUrl = String.format("%s/jsonResult.php?test=%s", wptUrl, id);
//        String harUrl = String.format("%s/export.php?test=%s", wptUrl, id);
//        String resultFileName = String.format("%sresults/%s", uxDataStorePath, id);
//
//        // get json results from wpt
//        String jsonContents = Unirest.get(jsonUrl).asString().getBody();
//        // save to local file
//        FileUtil.writeToFile(resultFileName + ".json", jsonContents);
//        // gzip file
//        FileUtil.gzipFile(resultFileName + ".json", resultFileName + ".json.gz");
//        Files.deleteIfExists(Paths.get(resultFileName + ".json")); // delete original
//        log.info("Got json results");
//
//        // grab har file contents
//        String harContents = Unirest.get(harUrl).asString().getBody();
//        // save to local file
//        FileUtil.writeToFile(resultFileName + ".har", harContents);
//        // gzip file
//        FileUtil.gzipFile(resultFileName + ".har", resultFileName + ".har.gz");
//        Files.deleteIfExists(Paths.get(resultFileName + ".har")); // delete original
//        log.info("Got har file");
//
//        // push json to couch db
//        JsonNode jsonNodeWptResults = objectMapper.readTree(jsonContents);
//        CouchDbActionResp summaryResults = storeSummaryResultsCouch(jsonNodeWptResults);
//        CouchDbActionResp rawResults = storeRawResults(jsonNodeWptResults);
//        log.info("UX Summary Results: " + summaryResults.isOk());
//        log.info("UX Raw Results: " + rawResults.isOk());
//        log.info("Pushed to couchdb");
//
//        // send to graphite
//        if(sendToGraphite) {
//            sendDataToGraphite(jsonNodeWptResults);
//        }
//
//        // mark this processed and move to different directory
//        Path processedFilePath = Paths.get(uxDataStorePath + "processed/" + id);
//        try {
//            Files.move(queueFilePath, processedFilePath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (Exception ex){
//            log.error("Error moving file - " + queueFilePath + "\n Error: " + ex.getMessage());
//        }
//        log.info("Marked as processed");
//
//        return new WptImportResult(summaryResults, rawResults);
//    }



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

}
