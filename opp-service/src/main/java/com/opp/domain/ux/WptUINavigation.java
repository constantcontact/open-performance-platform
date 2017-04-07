package com.opp.domain.ux;

/**
 * Created by ctobe on 4/7/17.
 */

/**
 * Used for the navigation UI
 */
public class WptUINavigation extends WptTestLabel {
    long testDate;

    public WptUINavigation(long testDate) {
        this.testDate = testDate;
    }

    public WptUINavigation(String fullLabel, long testDate) {
        super(fullLabel);
        this.testDate = testDate;
    }

    public long getTestDate() {
        return testDate;
    }

    public void setTestDate(long testDate) {
        this.testDate = testDate;
    }
}
