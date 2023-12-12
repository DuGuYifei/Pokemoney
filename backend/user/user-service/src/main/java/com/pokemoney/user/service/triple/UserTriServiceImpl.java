package com.pokemoney.user.service.triple;

import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.commons.http.errors.GenericNotFoundError;
import com.pokemoney.commons.proto.ResponseCommonPart;
import com.pokemoney.user.service.api.*;
import com.pokemoney.user.service.api.GetUserInfoByEmailRequestDto;
import com.pokemoney.user.service.api.GetUserInfoResponseDto;
import com.pokemoney.user.service.api.UserTriService;
import com.pokemoney.user.service.api.VerifyUserJwtWithServiceNameRequestDto;
import com.pokemoney.user.service.api.VerifyUserJwtWithServiceNameResponseDto;
import com.pokemoney.user.service.api.exceptions.UserTriRpcException;
import com.pokemoney.user.service.service.JwtService;
import com.pokemoney.user.service.service.PermissionService;
import com.pokemoney.user.service.service.UserService;
import com.pokemoney.user.service.vo.JwtInfo;
import com.pokemoney.user.service.Exceptions.JwtVerifyException;
import com.pokemoney.user.service.entity.UserEntity;
import com.pokemoney.user.service.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.StatusRpcException;

@Slf4j
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
            log.error("Invalid token: {}", jwt);
            throw new StatusRpcException(UserTriRpcException.INVALID_TOKEN);
        } catch (JwtVerifyException.InvalidUserException e) {
            throw new StatusRpcException(UserTriRpcException.INVALID_USER);
        }
        try {
            if (!needRole.isEmpty()) {
                roleService.verifyRoleByJwtInfo(jwtInfo, needRole);
            }
        } catch (GenericForbiddenError e) {
            log.error("User {} has no enough role to access this service", userId);
            throw new StatusRpcException(UserTriRpcException.WRONG_ROLE);
        }
        try {
            if (!serviceName.isEmpty()) {
                permissionService.verifyPermissionByJwtInfo(jwtInfo, serviceName);
            }
        } catch (GenericForbiddenError | GenericNotFoundError e) {
            log.error("User {} has no enough permission to access this service", userId);
            throw new StatusRpcException(UserTriRpcException.LOW_PERMISSION);
        }
        return VerifyUserJwtWithServiceNameResponseDto.newBuilder()
                .setResponseCommonPart(ResponseCommonPart.newBuilder().setStatus(1).setMessage("Success").build())
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
    public GetUserInfoResponseDto getUserInfo(com.pokemoney.user.service.api.GetUserInfoRequestDto request) {
        long userId = request.getUserId();
        UserEntity userEntity = userService.getUserById(userId);
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

    /**
     * Get user info by email.
     *
     * @param request {@link GetUserInfoByEmailRequestDto}
     * @return {@link GetUserInfoResponseDto}
     */
    @Override
    public GetUserInfoResponseDto getUserInfoByEmail(GetUserInfoByEmailRequestDto request) {
        String email = request.getEmail();
        UserEntity userEntity = userService.getUserByEmail(email);
        if (userEntity == null) {
            throw new StatusRpcException(UserTriRpcException.USER_NOT_FOUND);
        }
        return GetUserInfoResponseDto.newBuilder()
                .setUserId(userEntity.getId())
                .setEmail(userEntity.getEmail())
                .setUsername(userEntity.getUsername())
                .setRole(userEntity.getUserRole().getRoleName())
                .setPermission(userEntity.getServicePermission().toString())
                .build();
    }
}
