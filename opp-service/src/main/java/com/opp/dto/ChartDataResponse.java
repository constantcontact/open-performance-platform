package com.opp.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by jhermida on 8/26/16.
 */
public class ChartDataResponse {
    private Chart chart;

    public ChartDataResponse(List<Map<String, String>> data, List<String> modelFields, List<LineSeries> series) {
        chart = new Chart();
        chart.setData(data);
        chart.setModelFields(modelFields);
        chart.setSeries(series);
    }

    public ChartDataResponse(String chartTitle, List<Map<String, String>> data, List<String> modelFields, List<LineSeries> series) {
        chart = new ChartTitle(chartTitle);
        chart.setData(data);
        chart.setModelFields(modelFields);
        chart.setSeries(series);
    }
    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public static class Chart {
        private List<Map<String, String>> data;
        private List<String> modelFields;
        private List<LineSeries> series;

        public List<Map<String, String>> getData() {
            return data;
        }

        public void setData(List<Map<String, String>> data) {
            this.data = data;
        }

        public List<String> getModelFields() {
            return modelFields;
        }

        public void setModelFields(List<String> modelFields) {
            this.modelFields = modelFields;
        }

        public List<LineSeries> getSeries() {
            return series;
        }

        public void setSeries(List<LineSeries> series) {
            this.series = series;
        }
    }

    public static class ChartTitle extends Chart {
        private String title;

        public ChartTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class LineSeries {
        private String type = "line";
        private String axis = "left";
        private String xField;
        private String yField;

        public LineSeries(String xField, String yField) {
            this.xField = xField;
            this.yField = yField;
        }
        public String getType() {
            return type;
        }

        public String getAxis() {
            return axis;
        }

        public String getxField() {
            return xField;
        }

        public String getyField() {
            return yField;
        }
    }
}
