package com.opp.dto.ux;

import com.opp.domain.ux.WptNavCategory;

import java.util.List;

/**
 * Created by ctobe on 10/26/16.
 */
public class WptNavApp {
    String appName;
    List<WptNavCategory> parametricOptions;

    public WptNavApp() {
    }

    public WptNavApp(String appName, List<WptNavCategory> parametricOptions) {
        this.appName = appName;
        this.parametricOptions = parametricOptions;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<WptNavCategory> getParametricOptions() {
        return parametricOptions;
    }

    public void setParametricOptions(List<WptNavCategory> parametricOptions) {
        this.parametricOptions = parametricOptions;
    }
}
