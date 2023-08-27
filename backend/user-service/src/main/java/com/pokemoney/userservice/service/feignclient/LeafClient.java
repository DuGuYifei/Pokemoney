package com.pokemoney.userservice.service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Leaf client for feign client of leaf-service.
 * Setup strategy for feign client in application properties.
 */
@FeignClient(name = "leaf-service")
public interface LeafClient {
    /**
     * Get segment id from leaf-service.
     * @param key Key of segment.
     * @return Segment id.
     */
    @RequestMapping(value = "/api/snowflake/get/{key}")
    String getSnowflakeId(@PathVariable("key") String key);
}
