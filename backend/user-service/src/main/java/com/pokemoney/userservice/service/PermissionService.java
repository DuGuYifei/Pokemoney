package com.pokemoney.userservice.service;

import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.commons.http.errors.GenericNotFoundError;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestPermissionDto;
import com.pokemoney.userservice.entity.PermissionEntity;
import com.pokemoney.userservice.feignclient.LeafClient;
import com.pokemoney.userservice.repository.PermissionRepository;
import com.pokemoney.userservice.vo.JwtInfo;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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
        permissionBitMap.put(requestPermissionDto.getServiceName(), requestPermissionDto.getPermissionBit());
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

    /**
     * Verify permission based on information of jwt.
     *
     * @param jwtInfo The {@link JwtInfo}.
     * @param service The service name.
     * @throws GenericForbiddenError If no permission to access this service.
     * @throws GenericNotFoundError  If service not found.
     */
    public void verifyPermissionByJwtInfo(JwtInfo jwtInfo, String service) throws GenericForbiddenError, GenericNotFoundError {
        BigInteger servicePermission = new BigInteger(jwtInfo.getPermission());
        Integer serviceBit = permissionBitMap.get(service);
        if (serviceBit == null) {
            throw new GenericNotFoundError("Invalid service.");
        }

        // if the service bit is not set to 1, throw exception.
        if ((servicePermission.and(BigInteger.ONE.shiftLeft(serviceBit))).equals(BigInteger.ZERO)) {
            throw new GenericForbiddenError("No permission to access this service.");
        }
    }
}
