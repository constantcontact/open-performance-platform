package com.opp.dto.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

/**
 * Created by ctobe on 6/13/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUserTimings {
    String wptTestId;
    long completed;
    Run average;
    Run min;
    Run median;
    Run max;

    public String getWptTestId() {
        return wptTestId;
    }

    public void setWptTestId(String wptTestId) {
        this.wptTestId = wptTestId;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    public Run getAverage() {
        return average;
    }

    public void setAverage(Run average) {
        this.average = average;
    }

    public Run getMin() {
        return min;
    }

    public void setMin(Run min) {
        this.min = min;
    }

    public Run getMedian() {
        return median;
    }

    public void setMedian(Run median) {
        this.median = median;
    }

    public Run getMax() {
        return max;
    }

    public void setMax(Run max) {
        this.max = max;
    }

    public static class Run {
        View firstView;
        View repeatView;

        public View getFirstView() {
            return firstView;
        }

        public void setFirstView(View firstView) {
            this.firstView = firstView;
        }

        public View getRepeatView() {
            return repeatView;
        }

        public void setRepeatView(View repeatView) {
            this.repeatView = repeatView;
        }
    }

    public static class View {
        HashMap<String, Integer> userTimes;

        public HashMap<String, Integer> getUserTimes() {
            return userTimes;
        }

        public void setUserTimes(HashMap<String, Integer> userTimes) {
            this.userTimes = userTimes;
        }
    }
}


