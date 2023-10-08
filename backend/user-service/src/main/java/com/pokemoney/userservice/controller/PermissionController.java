package com.pokemoney.userservice.controller;

import com.pokemoney.commons.http.dto.ResponseDto;
import com.pokemoney.userservice.dto.RequestPermissionDto;
import com.pokemoney.userservice.service.NotifyEurekaInstancesService;
import com.pokemoney.userservice.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Notify eureka instances service.
     */
    private final NotifyEurekaInstancesService notifyEurekaInstancesService;

    /**
     * Constructor.
     *
     * @param permissionService            permissionEntity service.
     * @param notifyEurekaInstancesService Notify eureka instances service.
     */
    public PermissionController(PermissionService permissionService, NotifyEurekaInstancesService notifyEurekaInstancesService) {
        this.permissionService = permissionService;
        this.notifyEurekaInstancesService = notifyEurekaInstancesService;
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
        notifyEurekaInstancesService.notifyUserServicePermissionUpdateEvent(requestPermissionDto);
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
