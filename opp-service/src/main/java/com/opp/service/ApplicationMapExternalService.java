package com.opp.service;

import com.opp.domain.ApplicationMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jpalmer on 1/9/17.
 */
@Service
public class ApplicationMapExternalService {

    public static final String URL_APPLICATIONS = "/applications";

    @Value("${opp.appMap.externalURL}")
    private String externalURL;

    @Value("${opp.appMap.apiVersion}")
    private String apiVersion;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Optional<ApplicationMap> add(ApplicationMap applicationMap) {
        Optional<ApplicationMap> mapOptional = Optional.empty();
        try {
            HttpResponse<ApplicationMap> resp = Unirest.post(externalURL + apiVersion + URL_APPLICATIONS)
                    .body(applicationMap)
                    .asObject(ApplicationMap.class);

            mapOptional = Optional.of(resp.getBody());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return mapOptional;
    }

    public Optional<ApplicationMap> update(int applicationId, ApplicationMap updateApplicationMap) {
        Optional<ApplicationMap> mapOptional = Optional.empty();
        try {
            HttpResponse<ApplicationMap> resp = Unirest.put(externalURL + apiVersion + URL_APPLICATIONS + "/" + applicationId)
                    .body(updateApplicationMap)
                    .asObject(ApplicationMap.class);

            mapOptional = Optional.of(resp.getBody());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return mapOptional;
    }

    public Optional<ApplicationMap> getById(int id) {
        Optional<ApplicationMap> applicationMap = Optional.empty();
        try {
            HttpResponse<ApplicationMap> resp = Unirest.get(externalURL + apiVersion + URL_APPLICATIONS + "/" + id).asObject(ApplicationMap.class);
            if (resp.getStatus() == 200 ) {
                applicationMap = Optional.of(resp.getBody());
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return applicationMap;
    }

    public List<ApplicationMap> getAll() {
        List<ApplicationMap> applicationMapList = new ArrayList<>();

        try {
            HttpResponse<ApplicationMap[]> resp = Unirest.get(externalURL + apiVersion + URL_APPLICATIONS).asObject(ApplicationMap[].class);
            applicationMapList = Arrays.asList(resp.getBody());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return applicationMapList;
    }

    public List<ApplicationMap> getAllByAppKey(String appKey) {
        List<ApplicationMap> applicationMapList = new ArrayList<>();

        try {
            applicationMapList = getAll().stream().filter(app -> app.getAppKey().equalsIgnoreCase(appKey)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return applicationMapList;
    }

    public void delete(int id){
        try {
            HttpResponse<String> resp = Unirest.delete(externalURL + apiVersion + URL_APPLICATIONS + "/" + id).asString();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }
}
