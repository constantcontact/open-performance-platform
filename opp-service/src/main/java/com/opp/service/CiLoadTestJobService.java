package com.opp.service;

import com.opp.dao.CiLoadTestJobDao;
import com.opp.domain.CiLoadTestJob;
import com.opp.domain.CiLoadTestJobGetWithType;
import com.opp.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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

    public List<CiLoadTestJobGetWithType> search(CiLoadTestJob ciLoadTestJob, boolean mergeWithDefault) {
        List<CiLoadTestJobGetWithType> jobs = dao.search(ciLoadTestJob);
        if(mergeWithDefault) {

            // get default job
            CiLoadTestJob defaultJobSearch = new CiLoadTestJob();
            defaultJobSearch.setTestName(".DEFAULT");
            defaultJobSearch.setTestType(ciLoadTestJob.getTestType());
            List<CiLoadTestJobGetWithType> defaultJobs = dao.search(defaultJobSearch);

            // if only 1 default job was found
            if(defaultJobs.size() == 1){
                // merge with default
                return jobs.stream().map(j -> merge(defaultJobs.get(0), j)).collect(toList());
            } else {
                // should not be here
                throw new BadRequestException("You requested to merge with defaults, but no default was found matching your testType");
            }
        } else {
            return jobs;
        }
    }

    private CiLoadTestJobGetWithType merge(CiLoadTestJobGetWithType src, CiLoadTestJobGetWithType dest){
        if(dest.getAdditionalOptions() == null) dest.setAdditionalOptions(src.getAdditionalOptions());
        if(dest.getAppUnderTest() == null) dest.setAppUnderTest(src.getAppUnderTest());
        if(dest.getAppUnderTestVersion() == null) dest.setAppUnderTestVersion(src.getAppUnderTestVersion());
        if(dest.getComments() == null) dest.setComments(src.getComments());
        if(dest.getCronSchedule() == null) dest.setCronSchedule(src.getCronSchedule());
        if(dest.getDescription() == null) dest.setDescription(src.getDescription());
        if(dest.getEnvironment() == null) dest.setEnvironment(src.getEnvironment());
        if(dest.getHostName() == null) dest.setHostName(src.getHostName());
        if(dest.getRampVuserEndDelay() == null) dest.setRampVuserEndDelay(src.getRampVuserEndDelay());
        if(dest.getRampVuserStartDelay() == null) dest.setRampVuserStartDelay(src.getRampVuserStartDelay());
        if(dest.getRunDuration() == null) dest.setRunDuration(src.getRunDuration());
        if(dest.getSlaGroupId() == null) dest.setSlaGroupId(src.getSlaGroupId());
        if(dest.getTestType() == null) dest.setTestType(src.getTestType());
        if(dest.getTestTool() == null) dest.setTestTool(src.getTestTool());
        if(dest.getTestToolVersion() == null) dest.setTestToolVersion(src.getTestToolVersion());
        if(dest.getTestDataType() == null) dest.setTestDataType(src.getTestDataType());
        if(dest.getTestName() == null) dest.setTestName(src.getTestName());
        if(dest.getTestSubName() == null) dest.setTestSubName(src.getTestSubName());
        if(dest.getTestPath() == null) dest.setTestPath(src.getTestPath());
        if(dest.getVuserCount() == null) dest.setVuserCount(src.getVuserCount());
        return dest;
    }
}
