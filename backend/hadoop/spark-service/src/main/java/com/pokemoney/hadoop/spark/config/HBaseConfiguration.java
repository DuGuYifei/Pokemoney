package com.pokemoney.hadoop.spark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HBaseConfiguration {
    /**
     * The HBase configuration.
     */
    @Bean
    public org.apache.hadoop.conf.Configuration hbaseConfig() {
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("hbase.zookeeper.quorum", "43.131.33.18");
        return configuration;
    }
}
