package com.opp.service;

import com.opp.dao.LoadTestAggregateDao;
import com.opp.dao.LoadTestDao;
import com.opp.domain.LoadTestAggregate;
import com.opp.dto.LoadTestAggregateDataWithSlas;
import com.opp.dto.aggregate.LoadTestAggregateDataResp;
import com.opp.dto.aggregate.LoadTestAggregateImportResp;
import com.opp.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by ctobe on 6/27/16.
 */
@Service
public class LoadTestAggregateService {

    @Autowired
    private LoadTestAggregateDao dao;

    @Autowired
    private LoadTestDao loadTestDao;

    @Autowired
    private LoadTestService loadTestService;


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Aggregates load test data
     * @param loadTestId
     * @return
     */
    public LoadTestAggregateDataResp aggregateLoadTestData(int loadTestId){

        // delete aggregates if they exist
        if(isLoadTestAggregated(loadTestId)) deleteByLoadTestId(loadTestId);

        // aggregate data
        int createdCount = dao.createLoadTestAggregate(loadTestId);

        if(createdCount > 0){
            // set start and end times on load test object
            Map<String, Long> timesMap = loadTestDao.deriveLoadTestStartEndTimes(loadTestId);
            Long startTime = timesMap.get("start_time");
            Long endTime = timesMap.get("end_time");

            if(startTime != null){
                // detect if date includes milliseconds.  If it is remove milliseconds
                startTime = (startTime > 143351712800L) ? startTime / 1000 : startTime;
                endTime = (endTime > 143351712800L) ? endTime / 1000 : endTime;

                // update start and end time for load test
                int updateAffectedRows = loadTestDao.updateStartEndTime(loadTestId, startTime, endTime);
                log.debug(String.format("Aggregate_model.aggregateLoadTestData: Updated %d load test record(s) in the database", updateAffectedRows));
                return new LoadTestAggregateDataResp(loadTestId, createdCount, startTime, endTime);
            }
        }
        return new LoadTestAggregateDataResp(loadTestId, 0, 0, 0); // nothing worked

    }

    /**
     * Checks to see if a load test has aggregate data
     * @param loadTestId
     * @return
     */
    public boolean isLoadTestAggregated(int loadTestId){
        List<LoadTestAggregate> loadTestAggregateList = getByLoadTestId(loadTestId);
        if(loadTestAggregateList.isEmpty())
            return false;

        log.debug(String.format("Load test id %s already aggregated", loadTestId));
        return true;
    }

    /**
     * Deletes load test aggregate data
     * @param loadTestId
     */
    public void deleteByLoadTestId(int loadTestId){
        dao.deleteByLoadTestId(loadTestId);
    }

    /**
     * Gets aggregate load test data
     * @param loadTestId
     * @return
     */
    public List<LoadTestAggregate> getByLoadTestId(int loadTestId) {
        return dao.findByLoadTestId(loadTestId);
    }

    /**
     * Gets aggregate load test data with SLAs
     * @param loadTestId
     * @return
     */
    public List<LoadTestAggregateDataWithSlas> getByLoadTestIdWithSlas(int loadTestId) {
        return dao.findByLoadTestIdWithSlas(loadTestId);
    }

    /**
     * Imports a list of aggregate data
     * @param loadTestId
     * @param loadTestAggregateList
     * @return
     */
    public LoadTestAggregateImportResp addAll(int loadTestId, List<LoadTestAggregate> loadTestAggregateList) {
        // check if load test exists
        loadTestService.getById(loadTestId).orElseThrow(() -> new ResourceNotFoundException("Load Test not found with ID: " + loadTestId));

        LocalDateTime dtStart = LocalDateTime.now();
        return new LoadTestAggregateImportResp(loadTestId, loadTestAggregateList.size(), dao.add(loadTestId, loadTestAggregateList),  Duration.between(dtStart, LocalDateTime.now()).getSeconds());
    }
}
