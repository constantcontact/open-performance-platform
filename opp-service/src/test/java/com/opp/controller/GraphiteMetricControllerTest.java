package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.domain.ApplicationMap;
import com.opp.service.ApplicationMapExternalService;
import com.opp.service.ApplicationMapService;
import com.opp.util.MathUtil;
import com.opp.util.RestUtil;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

/**
 * Created by ctobe on 9/13/16.
 */
public class GraphiteMetricControllerTest extends BaseIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ApplicationMapService applicationMapService;


    @Value("${opp.appMap.useExternal}")
    private boolean useExternal;

    @Autowired
    private ApplicationMapExternalService externalService;

    private static final String APP_KEY = "junit_test_app_graphite_metric";

    @Before
    public void setUp() throws Exception {
        // clean out any existing instances of this app key
        if (useExternal) {
            cleanupTestApps();
        } else {
            jdbcTemplate.update("DELETE from application where app_key = ?", APP_KEY);
        }
    }

    @After
    public void tearDown() throws Exception {
        if (useExternal) {
            cleanupTestApps();
        }
    }

    @Test
    public void testCrud() throws Exception {
        ApplicationMap applicationMap = new ApplicationMap();
        applicationMap.setAppKey(APP_KEY);
        Optional<ApplicationMap> appMap = applicationMapService.add(applicationMap);
        String apiUrl = getBaseUrl() + "/loadsvc/v1/graphitemetrics";
        RestUtil.testFullCrud(apiUrl, apiUrl + "/{id}", "graphiteId", getData(appMap.get().getId(), appMap.get().getAppKey()), getData(appMap.get().getId(), appMap.get().getAppKey()));
    }

    private JSONObject getData(int applicationId, String appKey){
        JSONObject obj = new JSONObject();
        obj.put("applicationId", applicationId);
        obj.put("appKey", appKey);
        obj.put("name", "My Name " + MathUtil.getRandomInt(1, 100));
        obj.put("graphitePath", "this.is.my.path." + MathUtil.getRandomInt(1, 100));
        return obj;
    }

    private void cleanupTestApps() {
        externalService.getAllByAppKey(APP_KEY).forEach(app -> externalService.delete(app.getId()));
    }
}