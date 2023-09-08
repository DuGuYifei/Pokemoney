package com.pokemoney.userservice.controller;

import com.pokemoney.commons.redis.RedisKeyValueDto;
import com.pokemoney.commons.http.dto.ResponseDto;
import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.commons.http.errors.GenericInternalServerError;
import com.pokemoney.commons.http.errors.GenericNotFoundError;
import com.pokemoney.commons.mail.MailProperty;
import com.pokemoney.commons.mail.SmtpEmail;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestRegisterUserDto;
import com.pokemoney.userservice.dto.ResponseRegisterUserDto;
import com.pokemoney.userservice.dto.validation.RegisterValidationGroup;
import com.pokemoney.userservice.dto.validation.TryRegisterValidationGroup;
import com.pokemoney.userservice.entity.User;
import com.pokemoney.userservice.service.UserService;
import com.pokemoney.userservice.feignclient.RedisClient;
import com.pokemoney.userservice.utils.CodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * User controller.
 */
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    /**
     * User service.
     */
    private final UserService userService;

    /**
     * Smtp email component.
     */
    private final SmtpEmail smtpEmail;

    /**
     * Redis feign client.
     */
    private final RedisClient redisClient;

    /**
     * Constructor.
     *
     * @param userService User service.
     */
    public UserController(UserService userService, SmtpEmail smtpEmail, RedisClient redisClient) {
        this.userService = userService;
        this.smtpEmail = smtpEmail;
        this.redisClient = redisClient;
    }

    /**
     * Try to register user, send verification to user's email.
     *
     * @param requestRegisterUserDto The {@link RequestRegisterUserDto} to be registered.
     * @return The {@link ResponseDto<RequestRegisterUserDto>} of the result.
     * @throws GenericInternalServerError If failed in internal server.
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseDto<RequestRegisterUserDto>> tryRegister(@Validated(TryRegisterValidationGroup.class) RequestRegisterUserDto requestRegisterUserDto) throws GenericInternalServerError {
        long verificationCode = CodeGenerator.GenerateNumber(Constants.VERIFICATION_CODE_LENGTH);
        String verificationCodeStr = String.format("%0" + Constants.VERIFICATION_CODE_LENGTH + "d", verificationCode);
        RedisKeyValueDto redisKeyValueDto = RedisKeyValueDto.builder()
                .key(requestRegisterUserDto.getEmail())
                .value(verificationCodeStr)
                .timeout(600L)
                .prefix(Constants.REDIS_REGISTER_PREFIX)
                .build();
        try {
            redisClient.setKeyValue(redisKeyValueDto);
        } catch (Exception e) {
            log.error("Failed to send request to set verification code in redis.", e);
            throw new GenericInternalServerError("Something wrong when generating verification code.");
        }
        MailProperty mailProperty = MailProperty.builder()
                .to(new String[]{requestRegisterUserDto.getEmail()})
                .text("Your verification code is " + verificationCodeStr)
                .subject("Pokemoney Registration Verification")
                .build();
        try {
            smtpEmail.sendMimeMessage(mailProperty);
            log.debug("Send verification code to email - {}", requestRegisterUserDto.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification code to email - {}", requestRegisterUserDto.getEmail(), e);
            throw new GenericInternalServerError("Failed to send verification code to email.");
        }
        requestRegisterUserDto.setPassword("*");
        ResponseDto<RequestRegisterUserDto> responseSuccessDto = ResponseDto.<RequestRegisterUserDto>builder()
                .status(1)
                .message("Verification code has been sent to your email.")
                .data(requestRegisterUserDto)
                .build();
        return ResponseEntity.ok(responseSuccessDto);
    }

    /**
     * Verify the registration, and register user.
     *
     * @param requestRegisterUserDto The {@link RequestRegisterUserDto} to be registered.
     * @return The {@link ResponseRegisterUserDto} of the result.
     * @throws GenericInternalServerError If failed in internal server.
     * @throws GenericForbiddenError      If verification code expired.
     */
    @PostMapping("/register-verify")
    public ResponseEntity<ResponseDto<ResponseRegisterUserDto>> register(@Validated(RegisterValidationGroup.class) RequestRegisterUserDto requestRegisterUserDto) throws GenericInternalServerError, GenericForbiddenError {
        RedisKeyValueDto redisKeyValueDto = RedisKeyValueDto.builder()
                .key(requestRegisterUserDto.getEmail())
                .prefix(Constants.REDIS_REGISTER_PREFIX)
                .build();

        String verificationCode;
        ResponseDto<RedisKeyValueDto> responseFromRedis;

        try {
            responseFromRedis = redisClient.getKeyValue(redisKeyValueDto).getBody();
            verificationCode = String.valueOf(responseFromRedis.getData().getValue());
        } catch (feign.FeignException e) {
            if (e.status() == 404) {
                throw new GenericForbiddenError("Verification code expired or not exist. Please re-register.");
            } else {
                throw new RuntimeException(e);
            }
        } catch (GenericNotFoundError e) {
            throw new RuntimeException("Failed to get verification code from redis by GenericNotFoundError. But it shouldn't run to here.", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get verification code from redis.", e);
        }
        if (verificationCode == null) {
            throw new RuntimeException("Verification code not exist. But it shouldn't run to here.");
        }
        if (!verificationCode.equals(requestRegisterUserDto.getVerificationCode())) {
            System.out.println(verificationCode);
            System.out.println(requestRegisterUserDto.getVerificationCode());
            throw new GenericForbiddenError("Verification code not match.");
        }

        userService.setSegmentId(requestRegisterUserDto);
        User user = userService.createUser(requestRegisterUserDto);
        String jwt = user.generateJwtToken();
        ResponseRegisterUserDto responseRegisterUserDto = ResponseRegisterUserDto.builder().jwt(jwt).build();
        ResponseDto<ResponseRegisterUserDto> responseSuccessDto = ResponseDto.<ResponseRegisterUserDto>builder()
                .status(1)
                .message("Register successfully.")
                .data(responseRegisterUserDto)
                .build();
        return ResponseEntity.ok(responseSuccessDto);
    }
}
