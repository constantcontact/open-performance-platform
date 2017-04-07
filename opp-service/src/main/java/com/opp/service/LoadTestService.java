package com.opp.service;

import com.opp.dao.LoadTestDao;
import com.opp.domain.LoadTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by ctobe on 7/8/16.
 */
@Service
public class LoadTestService {

    @Autowired
    LoadTestDao dao;

    public Optional<LoadTest> add(LoadTest loadTest) {
        int insertId = dao.insert(loadTest);
        return dao.findById(insertId);
    }

    public Optional<LoadTest> update(Integer loadTestId, LoadTest update) {
        dao.update(loadTestId, update);
        return dao.findById(loadTestId);
    }

    public int delete(String loadTestId) {
        int successCount = 0;
        String[] ids = loadTestId.split(",");
        for(String id : ids){
            boolean res = dao.delete(Integer.parseInt(id.trim())) == 1;
            if(!res) successCount++;
        }
        return successCount;
    }

    public Optional<LoadTest> getById(Integer loadTestId) {
        return dao.findById(loadTestId);
    }

    public List<LoadTest> getAll(Map<String, String> filter) {
        return dao.findAll(filter);
    }
}
