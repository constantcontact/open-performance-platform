package com.opp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * Created by ctobe on 9/16/16.
 */
@Service
public class GraphiteService {

    private static final Logger logger = LoggerFactory.getLogger(GraphiteService.class);

    @Value("${opp.graphite.host}")
    private String graphiteHost;

    @Value("${opp.graphite.port}")
    private int graphitePort;

    @Value("${opp.ux.graphiteMetricRootPath}")
    private String graphiteUxMetricRootPath;


    public void logToGraphite(String key, long value) {
        Map stats = new HashMap();
        stats.put(key, value);
        logToGraphite(stats);
    }

    public void logToGraphite(Map<String, Long> stats) {
        if (stats.isEmpty()) return;

        try {
            String nodeIdentifier = java.net.InetAddress.getLocalHost().getHostName();
            logToGraphite(nodeIdentifier, stats);
        } catch (Throwable t) {
            logger.error("Can't log to graphite", t);
        }
    }

    private void logToGraphite(String nodeIdentifier, Map<String, Long> stats) throws Exception {
        Long curTimeInSec = System.currentTimeMillis() / 1000;
        String lines = stats.entrySet().stream().map(
                e -> String.format("%s.%s %s %d", nodeIdentifier, e.getKey(), e.getValue(), curTimeInSec)
        ).collect(joining("\n"));
        logToGraphite(lines);
    }

    private void logToGraphite(String lines) throws Exception {
        logger.info("Writing [{}] to graphite", lines);
        Socket socket = new Socket(graphiteHost, graphitePort);
        try {
            Writer writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write(lines);
            writer.flush();
            writer.close();
        } finally {
            socket.close();
        }
    }


    public boolean logToGraphite(List<String> graphiteData) {
        String lines = graphiteData.stream().collect(joining("\n"));
        try {
            logToGraphite(lines);
            return true;
        } catch (Exception ex){
            logger.error("Failed to log to graphite");
            return false;
        }
    }

    public String buildMessage(String metricName, long metricValue){
        return buildMessage(metricName, metricValue, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public String buildMessage(String metricName, long metricValue, long time){
        return String.format("%s %s %s", metricName, metricValue, time);
    }

    public String buildUxMessage(String metricName, long metricValue){
        return buildUxMessage(metricName, metricValue, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public String buildUxMessage(String metricName, long metricValue, long time){
        return buildMessage(String.format("%s.%s", graphiteUxMetricRootPath, metricName), metricValue, time);
    }

}
