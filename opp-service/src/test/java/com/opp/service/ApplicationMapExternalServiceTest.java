package com.opp.service;

import com.opp.BaseIntegrationTest;
import com.opp.domain.ApplicationMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * Created by jpalmer on 1/10/17.
 */
/* don't run if not using an external app-map service */
@Ignore
public class ApplicationMapExternalServiceTest extends BaseIntegrationTest {
    private static final String TEST_RR_TESTAPP = "test-rr-testapp";
    @Autowired
    private ApplicationMapExternalService service;

    @Before
    public void setup(){
        // Clear out any test apps.
        cleanupTestApps();
    }

    @After
    public void tearDown(){
        // Clear out any test apps.
        cleanupTestApps();
    }

    @Test
    public void getAllApplications() {
        assertTrue(service.getAll().size() > 0);
    }

    @Test
    public void getApplicationsAppKey() {
        String appKey = "auth-platform";
        List<ApplicationMap> apps = service.getAllByAppKey(appKey);
        assertTrue(apps.size() > 0);
        assertTrue(apps.get(0).getAppKey().equalsIgnoreCase(appKey));
    }

    @Test
    public void getAppById() {
        ApplicationMap applicationMap = new ApplicationMap();

        applicationMap.setIsClientSide(true);
        applicationMap.setAppKey(TEST_RR_TESTAPP);
        applicationMap.setInCdPipelineClient(true);
        applicationMap.setKqiAppName("test-kqi-app-name");
        applicationMap.setSecurityId(234234234);
        applicationMap.setNewrelic("my-nr-name");
        Optional<ApplicationMap> setupApp = service.add(applicationMap);

        assertTrue("If the app failed to get added this step will fail.", setupApp.isPresent());
        int applicationId = setupApp.get().getId();

        Optional<ApplicationMap> response = service.getById(applicationId);
        assertTrue(response.isPresent());
        assertTrue(response.get().getId().equals(applicationId));
    }

    @Test
    public void addApp() {
        ApplicationMap applicationMap = new ApplicationMap();

        applicationMap.setIsClientSide(true);
        applicationMap.setAppKey(TEST_RR_TESTAPP);
        applicationMap.setInCdPipelineClient(true);
        applicationMap.setKqiAppName("test-kqi-app-name");
        applicationMap.setSecurityId(234234234);
        applicationMap.setNewrelic("my-nr-name");
        Optional<ApplicationMap> response = service.add(applicationMap);

        assertTrue(response.isPresent());
        assertTrue(response.get().getId() > 0);
        assertTrue(response.get().getAppKey().equalsIgnoreCase(TEST_RR_TESTAPP));

    }

    @Test
    public void updateApp() {
        ApplicationMap applicationMap = new ApplicationMap();

        applicationMap.setIsClientSide(true);
        applicationMap.setAppKey(TEST_RR_TESTAPP);
        applicationMap.setInCdPipelineClient(true);
        applicationMap.setKqiAppName("test-kqi-app-name");
        applicationMap.setSecurityId(234234234);
        applicationMap.setNewrelic("my-nr-name");
        Optional<ApplicationMap> response = service.add(applicationMap);

        assertTrue(response.isPresent());
        assertTrue(response.get().getId() > 0);
        assertTrue(response.get().getAppKey().equalsIgnoreCase(TEST_RR_TESTAPP));

        applicationMap.setSecurityId(1234);
        Optional<ApplicationMap> updateResponse = service.update(response.get().getId(), applicationMap);

        assertTrue(updateResponse.isPresent());
        assertTrue(updateResponse.get().getId().equals(response.get().getId()));
        assertTrue(updateResponse.get().getSecurityId().equals(1234));

    }

    private void cleanupTestApps() {
        service.getAllByAppKey(TEST_RR_TESTAPP).forEach(app -> service.delete(app.getId()));
    }
}
