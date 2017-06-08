package com.opp.dto.ux;

import java.util.List;

/**
 * Created by ctobe on 4/12/17.
 */
public class WptTrendMetric {

    private Long completedDate;
    private BasicMetric ttfb;
    private BasicMetric visuallyComplete;
    private BasicMetric speedIndex;
    private List<UserTimingMetric> userTimings;

    public WptTrendMetric() {
    }

    public WptTrendMetric(Long completedDate, BasicMetric ttfb, BasicMetric visuallyComplete, BasicMetric speedIndex, List<UserTimingMetric> userTimings) {
        this.completedDate = completedDate;
        this.ttfb = ttfb;
        this.visuallyComplete = visuallyComplete;
        this.speedIndex = speedIndex;
        this.userTimings = userTimings;
    }

    public Long getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Long completedDate) {
        this.completedDate = completedDate;
    }

    public BasicMetric getTtfb() {
        return ttfb;
    }

    public void setTtfb(BasicMetric ttfb) {
        this.ttfb = ttfb;
    }

    public BasicMetric getVisuallyComplete() {
        return visuallyComplete;
    }

    public void setVisuallyComplete(BasicMetric visuallyComplete) {
        this.visuallyComplete = visuallyComplete;
    }

    public BasicMetric getSpeedIndex() {
        return speedIndex;
    }

    public void setSpeedIndex(BasicMetric speedIndex) {
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

        public UserTimingMetric(Integer min, Integer max, Integer median, Double average, String name) {
            super(min, max, median, average);
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
        private Integer min;
        private Integer max;
        private Integer median;
        private Double average;


        public BasicMetric() {
        }

        public BasicMetric(Integer min, Integer max, Integer median, Double average) {
            this.min = min;
            this.max = max;
            this.median = median;
            this.average = average;
        }

        public Integer getMin() {
            return min;
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


