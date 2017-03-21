package com.opp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by ctobe on 6/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadSla {
    private int id;
    @NotNull
    private int loadSlaGroupId;
    private String customName;
    private Double customValue;
    private Double marginOfError;
    private Integer max;
    private Integer median;
    private Integer min;
    @NotNull
    private String name;
    private Integer pct75;
    private Integer pct90;
    private Integer avg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoadSlaGroupId() {
        return loadSlaGroupId;
    }

    public void setLoadSlaGroupId(int loadSlaGroupId) {
        this.loadSlaGroupId = loadSlaGroupId;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public Double getCustomValue() {
        return customValue;
    }

    public void setCustomValue(Double customValue) {
        this.customValue = customValue;
    }

    public Double getMarginOfError() {
        return marginOfError;
    }

    public void setMarginOfError(Double marginOfError) {
        this.marginOfError = marginOfError;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMedian() {
        return median;
    }

    public void setMedian(Integer median) {
        this.median = median;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPct75() {
        return pct75;
    }

    public void setPct75(Integer pct75) {
        this.pct75 = pct75;
    }

    public Integer getPct90() {
        return pct90;
    }

    public void setPct90(Integer pct90) {
        this.pct90 = pct90;
    }

    public Integer getAvg() {
        return avg;
    }

    public void setAvg(Integer avg) {
        this.avg = avg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadSla loadSla = (LoadSla) o;
        return id == loadSla.id &&
                Objects.equals(customName, loadSla.customName) &&
                Objects.equals(customValue, loadSla.customValue) &&
                Objects.equals(marginOfError, loadSla.marginOfError) &&
                Objects.equals(max, loadSla.max) &&
                Objects.equals(median, loadSla.median) &&
                Objects.equals(min, loadSla.min) &&
                Objects.equals(name, loadSla.name) &&
                Objects.equals(pct75, loadSla.pct75) &&
                Objects.equals(pct90, loadSla.pct90) &&
                Objects.equals(avg, loadSla.avg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customName, customValue, marginOfError, max, median, min, name, pct75, pct90, avg);
    }
}
