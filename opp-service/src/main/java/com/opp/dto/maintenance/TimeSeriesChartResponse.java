package com.opp.dto.maintenance;

import com.opp.dto.ChartDataResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by jhermida on 9/7/16.
 */
public class TimeSeriesChartResponse extends ChartDataResponse {
    private String title;

    public TimeSeriesChartResponse(List<Map<String, String>> data, List<String> modelFields, List<LineSeries> series) {
        super(data, modelFields, series);
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
