package com.opp.service;

import com.opp.dao.LoadSlaDao;
import com.opp.domain.LoadSla;
import com.opp.dto.LoadTestSla;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ctobe on 8/8/16.
 */
@Service
public class LoadSlaService {

    @Autowired
    private LoadSlaDao dao;

    public Optional<LoadSla> add(LoadSla sla) {
        int insertId = dao.insert(sla);
        return dao.findById(insertId);
    }

    public Optional<LoadSla> update(Integer slaId, LoadSla update) {
        dao.update(slaId, update);
        return dao.findById(slaId);
    }

    public boolean delete(Integer slaGroupId) {
        return (dao.delete(slaGroupId) == 1);
    }

    public Optional<LoadSla> getById(Integer slaId) {
        return dao.findById(slaId);
    }

    public List<LoadSla> getAll() {
        return dao.findAll();
    }

    public List<LoadTestSla> getAllByLoadTestId(Integer loadTestId) {
        return dao.findAllByLoadTestId(loadTestId);
    }
}
