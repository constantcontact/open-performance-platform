package com.opp;

import com.mashape.unirest.http.Unirest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Base abstract class from which integration tests should extend.
 *
 * Any test extending from this base class will not run unless the following system properties are set:
 *
 * -Dtest-groups=integration-tests
 *
 * Created by ctobe on 8/30/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {OppApplication.class})
//@IfProfileValue(name = "test-groups", values = { "integration-tests" })
@ActiveProfiles("development")
public abstract class BaseIntegrationTest {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Value("${server.port}")
    private String port;

    private String domain = "http://localhost";
    private String baseUrl;

    @BeforeClass
    public static void preSetUp() throws Exception {
        Unirest.setDefaultHeader("accept", "application/json");
        Unirest.setDefaultHeader("content-type", "application/json");
    }

    public String getDomain() {
        return domain;
    }

    public String getBaseUrl() {
        return domain + ":" + port;
    }
}