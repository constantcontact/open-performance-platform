package com.opp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

/**
 * Created by ctobe on 6/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadSlaTestGroup {
    private int id;
    private LocalDateTime creationDate;
    private boolean isActive;
    private int loadSlaGroupId;
    private int loadTestId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getLoadSlaGroupId() {
        return loadSlaGroupId;
    }

    public void setLoadSlaGroupId(int loadSlaGroupId) {
        this.loadSlaGroupId = loadSlaGroupId;
    }

    public int getLoadTestId() {
        return loadTestId;
    }

    public void setLoadTestId(int loadTestId) {
        this.loadTestId = loadTestId;
    }
}
