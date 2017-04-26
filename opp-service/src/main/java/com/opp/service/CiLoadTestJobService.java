package com.opp.service;

import com.opp.dao.CiLoadTestJobDao;
import com.opp.dao.CiLoadTestJobDao;
import com.opp.domain.CiLoadTestJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public Optional<CiLoadTestJob> add(CiLoadTestJob ciLoadTestJob) {
        int insertId = dao.insert(ciLoadTestJob);
        return dao.findById(insertId);

    }

    public Optional<CiLoadTestJob> update(int applicationId, CiLoadTestJob updateCiLoadTestJob) {
        dao.update(applicationId, updateCiLoadTestJob);
        return dao.findById(applicationId);
    }

    public Optional<CiLoadTestJob> getById(int id) {
        return dao.findById(id);
    }

    public List<CiLoadTestJob> getAll() {
        return dao.findAll();
    }

    public Optional<CiLoadTestJob> getByTestName(String testName) {
        return dao.findByTestName(testName);
    }

    public int delete(int id) {
        return dao.delete(id);
    }
}
