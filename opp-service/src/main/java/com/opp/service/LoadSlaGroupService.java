package com.opp.service;

import com.opp.dao.LoadSlaGroupDao;
import com.opp.domain.LoadSlaGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ctobe on 8/8/16.
 */
@Service
public class LoadSlaGroupService {

    @Autowired
    private LoadSlaGroupDao dao;

    public Optional<LoadSlaGroup> add(LoadSlaGroup slaGroup) {
        int insertId = dao.insert(slaGroup);
        return dao.findById(insertId);
    }

    public Optional<LoadSlaGroup> update(Integer slaGroupId, LoadSlaGroup update) {
        dao.update(slaGroupId, update);
        return dao.findById(slaGroupId);
    }

    public boolean delete(Integer slaGroupId) {
        return (dao.delete(slaGroupId) == 1);
    }

    public Optional<LoadSlaGroup> getById(Integer slaGroupId) {
        return dao.findById(slaGroupId);
    }

    public List<LoadSlaGroup> getAll() {
        return dao.findAll();
    }
}
