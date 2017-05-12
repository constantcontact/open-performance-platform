package com.opp.service;

import com.opp.dao.CiLoadTestJobDao;
import com.opp.domain.CiLoadTestJob;
import com.opp.domain.CiLoadTestJobGetWithType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ctobe on 4/27/17.
 */
@Service
public class CiLoadTestJobService {

    @Autowired
    private CiLoadTestJobDao dao;

    public Optional<CiLoadTestJobGetWithType> add(CiLoadTestJob ciLoadTestJob) {
        int insertId = dao.insert(ciLoadTestJob);
        return dao.findById(insertId);

    }

    public Optional<CiLoadTestJobGetWithType> update(int applicationId, CiLoadTestJob updateCiLoadTestJob) {
        dao.update(applicationId, updateCiLoadTestJob);
        return dao.findById(applicationId);
    }

    public Optional<CiLoadTestJobGetWithType> getById(int id) {
        return dao.findById(id);
    }

    public List<CiLoadTestJobGetWithType> getAll() {
        return dao.findAll();
    }

    public Optional<CiLoadTestJobGetWithType> getByTestName(String testName) {
        return dao.findByTestName(testName);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

    public List<CiLoadTestJobGetWithType> search(CiLoadTestJob ciLoadTestJob) {
        return dao.search(ciLoadTestJob);
    }
}
