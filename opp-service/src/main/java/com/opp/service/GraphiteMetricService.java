package com.opp.service;

import com.opp.dao.GraphiteMetricDao;
import com.opp.domain.GraphiteMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ctobe on 9/13/16.
 */
@Service
public class GraphiteMetricService {

    @Autowired
    GraphiteMetricDao dao;

    public Optional<GraphiteMetric> add(GraphiteMetric loadTest) {
        int insertId = dao.insert(loadTest);
        return dao.findById(insertId);
    }

    public Optional<GraphiteMetric> update(Integer graphiteMetricId, GraphiteMetric update) {
        dao.update(graphiteMetricId, update);
        return dao.findById(graphiteMetricId);
    }

    public boolean delete(Integer graphiteMetricId) {
        return (dao.delete(graphiteMetricId) == 1);
    }

    public Optional<GraphiteMetric> getById(Integer graphiteMetricId) {
        return dao.findById(graphiteMetricId);
    }

    public List<GraphiteMetric> getAll() {
        return dao.findAll();
    }
}
