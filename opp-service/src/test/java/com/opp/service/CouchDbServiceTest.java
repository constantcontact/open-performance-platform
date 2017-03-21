package com.opp.service;

import com.opp.BaseIntegrationTest;
import com.opp.dto.ux.couchdb.CouchDbActionResp;
import com.opp.dto.ux.couchdb.CouchDbViewResp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class CouchDbServiceTest extends BaseIntegrationTest {

    @Autowired
    private CouchDbService couchDbService;


    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Ignore
    public void crudTest() throws Exception {

        String dbName = "craig";

        // create database
        // todo: fix access errors
        HttpResponse<JsonNode> addDbResp = couchDbService.addDatabase(dbName);
        assertEquals("create db", 201, addDbResp.getStatus());

        HashMap<String, String> data = new HashMap<>();
        data.put("name", "craig");
        data.put("address", "123 lane");

        // add doc
        CouchDbActionResp createResp = couchDbService.addDoc(dbName, data);
        assertTrue("Add doc", createResp.isOk());

        JsonNode getResp = couchDbService.getDocumentById(dbName, createResp.getId());
        assertEquals("get doc", "craig", getResp.asText("name"));

        // update doc
        data.put("name", "john");
        // todo: fix 409 conflict error
        CouchDbActionResp updateResp = couchDbService.updateDocumentById(dbName, createResp.getId(), data);
        assertTrue("get doc", updateResp.isOk());

        // get doc
        getResp = couchDbService.getDocumentById(dbName, updateResp.getId());
        assertEquals("get doc", "john", getResp.asText("name"));

        System.out.println("Got doc");
        System.out.println(getResp);

        // get all docs
        CouchDbViewResp allDocs = couchDbService.getAllDocumentIds(dbName);
        assertTrue("getting all docs", allDocs.getRows().size() > 0);
        System.out.println(allDocs);


        // delete by rev
        System.out.println("deleting by rev");
        System.out.println(couchDbService.deleteDoc(dbName, createResp.getId(), createResp.getRev()));

        // delete by id
        // todo: fix 409 conflict
        System.out.println("deleting no rev");
        System.out.println(couchDbService.deleteDocNoRev(dbName, createResp.getId()));





    }
}
