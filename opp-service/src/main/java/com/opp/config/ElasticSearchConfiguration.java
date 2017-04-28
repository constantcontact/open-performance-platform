package com.opp.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ctobe on 3/29/17.
 */
@Configuration
public class ElasticSearchConfiguration {
    @Value("${opp.elasticsearch.clusterName}")
    private String clusterName;
    @Value("${opp.elasticsearch.clusterNodes}")
    private String clusterNodes;


    @Bean
    public TransportClient client() throws UnknownHostException {
        String server = clusterNodes.split(":")[0];
        Integer port = Integer.parseInt(clusterNodes.split(":")[1]);
        Settings settings = Settings.builder()
                //.put("client.transport.sniff", true)
                .put("cluster.name", clusterName)
                .build();
        return new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(server), port));


    }

}
