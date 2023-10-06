package com.pokemoney.userservice.controller;

import com.pokemoney.commons.http.dto.ResponseDto;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestPermissionDto;
import com.pokemoney.userservice.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Permission controller
 */
@RestController
@RequestMapping("/api/v1/permission")
@Slf4j
public class PermissionController {
    /**
     * permissionEntity service.
     */
    private final PermissionService permissionService;

    /**
     * Discovery client.
     */
    private final DiscoveryClient discoveryClient;

    /**
     * Rest template.
     */
    private final RestTemplate restTemplate;

    /**
     * Constructor.
     *
     * @param permissionService permissionEntity service.
     * @param discoveryClient   Discovery client.
     * @param restTemplate    Rest template.
     */
    public PermissionController(PermissionService permissionService, DiscoveryClient discoveryClient, RestTemplate restTemplate) {
        this.permissionService = permissionService;
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    /**
     * Add new permission bit.
     *
     * @param requestPermissionDto add permission dto.
     * @return Response entity.
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDto<?>> add(@Validated RequestPermissionDto requestPermissionDto) {
        permissionService.savePermission(requestPermissionDto);
        List<ServiceInstance> userServiceInstances = discoveryClient.getInstances(Constants.SERVICE_NAME);
        // notify other user service to update permission bit map
        for (ServiceInstance userServiceInstance : userServiceInstances) {
            String url = userServiceInstance.getUri() + "/api/v1/permission/update-map";
            restTemplate.postForEntity(url, requestPermissionDto, ResponseDto.class);
        }
        return ResponseEntity.ok(ResponseDto.builder().message("Add permission successfully.").status(1).build());
    }

    /**
     * Update permission bit map.
     *
     * @param requestPermissionDto permission dto.
     * @return Response entity.
     */
    @PostMapping("/update-map")
    public ResponseEntity<ResponseDto<?>> updateBitMap(@Validated RequestPermissionDto requestPermissionDto) {
        PermissionService.getPermissionBitMap().put(requestPermissionDto.getServiceName(), requestPermissionDto.getPermissionBit());
        return ResponseEntity.ok(ResponseDto.builder().message("Update permission bit map successfully.").status(1).build());
    }

}
