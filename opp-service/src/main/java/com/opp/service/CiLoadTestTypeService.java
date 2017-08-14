package com.opp.service;

import com.opp.dao.CiLoadTestTypeDao;
import com.opp.domain.CiLoadTestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ctobe on 4/27/17.
 */
@Service
public class CiLoadTestTypeService {

    @Autowired
    private CiLoadTestTypeDao dao;

    public Optional<CiLoadTestType> add(CiLoadTestType ciLoadTestType) {
        int insertId = dao.insert(ciLoadTestType);
        return dao.findById(insertId);

    }

    public Optional<CiLoadTestType> update(int applicationId, CiLoadTestType updateCiLoadTestType) {
        dao.update(applicationId, updateCiLoadTestType);
        return dao.findById(applicationId);
    }

    public Optional<CiLoadTestType> getById(int id) {
        return dao.findById(id);
    }

    public List<CiLoadTestType> getAll() {
        return dao.findAll();
    }

    public Optional<CiLoadTestType> getByTestType(String testType) {
        return dao.findByTestType(testType);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

}
