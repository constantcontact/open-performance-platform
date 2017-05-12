package com.opp.config;

import com.opp.service.WptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by ctobe on 5/11/17.
 */
@Component
public class ScheduledTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    private static boolean needToRunStartupMethod = true;

    @Autowired
    private WptService wptService;

    @Scheduled(fixedRate = 50000000)
    public void init() throws IOException {
        if (needToRunStartupMethod) {
            runOnceOnlyOnStartup();
            needToRunStartupMethod = false;
        }
    }

    public void runOnceOnlyOnStartup() throws IOException {
        LOGGER.info("Verifying Elastic Search Indexes and Aliases");
        boolean res = wptService.initES();
        if(res) {
            LOGGER.info("ElasticSearch is initialized with the correct indexes and aliases");
        } else {
            LOGGER.error("Failed to initialize elastic search");
        }
    }

}