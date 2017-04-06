package com.opp.dto.ux;

/**
 * Created by ctobe on 3/30/17.
 */
public class WptDeleteResponse {
    private String searchField;
    private String searchBy;
    private int deleteCount;

    public WptDeleteResponse() {
    }

    public WptDeleteResponse(String searchField, String searchBy, int deleteCount) {
        this.searchField = searchField;
        this.searchBy = searchBy;
        this.deleteCount = deleteCount;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }
}
