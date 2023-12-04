package com.pokemoney.userservice.triple;

import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.commons.http.errors.GenericNotFoundError;
import com.pokemoney.user.service.api.*;
import com.pokemoney.user.service.api.exceptions.UserTriRpcException;
import com.pokemoney.userservice.Exceptions.JwtVerifyException;
import com.pokemoney.userservice.entity.UserEntity;
import com.pokemoney.userservice.service.JwtService;
import com.pokemoney.userservice.service.PermissionService;
import com.pokemoney.userservice.service.RoleService;
import com.pokemoney.userservice.service.UserService;
import com.pokemoney.userservice.vo.JwtInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.StatusRpcException;

@DubboService(version = "1.0.0", protocol = "tri", group = "user", timeout = 10000)
public class UserTriServiceImpl implements UserTriService {
    /**
     * JWT service.
     */
    private final JwtService jwtService;

    /**
     * Role service.
     */
    private final RoleService roleService;

    /**
     * Permission service.
     */
    private final PermissionService permissionService;

    /**
     * User service.
     */
    private final UserService userService;

    /**
     * Constructor.
     *
     * @param jwtService        JWT service.
     * @param roleService       Role service.
     * @param permissionService Permission service.
     * @param userService       User service.
     */
    public UserTriServiceImpl(JwtService jwtService, RoleService roleService, PermissionService permissionService, UserService userService) {
        this.jwtService = jwtService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.userService = userService;
    }

    /**
     * Verify user jwt.
     *
     * @param request {@link VerifyUserJwtWithServiceNameRequestDto}
     * @return {@link VerifyUserJwtWithServiceNameResponseDto}
     */
    @Override
    public VerifyUserJwtWithServiceNameResponseDto verifyUserJwt(VerifyUserJwtWithServiceNameRequestDto request) {
        String jwt = request.getJwt().replace("Bearer ", "");
        long userId = request.getUserId();
        String serviceName = request.getServiceName();
        String needRole = request.getNeedRole();
        JwtInfo jwtInfo;
        try {
            jwtInfo = jwtService.verifyUserJwt(jwt, Long.toString(userId));
        } catch (JwtVerifyException.InvalidTokenException e) {
            throw new StatusRpcException(UserTriRpcException.INVALID_TOKEN);
        } catch (JwtVerifyException.InvalidUserException e) {
            throw new StatusRpcException(UserTriRpcException.INVALID_USER);
        }
        try {
            if (!needRole.isEmpty()) {
                roleService.verifyRoleByJwtInfo(jwtInfo, needRole);
            }
        } catch (GenericForbiddenError e) {
            throw new StatusRpcException(UserTriRpcException.WRONG_ROLE);
        }
        try {
            if (!serviceName.isEmpty()) {
                permissionService.verifyPermissionByJwtInfo(jwtInfo, serviceName);
            }
        } catch (GenericForbiddenError | GenericNotFoundError e) {
            throw new StatusRpcException(UserTriRpcException.LOW_PERMISSION);
        }
        return VerifyUserJwtWithServiceNameResponseDto.newBuilder()
                .setUserId(userId)
                .setEmail(jwtInfo.getEmail())
                .setUsername(jwtInfo.getUsername())
                .setRole(jwtInfo.getRole())
                .setPermission(jwtInfo.getPermission())
                .build();
    }

    /**
     * Get user info.
     *
     * @param request {@link GetUserInfoRequestDto}
     * @return {@link GetUserInfoResponseDto}
     */
    @Override
    public GetUserInfoResponseDto getUserInfo(GetUserInfoRequestDto request) {
        long userId = request.getUserId();
        UserEntity userEntity = userService.ge(userId);
        if (userEntity == null) {
            throw new StatusRpcException(UserTriRpcException.USER_NOT_FOUND);
        }
        return GetUserInfoResponseDto.newBuilder()
                .setUserId(userId)
                .setEmail(userEntity.getEmail())
                .setUsername(userEntity.getUsername())
                .setRole(userEntity.getUserRole().getRoleName())
                .setPermission(userEntity.getServicePermission().toString())
                .build();
    }
}
