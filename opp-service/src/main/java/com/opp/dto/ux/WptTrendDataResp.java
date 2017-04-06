package com.opp.dto.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ctobe on 9/28/16.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class WptTrendDataResp {

    private Chart chart = new Chart();
    private List<TestRunData> dataTable = new ArrayList<>();

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public List<TestRunData> getDataTable() {
        return dataTable;
    }

    public void setDataTable(List<TestRunData> dataTable) {
        this.dataTable = dataTable;
    }

    public static class Chart {
        private List<List<Long>> ttfbRange = new ArrayList<>();
        private List<List<Long>> ttfbLine = new ArrayList<>();
        private List<List<Long>> vcRange = new ArrayList<>();
        private List<List<Long>> vcLine = new ArrayList<>();
        private List<List<Long>> siRange = new ArrayList<>();
        private List<List<Long>> siLine = new ArrayList<>();
        private Map<String, List<List<Long>>> userTimings = new TreeMap<>();
        private Map<String, List<List<Long>>> userTimingsRange = new TreeMap<>();


        public List<List<Long>> getTtfbRange() {
            return ttfbRange;
        }

        public void setTtfbRange(List<List<Long>> ttfbRange) {
            this.ttfbRange = ttfbRange;
        }

        public List<List<Long>> getTtfbLine() {
            return ttfbLine;
        }

        public void setTtfbLine(List<List<Long>> ttfbLine) {
            this.ttfbLine = ttfbLine;
        }

        public List<List<Long>> getVcRange() {
            return vcRange;
        }

        public void setVcRange(List<List<Long>> vcRange) {
            this.vcRange = vcRange;
        }

        public List<List<Long>> getVcLine() {
            return vcLine;
        }

        public void setVcLine(List<List<Long>> vcLine) {
            this.vcLine = vcLine;
        }

        public List<List<Long>> getSiRange() {
            return siRange;
        }

        public void setSiRange(List<List<Long>> siRange) {
            this.siRange = siRange;
        }

        public List<List<Long>> getSiLine() {
            return siLine;
        }

        public void setSiLine(List<List<Long>> siLine) {
            this.siLine = siLine;
        }

        public Map<String, List<List<Long>>> getUserTimings() {
            return userTimings;
        }

        public void setUserTimings(Map<String, List<List<Long>>> userTimings) {
            this.userTimings = userTimings;
        }

        public Map<String, List<List<Long>>> getUserTimingsRange() {
            return userTimingsRange;
        }

        public void setUserTimingsRange(Map<String, List<List<Long>>> userTimingsRange) {
            this.userTimingsRange = userTimingsRange;
        }
    }

}

