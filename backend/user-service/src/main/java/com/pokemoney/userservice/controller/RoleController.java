package com.pokemoney.userservice.controller;

import com.pokemoney.commons.http.dto.ResponseDto;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestRoleDto;
import com.pokemoney.userservice.entity.RoleEntity;
import com.pokemoney.userservice.service.RoleService;
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
 * Role controller
 */
@RestController
@RequestMapping("/api/v1/role")
@Slf4j
public class RoleController {
    /**
     * roleEntity service.
     */
    private final RoleService roleService;

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
     * @param roleService roleEntity service.
     * @param discoveryClient   Discovery client.
     * @param restTemplate    Rest template.
     */
    public RoleController(RoleService roleService, DiscoveryClient discoveryClient, RestTemplate restTemplate) {
        this.roleService = roleService;
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    /**
     * Add new role.
     *
     * @param requestRoleDto add role dto.
     * @return Response entity.
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDto<?>> add(@Validated RequestRoleDto requestRoleDto) {
        RoleEntity role = roleService.saveRole(requestRoleDto);
        List<ServiceInstance> userServiceInstances = discoveryClient.getInstances(Constants.SERVICE_NAME);
        requestRoleDto.setId(role.getId());
        // notify other user service to update role bit map
        for (ServiceInstance userServiceInstance : userServiceInstances) {
            String url = userServiceInstance.getUri() + "/api/v1/role/update-map";
            restTemplate.postForEntity(url, requestRoleDto, ResponseDto.class);
        }
        return ResponseEntity.ok(ResponseDto.builder().message("Add role successfully.").status(1).build());
    }

    /**
     * Update role bit map.
     *
     * @param requestRoleDto role dto.
     * @return Response entity.
     */
    @PostMapping("/update-map")
    public ResponseEntity<ResponseDto<?>> updateBitMap(RequestRoleDto requestRoleDto) {
        RoleEntity role = RoleEntity.builder()
                .id(requestRoleDto.getId())
                .roleName(requestRoleDto.getRoleName())
                .description(requestRoleDto.getDescription())
                .build();
        RoleService.getRoleMap().put(role.getRoleName(), role);
        return ResponseEntity.ok(ResponseDto.builder().message("Update role map successfully.").status(1).build());
    }

}
