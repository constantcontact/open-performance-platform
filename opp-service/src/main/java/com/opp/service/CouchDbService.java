package com.opp.service;

import com.opp.dto.ux.couchdb.CouchDbActionResp;
import com.opp.dto.ux.couchdb.CouchDbViewResp;
import com.opp.exception.BadRequestException;
import com.opp.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by ctobe on 9/15/16.
 */
@Service
public class CouchDbService {


    @Value("${opp.ux.couchdb.url}")
    private String couchdbUrl;

    @Autowired
    ObjectMapper objectMapper;

    public CouchDbService() {

        // Only one time
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Unirest.setDefaultHeader("accept", "application/json");
        Unirest.setDefaultHeader("content-type", "application/json");
    }

    public CouchDbActionResp addDoc(String db, Object contents) throws Exception {
        HttpResponse<CouchDbActionResp> resp = Unirest.post(couchdbUrl + db).body(contents).asObject(CouchDbActionResp.class);
        return processActionResp(resp.getBody());
    }



    public CouchDbActionResp deleteDocNoRev(String db, String id) throws UnirestException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_deleted", true);
        HttpResponse<CouchDbActionResp> resp = Unirest.put(couchdbUrl + db + "/" + id).body(jsonObject).asObject(CouchDbActionResp.class);
        return resp.getBody();
    }

    public CouchDbActionResp deleteDoc(String db, String id, String rev) throws UnirestException {
        HttpResponse<CouchDbActionResp> resp = Unirest.delete(couchdbUrl + db + "/" + id).queryString("rev", rev).asObject(CouchDbActionResp.class);
        return resp.getBody();
    }

    //todo: figure out why this throws a 409 conflict error.  CouchDb 2 should fix this.
    public CouchDbActionResp updateDocumentById(String db, String id, Object contents) throws UnirestException {
        HttpResponse<CouchDbActionResp> resp = Unirest.put(couchdbUrl + db + "/" + id).body(contents).asObject(CouchDbActionResp.class);
        return processActionResp(resp.getBody());
    }


    public CouchDbViewResp getAllDocumentIds(String db) throws UnirestException {
        HttpResponse<CouchDbViewResp> resp = Unirest.get(couchdbUrl + db + "/_all_docs").asObject(CouchDbViewResp.class);
        return processGetViewResp(resp);
    }

    public JsonNode getDocumentById(String db, String id) throws UnirestException {
        HttpResponse<JsonNode> resp = Unirest.get(couchdbUrl + db + "/" + id).asObject(JsonNode.class);
        return processGetResp(resp);
    }

    public CouchDbViewResp getDocumentsByViewQuery(String viewQuery) throws UnirestException {
        HttpResponse<CouchDbViewResp> resp = Unirest.get(couchdbUrl + viewQuery).asObject(CouchDbViewResp.class);
        return processGetViewResp(resp);
    }

    public HttpResponse<JsonNode> addDesign(String db, String name, Object contents) throws UnirestException {
        return Unirest.put(couchdbUrl + db + "/_design/" + name).body(contents).asObject(JsonNode.class);
    }

    public HttpResponse<JsonNode> addDatabase(String db) throws UnirestException {
        return Unirest.put(couchdbUrl + db).asObject(JsonNode.class);
    }

    private CouchDbActionResp processActionResp(CouchDbActionResp resp) throws BadRequestException {
        if(!resp.isOk()) throw new BadRequestException(resp.getError() + ": " + resp.getReason());
        return resp;
    }

    private CouchDbViewResp processGetViewResp(HttpResponse<CouchDbViewResp> resp) throws ResourceNotFoundException {
        if(resp.getStatus() > 400) throw new BadRequestException(resp.getBody().getError() + ": " + resp.getBody().getReason());
        return resp.getBody();
    }

    private JsonNode processGetResp(HttpResponse<JsonNode> resp) throws ResourceNotFoundException {
        if(resp.getStatus() > 400) throw new BadRequestException(resp.getBody().asText("error") + ": " + resp.getBody().asText("reason"));
        return resp.getBody();
    }

}
