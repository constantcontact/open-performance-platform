package com.opp.service;

import com.opp.dao.LoadTestDataDao;
import com.opp.domain.LoadTestData;
import com.opp.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ctobe on 7/14/16.
 */
@Service
public class LoadTestDataService {

    @Autowired
    private LoadTestDataDao dao;

    @Autowired
    private LoadTestService loadTestService;

    /**
     * Batch adds records.  Returns the total number of records added
     * @param loadTestId
     * @param loadTestDataList
     * @return
     */
    public int addAll(int loadTestId, List<LoadTestData> loadTestDataList){
        // check if load test exists
        loadTestService.getById(loadTestId).orElseThrow(() -> new ResourceNotFoundException("Load Test not found with ID: " + loadTestId));
        return dao.insertAll(loadTestId, loadTestDataList);
    }


    public Optional<LoadTestData> add(int loadTestId, LoadTestData loadTestData) {
        int insertId = dao.insert(loadTestId, loadTestData);
        return dao.findById(loadTestId, insertId);
    }

    public Optional<LoadTestData> update(Integer loadTestId, long dataId, LoadTestData update) {
        dao.update(loadTestId, dataId, update);
        return dao.findById(loadTestId, dataId);
    }

    public boolean delete(int loadTestId, int dataId) {
        return (dao.delete(loadTestId, dataId) == 1);
    }

    public int deleteAll(int loadTestId) {
        return dao.deleteAllByLoadTestId(loadTestId);
    }

    public Optional<LoadTestData> getById(int loadTestId, long dataId) {
        return dao.findById(loadTestId, dataId);
    }

    public List<LoadTestData> getAllByLoadTestId(int loadTestId) {
        return dao.findAllByLoadTestId(loadTestId);
    }


}
