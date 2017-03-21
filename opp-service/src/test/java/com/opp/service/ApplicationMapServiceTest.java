package com.opp.service;

import com.opp.BaseIntegrationTest;
import com.opp.domain.ApplicationMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertTrue;

/**
 * Created by ctobe on 6/22/16.
 */
public class ApplicationMapServiceTest extends BaseIntegrationTest {

    @Autowired
    private ApplicationMapService service;

    @Autowired
    private ApplicationMapExternalService externalService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${opp.appMap.useExternal}")
    private boolean useExternal;

    private static final String APP_KEY = "junit-test-app";
    private ApplicationMap applicationMap = new ApplicationMap();

    @Before
    public void setUp() throws Exception {

        System.out.println("setting up the house");
        // clean out any existing instances of this app key
        if (useExternal) {
            cleanupTestApps();
        } else {
            jdbcTemplate.update("DELETE from application where app_key = ?", APP_KEY);
        }

        // add the object to insert
        applicationMap.setIsClientSide(true);
        applicationMap.setAppKey(APP_KEY);
        applicationMap.setInCdPipelineClient(true);
        applicationMap.setKqiAppName("test-kqi-app-name");
        applicationMap.setSecurityId(234234234);
        applicationMap.setNewrelic("my-nr-name");
    }

    @After
    public void tearDown() {
        if (useExternal) {
            cleanupTestApps();
        }
    }

    @Test
    public void getAllApplicationMapsByAppKey() throws Exception {
        ApplicationMap createdAppMap = service.add(applicationMap).get();
        assertTrue("Application map created", createdAppMap.getId() != null);
        assertTrue("Get all application maps", service.getAllByAppKey(APP_KEY).size() > 0);
    }

    private void cleanupTestApps() {
        externalService.getAllByAppKey(APP_KEY).forEach(app -> externalService.delete(app.getId()));
    }
}