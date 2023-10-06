package com.pokemoney.userservice.service;

import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestPermissionDto;
import com.pokemoney.userservice.entity.PermissionEntity;
import com.pokemoney.userservice.feignclient.LeafClient;
import com.pokemoney.userservice.repository.PermissionRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User permission business logic service.
 */
@Service
public class PermissionService {
    /**
     * Concurrent hash map of permission bit. Used to accelerate the query of permission bit.
     */
    @Getter
    private final static Map<String, Integer> permissionBitMap = new ConcurrentHashMap<>();

    /**
     * Repository of t_permissions table.
     */
    private final PermissionRepository permissionRepository;

    /**
     * The leaf feign client.
     */
    private final LeafClient leafClient;

    /**
     * Constructor.
     *
     * @param permissionRepository Repository of t_permissions table.
     */
    public PermissionService(PermissionRepository permissionRepository, LeafClient leafClient) {
        this.permissionRepository = permissionRepository;
        initPermissionMap();
        this.leafClient = leafClient;
    }

    /**
     * Get permission bit by service name.
     * Directly check the permission bit map to accelerate the query.
     *
     * @param serviceName Service name.
     * @return Permission bit.
     */
    public Integer getPermissionBit(String serviceName) {
        Integer value = permissionBitMap.get(serviceName);
        if (value != null) {
            return value;
        }
        throw new RuntimeException("Permission not found");
    }

    /**
     * Save permission to database and permission bit map.
     *
     * @param requestPermissionDto Add permission dto.
     */
    public void savePermission(RequestPermissionDto requestPermissionDto) {
        Integer id = Integer.parseInt(leafClient.getSegmentId(Constants.USER_PERMISSION_IN_LEAF_KEY));
        permissionRepository.save(PermissionEntity.builder().id(id).permissionBit(requestPermissionDto.getPermissionBit()).serviceName(requestPermissionDto.getServiceName()).build());
    }

    /**
     * Init permission map
     */
    public void initPermissionMap() {
        List<PermissionEntity> permissionEntities = permissionRepository.findAll();
        for (PermissionEntity permissionEntity : permissionEntities) {
            permissionBitMap.put(permissionEntity.getServiceName(), permissionEntity.getId());
        }
    }
}
