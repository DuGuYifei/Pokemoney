package com.pokemoney.userservice.service;

import com.pokemoney.commons.http.dto.ResponseDto;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestPermissionDto;
import com.pokemoney.userservice.dto.RequestRoleDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Notify eureka instances service.
 */
@Service
public class NotifyEurekaInstancesService {
    /**
     * Discovery client.
     */
    private final DiscoveryClient discoveryClient;

    /**
     * Rest template.
     */
    //TODO 删除这个在转为nacos之后，反正这里也报错用不了，需要restemplate自定义bean，因为cloud和直接的boot环境不一样
    private RestTemplate restTemplate;

    /**
     * Rest template builder
     */
    private final RestTemplateBuilder restTemplateBuilder;

    /**
     * Constructor.
     *
     * @param discoveryClient     discoveryClient
     * @param restTemplateBuilder
     */
    public NotifyEurekaInstancesService(DiscoveryClient discoveryClient, RestTemplateBuilder restTemplateBuilder) {
        this.discoveryClient = discoveryClient;
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Notify user service permission update event by calling endpoint.
     *
     * @param requestPermissionDto requestPermissionDto
     */
    public void notifyUserServicePermissionUpdateEvent(RequestPermissionDto requestPermissionDto) {
        List<ServiceInstance> userServiceInstances = discoveryClient.getInstances(Constants.SERVICE_NAME);
        for (ServiceInstance userServiceInstance : userServiceInstances) {
            String url = userServiceInstance.getUri() + "/api/v1/permission/update-map";
            restTemplate.postForEntity(url, requestPermissionDto, ResponseDto.class);
        }
    }

    /**
     * Notify user service role update event by calling endpoint.
     *
     * @param requestRoleDto requestRoleDto
     */
    public void notifyUserServiceRoleUpdateEvent(RequestRoleDto requestRoleDto) {
        List<ServiceInstance> userServiceInstances = discoveryClient.getInstances(Constants.SERVICE_NAME);
        for (ServiceInstance userServiceInstance : userServiceInstances) {
            String url = userServiceInstance.getUri() + "/api/v1/role/update-map";
            restTemplate.postForEntity(url, requestRoleDto, ResponseDto.class);
        }
    }
}
