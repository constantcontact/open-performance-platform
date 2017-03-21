package com.opp.dto.ux.couchdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by ctobe on 9/27/16.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class CouchDbViewResp {
    private int totalRows;
    private int offset;
    private List<Row> Rows;

    // if errors
    private String error;
    private String reason;


    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<Row> getRows() {
        return Rows;
    }

    public void setRows(List<Row> Rows) {
        this.Rows = Rows;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static class Row {
        private String id;
        private Object key;
        private JsonNode value;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public JsonNode getValue() {
            return value;
        }

        public void setValue(JsonNode value) {
            this.value = value;
        }
    }
}
