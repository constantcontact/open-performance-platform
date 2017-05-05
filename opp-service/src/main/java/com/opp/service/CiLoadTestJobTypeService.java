package com.opp.service;

import com.opp.dao.CiLoadTestJobTypeDao;
import com.opp.domain.CiLoadTestJobType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ctobe on 4/27/17.
 */
@Service
public class CiLoadTestJobTypeService {

    @Autowired
    private CiLoadTestJobTypeDao dao;

    public Optional<CiLoadTestJobType> add(CiLoadTestJobType ciLoadTestJobType) {
        int insertId = dao.insert(ciLoadTestJobType);
        return dao.findById(insertId);

    }

    public Optional<CiLoadTestJobType> update(int applicationId, CiLoadTestJobType updateCiLoadTestJobType) {
        dao.update(applicationId, updateCiLoadTestJobType);
        return dao.findById(applicationId);
    }

    public Optional<CiLoadTestJobType> getById(int id) {
        return dao.findById(id);
    }

    public List<CiLoadTestJobType> getAll() {
        return dao.findAll();
    }

    public Optional<CiLoadTestJobType> getByJobType(String jobType) {
        return dao.findByJobType(jobType);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

}
