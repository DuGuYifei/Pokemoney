package com.pokemoney.userservice.service;

import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestRoleDto;
import com.pokemoney.userservice.entity.RoleEntity;
import com.pokemoney.userservice.feignclient.LeafClient;
import com.pokemoney.userservice.repository.RoleRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User role business logic service.
 */
@Service
public class RoleService {
    /**
     * Concurrent hash map of role. Used to accelerate the query of role.
     */
    @Getter
    private final static Map<String, RoleEntity> roleMap = new ConcurrentHashMap<>();

    /**
     * Repository of t_roles table.
     */
    private final RoleRepository roleRepository;

    /**
     * The leaf feign client.
     */
    private final LeafClient leafClient;

    /**
     * Constructor.
     *
     * @param roleRepository Repository of t_roles table.
     * @param leafClient    The leaf feign client.
     */
    public RoleService(RoleRepository roleRepository, LeafClient leafClient) {
        this.roleRepository = roleRepository;
        this.leafClient = leafClient;
        initRoleMap();
    }

    /**
     * Get role by role name.
     * Firstly, check the role map.
     * If not found, query from database and put it into the map.
     *
     * @param roleName Role name.
     * @return Role entity.
     */
    public RoleEntity getRole(String roleName) {
        RoleEntity roleEntity = roleMap.get(roleName);
        if (roleEntity != null) {
            return roleEntity;
        }
        throw new RuntimeException("Role not found");
    }

    /**
     * Save role to database and role map.
     *
     * @param requestRoleDto Role dto.
     */
    public RoleEntity saveRole(RequestRoleDto requestRoleDto) {
        RoleEntity roleEntity = RoleEntity.builder()
                .id(Integer.parseInt(leafClient.getSegmentId(Constants.USER_ROLE_IN_LEAF_KEY)))
                .roleName(requestRoleDto.getRoleName())
                .description(requestRoleDto.getDescription())
                .build();
        roleRepository.save(roleEntity);
        roleMap.put(roleEntity.getRoleName(), roleEntity);
        return roleEntity;
    }

    /**
     * Initialize role map.
     */
    private void initRoleMap() {
        roleRepository.findAll().forEach(roleEntity -> roleMap.put(roleEntity.getRoleName(), roleEntity));
    }
}
