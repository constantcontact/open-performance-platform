package com.opp.dto.ux.couchdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by ctobe on 9/27/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouchDbGetResp {
    private String _id;
    private String _rev;
    private boolean _deleted;
    private  Object _attachments;
    private  List<JsonNode> _conflicts;
    private List<JsonNode> _conflicts_deleted_conflicts;
    private String _local_seq;
    private String _revs_info;
    private List<Object> _revisions;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public boolean is_deleted() {
        return _deleted;
    }

    public void set_deleted(boolean _deleted) {
        this._deleted = _deleted;
    }

    public Object get_attachments() {
        return _attachments;
    }

    public void set_attachments(Object _attachments) {
        this._attachments = _attachments;
    }

    public List<JsonNode> get_conflicts() {
        return _conflicts;
    }

    public void set_conflicts(List<JsonNode> _conflicts) {
        this._conflicts = _conflicts;
    }

    public List<JsonNode> get_conflicts_deleted_conflicts() {
        return _conflicts_deleted_conflicts;
    }

    public void set_conflicts_deleted_conflicts(List<JsonNode> _conflicts_deleted_conflicts) {
        this._conflicts_deleted_conflicts = _conflicts_deleted_conflicts;
    }

    public String get_local_seq() {
        return _local_seq;
    }

    public void set_local_seq(String _local_seq) {
        this._local_seq = _local_seq;
    }

    public String get_revs_info() {
        return _revs_info;
    }

    public void set_revs_info(String _revs_info) {
        this._revs_info = _revs_info;
    }

    public List<Object> get_revisions() {
        return _revisions;
    }

    public void set_revisions(List<Object> _revisions) {
        this._revisions = _revisions;
    }
}
