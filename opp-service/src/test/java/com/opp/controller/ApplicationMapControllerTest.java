package com.opp.controller;

import com.opp.BaseIntegrationTest;
import com.opp.service.ApplicationMapExternalService;
import com.opp.util.RestUtil;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by ctobe on 6/22/16.
 */
public class ApplicationMapControllerTest extends BaseIntegrationTest {

    @Value("${opp.appMap.useExternal}")
    private boolean useExternal;

    @Autowired
    private ApplicationMapExternalService externalService;

    private static final String APP_KEY = "integration-test-app";

    @Before
    public void setUp() throws Exception {
        jdbcTemplate.update("REPLACE into application_team (team_name) VALUES(?)", APP_KEY);
    }

    @After
    public void tearDown(){
        if (useExternal) {
            cleanupTestApps();
        }
        jdbcTemplate.update("DELETE from application_team where team_name = ?", APP_KEY);

    }

    @Test
    public void testCrud() throws Exception {
        String apiUrl = getBaseUrl() + "/loadsvc/v1/applications";
        RestUtil.testFullCrud(apiUrl, apiUrl + "/{id}", "id", getCreateObj(), getUpdateObj());
    }

    private JSONObject getCreateObj(){
        JSONObject createObj = new JSONObject();
        createObj.put("appKey",APP_KEY);
        createObj.put("newrelic","my-test-app-id-for-nr");
        createObj.put("isClientSide",true);
        createObj.put("isServerSide",true);
        createObj.put("inCdPipelineClient",true);
        createObj.put("inCdPipelineServer",true);
        createObj.put("codeCoverageId", "123");
        createObj.put("securityId",123);
        createObj.put("regressionResultsId",123);
        createObj.put("kqiAppName", "kqi-test-app-name");
        createObj.put("teamName", APP_KEY); // just set team name to app key. doesn't matter, but it must exist
        return createObj;
    }

    private JSONObject getUpdateObj(){
        JSONObject updateObj = getCreateObj();
        updateObj.put("newrelic", "newrelic-updated");
        updateObj.put("isClientSide",false);
        updateObj.put("isServerSide",false);
        updateObj.put("inCdPipelineClient",false);
        updateObj.put("inCdPipelineServer",false);
        updateObj.put("codeCoverageId", "456");
        updateObj.put("securityId",456);
        updateObj.put("regressionResultsId",456);
        updateObj.put("kqiAppName", "kqi-test-app-name-updated");
        return updateObj;
    }

    private void cleanupTestApps() {
        externalService.getAllByAppKey(APP_KEY).forEach(app -> externalService.delete(app.getId()));
    }

}