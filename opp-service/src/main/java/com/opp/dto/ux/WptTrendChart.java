package com.opp.dto.ux;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ctobe on 4/12/17.
 */
public class WptTrendChart {


    private List<BasicMetric> ttfb = new ArrayList<>();
    private List<BasicMetric> visuallyComplete = new ArrayList<>();
    private List<BasicMetric> speedIndex = new ArrayList<>();
    private List<UserTimingMetric> userTimings = new ArrayList<>();

    public WptTrendChart() {
    }

    public WptTrendChart(List<BasicMetric> ttfb, List<BasicMetric> visuallyComplete, List<BasicMetric> speedIndex, List<UserTimingMetric> userTimings) {
        this.ttfb = ttfb;
        this.visuallyComplete = visuallyComplete;
        this.speedIndex = speedIndex;
        this.userTimings = userTimings;
    }

    public List<BasicMetric> getTtfb() {
        return ttfb;
    }

    public void setTtfb(List<BasicMetric> ttfb) {
        this.ttfb = ttfb;
    }

    public List<BasicMetric> getVisuallyComplete() {
        return visuallyComplete;
    }

    public void setVisuallyComplete(List<BasicMetric> visuallyComplete) {
        this.visuallyComplete = visuallyComplete;
    }

    public List<BasicMetric> getSpeedIndex() {
        return speedIndex;
    }

    public void setSpeedIndex(List<BasicMetric> speedIndex) {
        this.speedIndex = speedIndex;
    }

    public List<UserTimingMetric> getUserTimings() {
        return userTimings;
    }

    public void setUserTimings(List<UserTimingMetric> userTimings) {
        this.userTimings = userTimings;
    }

    public static class UserTimingMetric extends BasicMetric {
        String name;

        public UserTimingMetric(Long completedDate, Integer min, Integer max, Integer median, Double average, String name) {
            super(completedDate, min, max, median, average);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class BasicMetric {
        private Long completedDate;
        private Integer min;
        private Integer max;
        private Integer median;
        private Double average;


        public BasicMetric() {
        }

        public BasicMetric(Long completedDate, Integer min, Integer max, Integer median, Double average) {
            this.completedDate = completedDate;
            this.min = min;
            this.max = max;
            this.median = median;
            this.average = average;
        }

        public Integer getMin() {
            return min;
        }

        public Long getCompletedDate() {
            return completedDate;
        }

        public void setCompletedDate(Long completedDate) {
            this.completedDate = completedDate;
        }

        public void setMin(Integer min) {
            this.min = min;
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

        public Double getAverage() {
            return average;
        }

        public void setAverage(Double average) {
            this.average = average;
        }
    }

}


