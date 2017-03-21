package com.opp.service;

import com.opp.dao.LoadTestApplicationCoverageDao;
import com.opp.domain.LoadTestApplicationCoverage;
import com.opp.dto.LoadTestApplicationCoverageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ctobe on 8/16/16.
 */
@Service
public class LoadTestApplicationCoverageService {
    @Autowired
    private LoadTestApplicationCoverageDao dao;

    public int add(LoadTestApplicationCoverage ltac) {
        return dao.insert(ltac);
    }

    public boolean deleteByFilter(LoadTestApplicationCoverageFilter filter) {
        return (dao.deleteByFilter(filter) >= 1);
    }

    public List<LoadTestApplicationCoverage> getByFilter(LoadTestApplicationCoverageFilter filter) {
        return dao.findByFilter(filter);
    }

    public List<LoadTestApplicationCoverage> getAll() {
        return dao.findAll();
    }

}
