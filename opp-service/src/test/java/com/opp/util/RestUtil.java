package com.opp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

/**
 * Created by ctobe on 8/10/16.
 */
public class RestUtil {

    public RestUtil() {
        Unirest.setDefaultHeader("accept", "application/json");
        Unirest.setDefaultHeader("content-type", "application/json");

        // Only one time
        Unirest.setObjectMapper(new ObjectMapper() {
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
    }

    public static void testFullCrud(String url, String urlWithId, String idNameProperty, JSONObject postData, JSONObject putData) throws UnirestException {
        // Create - POST
        String id = verifyPost(url, postData, idNameProperty).toString();

        // Update - PUT
        urlWithId = urlWithId.replace("{id}", id);
        verifyPut(urlWithId, putData);

        // Get by id - GET
        verifyGet(urlWithId, putData);

        // Get all - GET
        verifyGetAll(url);

        // Delete - DELETE
        verifyDelete(urlWithId);
    }

    public static Integer verifyPost(String url, JSONObject postData, String idPropertyName) throws UnirestException {
        HttpResponse<JsonNode> httpResponse = Unirest.post(url).body(postData).asJson();
        Integer id = httpResponse.getBody().getObject().getInt(idPropertyName);
        assertTrue("POST - Verifying Response Code", httpResponse.getStatus() == 201);
        assertTrue("POST - Verifying ID", id > 0);
        verifyAllFields("POST Fields", postData, httpResponse);
        return id;
    }

    public static void verifyPut(String url, JSONObject putData) throws UnirestException {
        HttpResponse<JsonNode> httpResponse = Unirest.put(url).body(putData).asJson();
        assertTrue("PUT - Verifying Response Code", httpResponse.getStatus() == 200);
        verifyAllFields("PUT Fields", putData, httpResponse);
    }

    public static void verifyGet(String url, JSONObject expectedData) throws UnirestException {
        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertTrue("GET - Verifying Response Code", httpResponse.getStatus() == 200);
        verifyAllFields("GET Fields", expectedData, httpResponse);
    }

    public static HttpResponse<JsonNode> verifyGetAll(String url) throws UnirestException {
        HttpResponse<JsonNode> httpResponse = Unirest.get(url).asJson();
        assertTrue("GET All - Verifying Response Code", httpResponse.getStatus() == 200);
        assertTrue("GET All - Verifying Response Type is Array", httpResponse.getBody().isArray());
        assertTrue("GET All - Verifying Response has at least 1 value", httpResponse.getBody().getArray().length() > 0);
        return httpResponse;
    }

    private static void verifyAllFields(String name, JSONObject originalObj, HttpResponse<JsonNode> response){
        Iterator<?> keys = originalObj.keys();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            if(originalObj.get(key) instanceof Long){
                // sometimes it will convert ints to what should be longs and vice versa.  Check for long here.
                assertEquals(name + " - Verifying: " + key, response.getBody().getObject().getLong(key), originalObj.getLong(key));
            } else {
                assertTrue(name + " - Verifying: " + key, response.getBody().getObject().get(key).equals(originalObj.get(key)));
            }
        }
    }

    public static void verifyDelete(String url) throws UnirestException {
        HttpResponse<JsonNode> httpResponse = Unirest.delete(url).asJson();
        assertTrue("DELETE - Verifying Response Code", httpResponse.getStatus() == 202);
        HttpResponse<JsonNode> httpResponseConfirm = Unirest.get(url).asJson();
        assertTrue("DELETE - Content no longer exists", httpResponseConfirm.getStatus() == 404);
    }

}
