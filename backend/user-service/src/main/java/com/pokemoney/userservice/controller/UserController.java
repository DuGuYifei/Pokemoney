package com.pokemoney.userservice.controller;

import com.pokemoney.commons.dto.RedisKeyValueDto;
import com.pokemoney.commons.dto.ResponseSuccessDto;
import com.pokemoney.commons.errors.GenericInternalServerError;
import com.pokemoney.commons.mail.MailProperty;
import com.pokemoney.commons.mail.SmtpEmail;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestRegisterUserDto;
import com.pokemoney.userservice.dto.ResponseRegisterUserDto;
import com.pokemoney.userservice.dto.validation.RegisterValidationGroup;
import com.pokemoney.userservice.dto.validation.TryRegisterValidationGroup;
import com.pokemoney.userservice.entity.User;
import com.pokemoney.userservice.service.UserService;
import com.pokemoney.userservice.service.feignclient.RedisClient;
import com.pokemoney.userservice.utils.CodeGenerator;
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
     * @return The {@link ResponseSuccessDto} of the result.
     * @throws GenericInternalServerError If failed in internal server.
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseSuccessDto> tryRegister(@Validated(TryRegisterValidationGroup.class) RequestRegisterUserDto requestRegisterUserDto) throws GenericInternalServerError {
        int verificationCode = CodeGenerator.GenerateNumber(Constants.VERIFICATION_CODE_LENGTH);
        String verificationCodeStr = String.format("%0" + Constants.VERIFICATION_CODE_LENGTH + "d", verificationCode);
        RedisKeyValueDto redisKeyValueDto = RedisKeyValueDto.builder()
                .key(requestRegisterUserDto.getEmail())
                .value(verificationCodeStr)
                .timeout(600L)
                .prefix(Constants.REDIS_REGISTER_PREFIX)
                .build();
        boolean isSuccess = redisClient.setKeyValue(redisKeyValueDto).getStatusCode().is2xxSuccessful();
        if (!isSuccess) {
            throw new GenericInternalServerError("Failed to store verification code in redis.");
        }
        MailProperty mailProperty = MailProperty.builder()
                .to(new String[]{requestRegisterUserDto.getEmail()})
                .text("Your verification code is " + verificationCodeStr)
                .subject("Pokemoney Registration Verification")
                .build();
        smtpEmail.sendMimeMessage(mailProperty);
        requestRegisterUserDto.setPassword("*");
        ResponseSuccessDto responseSuccessDto = ResponseSuccessDto.builder()
                .status(200)
                .message("Verification code has been sent to your email.")
                .data(requestRegisterUserDto)
                .build();
        return ResponseEntity.ok(responseSuccessDto);
    }

    /**
     * Verify the registration, and register user.
     */
    @PostMapping("/register-verify")
    public ResponseEntity<ResponseRegisterUserDto> register(@Validated(RegisterValidationGroup.class) RequestRegisterUserDto requestRegisterUserDto) {
        // TODO: verify the email and verification code in redis
        userService.setSegmentId(requestRegisterUserDto);
        User user = userService.createUser(requestRegisterUserDto);
        String jwt = user.generateJwtToken();
        ResponseRegisterUserDto responseRegisterUserDto = ResponseRegisterUserDto.builder().jwt(jwt).build();
        return ResponseEntity.ok(responseRegisterUserDto);
    }
}
