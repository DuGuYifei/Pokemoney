package com.pokemoney.user.service.service;

import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.leaf.service.api.LeafGetRequestDto;
import com.pokemoney.leaf.service.api.LeafTriService;
import com.pokemoney.user.service.Constants;
import com.pokemoney.user.service.dto.RequestRoleDto;
import com.pokemoney.user.service.repository.RoleRepository;
import com.pokemoney.user.service.vo.JwtInfo;
import com.pokemoney.user.service.entity.RoleEntity;
import lombok.Getter;
import org.apache.dubbo.config.annotation.DubboReference;
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
     * Key is role name.
     */
    @Getter
    private final static Map<String, RoleEntity> roleMap = new ConcurrentHashMap<>();

    /**
     * Repository of t_roles table.
     */
    private final RoleRepository roleRepository;

    /**
     * Leaf triple protocol service.
     */
    @DubboReference(version = "1.0.0", protocol = "tri", group = "leaf", timeout = 10000)
    private final LeafTriService leafTriService;

    /**
     * Constructor.
     *
     * @param roleRepository Repository of t_roles table..
     * @param leafTriService Leaf triple protocol service.
     */
    public RoleService(RoleRepository roleRepository, LeafTriService leafTriService) {
        this.roleRepository = roleRepository;
        this.leafTriService = leafTriService;
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
        initRoleMap();
        roleEntity = roleMap.get(roleName);
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
        LeafGetRequestDto leafGetRequestDto = LeafGetRequestDto.newBuilder()
                .setKey(Constants.USER_ROLE_IN_LEAF_KEY)
                .build();
        Integer id = Integer.parseInt(leafTriService.getSegmentId(leafGetRequestDto).getId());
        RoleEntity roleEntity = RoleEntity.builder()
                .id(id)
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

    /**
     * verify role by jwt info
     *
     * @param jwtInfo The {@link JwtInfo}.
     * @param requiredRoleLevelName The required role level name.
     * @throws GenericForbiddenError If no permission to access this service.
     */
    public void verifyRoleByJwtInfo(JwtInfo jwtInfo, String requiredRoleLevelName) throws GenericForbiddenError {
        RoleEntity roleEntity = getRole(jwtInfo.getRole());
        if (roleEntity == null) {
            throw new GenericForbiddenError("No role found for this user.");
        }
        RoleEntity requiredRoleEntity = getRole(requiredRoleLevelName);
        if (roleEntity.getPermissionLevel() < requiredRoleEntity.getPermissionLevel()) {
            throw new GenericForbiddenError("No permission to access this service.");
        }
    }
}
