package com.opp.dto;

import com.opp.domain.LoadTestAggregate;

/**
 * Created by ctobe on 11/22/16.
 */
public class LoadTestAggregateDataWithSlas extends LoadTestAggregate {
    private Integer loadSlaGroupId;
    private String slaCustomName;
    private Double slaCustomValue;
    private Double slaMarginOfError;
    private Integer slaMax;
    private Integer slaMedian;
    private Integer slaMin;
    private String slaName;
    private Integer slaPct75;
    private Integer slaPct90;
    private Integer slaAvg;

    public Integer getLoadSlaGroupId() {
        return loadSlaGroupId;
    }

    public void setLoadSlaGroupId(Integer loadSlaGroupId) {
        this.loadSlaGroupId = loadSlaGroupId;
    }

    public String getSlaCustomName() {
        return slaCustomName;
    }

    public void setSlaCustomName(String slaCustomName) {
        this.slaCustomName = slaCustomName;
    }

    public Double getSlaCustomValue() {
        return slaCustomValue;
    }

    public void setSlaCustomValue(Double slaCustomValue) {
        this.slaCustomValue = slaCustomValue;
    }

    public Double getSlaMarginOfError() {
        return slaMarginOfError;
    }

    public void setSlaMarginOfError(Double slaMarginOfError) {
        this.slaMarginOfError = slaMarginOfError;
    }

    public Integer getSlaMax() {
        return slaMax;
    }

    public void setSlaMax(Integer slaMax) {
        this.slaMax = slaMax;
    }

    public Integer getSlaMedian() {
        return slaMedian;
    }

    public void setSlaMedian(Integer slaMedian) {
        this.slaMedian = slaMedian;
    }

    public Integer getSlaMin() {
        return slaMin;
    }

    public void setSlaMin(Integer slaMin) {
        this.slaMin = slaMin;
    }

    public String getSlaName() {
        return slaName;
    }

    public void setSlaName(String slaName) {
        this.slaName = slaName;
    }

    public Integer getSlaPct75() {
        return slaPct75;
    }

    public void setSlaPct75(Integer slaPct75) {
        this.slaPct75 = slaPct75;
    }

    public Integer getSlaPct90() {
        return slaPct90;
    }

    public void setSlaPct90(Integer slaPct90) {
        this.slaPct90 = slaPct90;
    }

    public Integer getSlaAvg() {
        return slaAvg;
    }

    public void setSlaAvg(Integer slaAvg) {
        this.slaAvg = slaAvg;
    }
}
