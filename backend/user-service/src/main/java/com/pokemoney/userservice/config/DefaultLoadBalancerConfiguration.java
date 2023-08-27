package com.pokemoney.userservice.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used to configure load balance.
 */
@Configuration(proxyBeanMethods = false)
@LoadBalancerClients(defaultConfiguration = LoadBalancerConfiguration.class)
public class DefaultLoadBalancerConfiguration {
}
