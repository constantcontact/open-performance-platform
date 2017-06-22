package com.opp.service;

import com.opp.dao.LoadSlaDao;
import com.opp.domain.LoadSla;
import com.opp.domain.LoadSlaGroup;
import com.opp.domain.LoadSlaTestGroup;
import com.opp.dto.LoadTestSla;
import com.opp.exception.BadRequestException;
import com.opp.exception.InternalServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ctobe on 8/8/16.
 */
@Service
public class LoadSlaService {

    @Autowired
    private LoadSlaDao dao;

    @Autowired
    private LoadSlaGroupService loadSlaGroupService;

    @Autowired
    private LoadSlaTestGroupService loadSlaTestGroupService;

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

    /**
     * Bulk import SLAs.  A group id will be created.
     *
     * @param loadTestId
     * @param slas
     * @return
     */
    public LoadSlaBulkImportResult bulkImport(Integer loadTestId, List<LoadSla> slas) {
        LoadSlaBulkImportResult res = new LoadSlaBulkImportResult();

        if(slas.size() == 0) throw new BadRequestException("No or invalid SLAs sent");

        // create load test group id
        int slaGroupId = loadSlaGroupService.add(new LoadSlaGroup("Auto Generated Group from Load Test #" + loadTestId))
                .orElseThrow(() -> new InternalServiceException("Unable to create SLA Group")).getId();

        // add load test to SLA group
        loadSlaTestGroupService.insert(new LoadSlaTestGroup(0, ZonedDateTime.now(ZoneOffset.UTC), true, slaGroupId, loadTestId));// ignore the 0

        res.setTotal(slas.size());
        AtomicInteger successful = new AtomicInteger(0);
        slas.forEach(sla -> {
                sla.setLoadSlaGroupId(slaGroupId);
                add(sla).ifPresent(c -> successful.incrementAndGet());
            }
        );
        res.setSuccessful(successful.get());
        return res;
    }

    public LoadSlaBulkImportResult bulkUpdate(List<LoadSla> slas) {
        LoadSlaBulkImportResult res = new LoadSlaBulkImportResult();
        res.setTotal(slas.size());
        AtomicInteger successful = new AtomicInteger(0);
        slas.forEach(sla -> update(sla.getId(), sla).ifPresent(c -> successful.incrementAndGet()));
        res.setSuccessful(successful.get());
        return res;
    }

    public static class LoadSlaBulkImportResult {
        private int total;
        private int successful;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSuccessful() {
            return successful;
        }

        public void setSuccessful(int successful) {
            this.successful = successful;
        }
    }
}
