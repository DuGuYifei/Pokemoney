package com.pokemoney.hadoop.client.service;

import com.pokemoney.hadoop.client.exception.GenericGraphQlForbiddenException;
import com.pokemoney.hadoop.hbase.dto.fund.FundDto;
import com.pokemoney.user.service.api.UserTriService;
import com.pokemoney.user.service.api.VerifyUserJwtWithServiceNameRequestDto;
import com.pokemoney.user.service.api.VerifyUserJwtWithServiceNameResponseDto;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Auth service.
 */
@Slf4j
@Service
public class AuthService {
    /**
     * User tri service
     */
    @DubboReference(version = "1.0.0", group = "user", protocol = "tri", timeout = 10000)
    private final UserTriService userTriService;

    /**
     * Constructor
     *
     * @param userTriService user tri service
     */
    public AuthService(UserTriService userTriService) {
        this.userTriService = userTriService;
    }

    /**
     * Verify user.
     *
     * @param userId user id
     * @param auth   auth
     * @return verify user jwt with service name response dto
     * @throws GenericGraphQlForbiddenException generic graphql forbidden exception
     */
    public VerifyUserJwtWithServiceNameResponseDto verifyUser(Long userId, String auth) throws GenericGraphQlForbiddenException {
        if (auth == null || auth.isBlank()) {
            log.error("auth is null or blank");
            throw new GenericGraphQlForbiddenException("You are not authorized to access this resource");
        }
        VerifyUserJwtWithServiceNameResponseDto verifyUserJwtWithServiceResponseDto = userTriService.verifyUserJwt(
                VerifyUserJwtWithServiceNameRequestDto.newBuilder()
                        .setUserId(userId)
                        .setJwt(auth)
                        .setServiceName(System.getProperty("spring.application.name"))
                        .setNeedRole("user")
                        .build()
        );
        if (verifyUserJwtWithServiceResponseDto.getResponseCommonPart().getStatus() <= 0) {
            log.error("verifyUserJwtWithServiceResponseDto error: {}", verifyUserJwtWithServiceResponseDto);
            throw new GenericGraphQlForbiddenException(verifyUserJwtWithServiceResponseDto.getResponseCommonPart().getMessage());
        }
        return verifyUserJwtWithServiceResponseDto;
    }

    /**
     * Pre handle request.
     *
     * @param userId user id
     * @param auth   auth
     * @throws GenericGraphQlForbiddenException generic graphql forbidden exception
     */
    public void preHandleRequest(Long userId, String auth) throws GenericGraphQlForbiddenException {
        verifyUser(userId, auth);
    }
}
