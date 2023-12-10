package com.pokemoney.hadoop.service.config;

import org.apache.phoenix.shaded.org.apache.curator.framework.CuratorFramework;
import org.apache.phoenix.shaded.org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.phoenix.shaded.org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Zookeeper config
 */
@Configuration
public class ZookeeperConfig {
    /**
     * Zookeeper connection string
     */
    @Value("${zookeeper.connect-string}")
    private String connectionString;
    /**
     * Zookeeper retry count
     */
    @Value("${zookeeper.retry.count}")
    private int retryCount;
    /**
     * Zookeeper retry elapsed time
     */
    @Value("${zookeeper.retry.time}")
    private int elapsedTimeMs;
    /**
     * Zookeeper session timeout
     */
    @Value("${zookeeper.session.timeout}")
    private int sessionTimeoutMs;
    /**
     * Zookeeper connection timeout
     */
    @Value("${zookeeper.connection.timeout}")
    private int connectionTimeoutMs;

    /**
     * Curator framework
     *
     * @return curator framework
     */
    @Bean
    public CuratorFramework curatorFramework() {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(elapsedTimeMs, retryCount);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .build();
        client.start();
        return client;
    }
}
