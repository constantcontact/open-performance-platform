package com.opp.domain;

/**
 * Created by jhermida on 9/7/16.
 */
public class ChartDetails {
    private int loadTestId;
    private String yAxis;
    private String xAxis;
    private String plotBy;
    private String trendingBy;

    public ChartDetails() {
        // set defaults
        this.xAxis = "start_time";
        this.plotBy = "transaction_name";
        this.trendingBy = "test_name";
    }

    public ChartDetails(int loadTestId, String yAxis) {
        // set defaults
        this.xAxis = "start_time";
        this.plotBy = "transaction_name";
        this.trendingBy = "test_name";
        this.loadTestId = loadTestId;
        this.yAxis = yAxis;
    }

    public int getLoadTestId() {
        return loadTestId;
    }

    public void setLoadTestId(int loadTestId) {
        this.loadTestId = loadTestId;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public String getPlotBy() {
        return plotBy;
    }

    public void setPlotBy(String plotBy) {
        this.plotBy = plotBy;
    }

    public String getTrendingBy() {
        return trendingBy;
    }

    public void setTrendingBy(String trendingBy) {
        this.trendingBy = trendingBy;
    }

//    public enum Plot {
//        transaction_name
//    }
//    public enum TrendOn {
//        test_name,
//        test_sub_name,
//        vuser_count
//    }
//
//    public enum XAxis {
//        start_time,
//        end_time
//    }
//
//    public enum YAxis {
//        call_count,
//        resp_avg,
//        resp_max,
//        resp_min,
//        resp_median,
//        resp_pct75,
//        resp_pct90,
//        resp_stddev,
//        total_bytes_received,
//        total_bytes_sent,
//        tps_median,
//        tps_max,
//        error_count
//    }
}
