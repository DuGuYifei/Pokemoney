package com.pokemoney.userservice.controller;

import com.pokemoney.commons.http.dto.ResponseDto;
import com.pokemoney.userservice.dto.RequestRoleDto;
import com.pokemoney.userservice.entity.RoleEntity;
import com.pokemoney.userservice.service.NotifyEurekaInstancesService;
import com.pokemoney.userservice.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Notify eureka instances service.
     */
    private final NotifyEurekaInstancesService notifyEurekaInstancesService;

    /**
     * Constructor.
     *
     * @param roleService                  roleEntity service.
     * @param notifyEurekaInstancesService Notify eureka instances service.
     */
    public RoleController(RoleService roleService, NotifyEurekaInstancesService notifyEurekaInstancesService) {
        this.roleService = roleService;
        this.notifyEurekaInstancesService = notifyEurekaInstancesService;
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
        requestRoleDto.setId(role.getId());
        notifyEurekaInstancesService.notifyUserServiceRoleUpdateEvent(requestRoleDto);
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
                .permissionLevel(requestRoleDto.getPermissionLevel())
                .description(requestRoleDto.getDescription())
                .build();
        RoleService.getRoleMap().put(role.getRoleName(), role);
        return ResponseEntity.ok(ResponseDto.builder().message("Update role map successfully.").status(1).build());
    }
}
