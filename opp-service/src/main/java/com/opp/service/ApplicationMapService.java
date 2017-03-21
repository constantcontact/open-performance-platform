package com.opp.service;

import com.opp.dao.ApplicationMapDao;
import com.opp.domain.ApplicationMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ctobe on 6/13/16.
 */
@Service
public class ApplicationMapService {

    @Autowired
    private ApplicationMapDao dao;

    @Value("${opp.appMap.useExternal}")
    private boolean useExternal;

    @Autowired
    private ApplicationMapExternalService applicationMapExternalService;

    public Optional<ApplicationMap> add(ApplicationMap applicationMap) {
        if (useExternal) {
            return applicationMapExternalService.add(applicationMap);
        } else {
            int insertId = dao.insert(applicationMap);
            return dao.findById(insertId);
        }
    }

    public Optional<ApplicationMap> update(int applicationId, ApplicationMap updateApplicationMap) {
        if (useExternal) {
            return applicationMapExternalService.update(applicationId, updateApplicationMap);
        } else {
            dao.update(applicationId, updateApplicationMap);
            return dao.findById(applicationId);
        }
    }

    public Optional<ApplicationMap> getById(int id) {
        if (useExternal) {
            return applicationMapExternalService.getById(id);
        } else {
            return dao.findById(id);
        }
    }

    public List<ApplicationMap> getAll() {
        if (useExternal) {
            return applicationMapExternalService.getAll();
        } else {
            return dao.findAll();
        }
    }

    public List<ApplicationMap> getAllByAppKey(String appKey) {
        if (useExternal) {
            return applicationMapExternalService.getAllByAppKey(appKey);
        } else {
            return dao.findAllByName(appKey);
        }
    }

    public void delete(int id) {
        if (useExternal) {
            applicationMapExternalService.delete(id);
        } else {
            dao.delete(id);
        }
    }
}
