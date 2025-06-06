package com.pokemoney.user.service.controller;

import com.pokemoney.commons.http.dto.ResponseDto;
import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.commons.http.errors.GenericInternalServerError;
import com.pokemoney.commons.mail.MailProperty;
import com.pokemoney.commons.mail.SmtpEmailService;
import com.pokemoney.user.service.dto.validation.RegisterValidationGroup;
import com.pokemoney.user.service.dto.validation.TryRegisterValidationGroup;
import com.pokemoney.user.service.service.PermissionService;
import com.pokemoney.user.service.dto.RequestLoginDto;
import com.pokemoney.user.service.dto.RequestRegisterCommonUserDto;
import com.pokemoney.user.service.dto.RequestVerifyLoginDto;
import com.pokemoney.user.service.dto.ResponseLoginDto;
import com.pokemoney.user.service.entity.RoleEntity;
import com.pokemoney.user.service.entity.UserEntity;
import com.pokemoney.user.service.service.JwtService;
import com.pokemoney.user.service.service.RoleService;
import com.pokemoney.user.service.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.UUID;


/**
 * User controller
 */
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    /**
     * UserEntity service.
     */
    private final UserService userService;

    /**
     * Jwt service.
     */
    private final JwtService jwtService;

    /**
     * RoleEntity service
     */
    private final RoleService roleService;

    /**
     * Permission service
     */
    private final PermissionService permissionService;

    /**
     * Smtp email service.
     */
    private final SmtpEmailService smtpEmailService;

    /**
     * Constructor.
     *
     * @param userService UserEntity service.
     */
    public UserController(UserService userService, JwtService jwtService, RoleService roleService, PermissionService permissionService, SmtpEmailService smtpEmailService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.smtpEmailService = smtpEmailService;
    }

    /**
     * Try to register common user, send verification to user's email.
     *
     * @param requestRegisterCommonUserDto The {@link RequestRegisterCommonUserDto} to be registered.
     * @return The {@link ResponseDto<RequestRegisterCommonUserDto>} of the result.
     * @throws GenericInternalServerError If failed in internal server.
     */
    @PostMapping("/register-try")
    public ResponseEntity<ResponseDto<RequestRegisterCommonUserDto>> tryRegister(@Validated(TryRegisterValidationGroup.class) @RequestBody RequestRegisterCommonUserDto requestRegisterCommonUserDto) throws GenericInternalServerError, GenericForbiddenError {
        String verificationCode = userService.tryRegister(requestRegisterCommonUserDto);

        MailProperty mailProperty = MailProperty.builder()
                .to(new String[]{requestRegisterCommonUserDto.getEmail()})
                .text("Your verification code is " + verificationCode)
                .subject("Pokemoney Registration Verification")
                .build();
        try {
            smtpEmailService.sendMimeMessage(mailProperty);
            log.debug("Send verification code to email - {}", requestRegisterCommonUserDto.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification code to email - {}", requestRegisterCommonUserDto.getEmail(), e);
            throw new GenericInternalServerError("Failed to send verification code to email.");
        }

        requestRegisterCommonUserDto.setPassword("*");
        ResponseDto<RequestRegisterCommonUserDto> responseSuccessDto = ResponseDto.<RequestRegisterCommonUserDto>builder()
                .status(1)
                .message("Verification code has been sent to your email.")
                .data(requestRegisterCommonUserDto)
                .build();
        return ResponseEntity.ok(responseSuccessDto);
    }

    /**
     * Verify the registration, and register common user.
     * Add token in the header authorization. Bearer jwt.
     *
     * @param requestRegisterCommonUserDto The {@link RequestRegisterCommonUserDto} to be registered.
     * @return The {@link ResponseLoginDto} of the result.
     * @throws GenericForbiddenError If verification code not exist or expired,
     *                              or not correct,
     *                              or email or username already exists.
     */
    @PostMapping("/register-verify")
    public ResponseEntity<ResponseDto<ResponseLoginDto>> verifyCommonUserRegister(@Validated(RegisterValidationGroup.class) @RequestBody RequestRegisterCommonUserDto requestRegisterCommonUserDto, HttpServletResponse response) throws GenericForbiddenError {
        userService.verifyRegister(requestRegisterCommonUserDto);

        // save user
        RoleEntity roleEntity = roleService.getRole("user");
        BigInteger permission = BigInteger.ONE.shiftLeft(permissionService.getPermissionBit("basic"));
        UserEntity userEntity = userService.createUser(requestRegisterCommonUserDto, roleEntity, permission);
        requestRegisterCommonUserDto.setPassword("*");
        requestRegisterCommonUserDto.setVerificationCode(null);
        ResponseDto<ResponseLoginDto> responseSuccessDto = ResponseDto.<ResponseLoginDto>builder()
                .status(1)
                .message("Register successfully.")
                .data(ResponseLoginDto.builder().id(userEntity.getId()).username(userEntity.getUsername()).email(userEntity.getEmail()).build())
                .build();
        UUID id = UUID.randomUUID();
        String jwt = jwtService.generateJwt(userEntity, id.toString());
        jwtService.storeJwtStatus(jwt, userEntity, id.toString());
        response.setHeader("Authorization", "Bearer " + jwt);
        return ResponseEntity.ok(responseSuccessDto);
    }

    /**
     * Login.
     *
     * @param requestLoginDto The {@link RequestLoginDto} to be verified.
     * @return The {@link ResponseLoginDto} of the result.
     * @throws GenericForbiddenError If login forbid.
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<ResponseLoginDto>> login(@Validated @RequestBody RequestLoginDto requestLoginDto, HttpServletResponse response) throws GenericForbiddenError {
        RequestVerifyLoginDto requestVerifyLoginDto = userService.generateVerifyLoginDto(requestLoginDto);
        UserEntity userEntity = userService.verifyLogin(requestVerifyLoginDto);

        UUID id = UUID.randomUUID();
        String jwt = jwtService.generateJwt(userEntity, id.toString());
        jwtService.storeJwtStatus(jwt, userEntity, id.toString());
        response.setHeader("Authorization", "Bearer " + jwt);
        ResponseLoginDto responseLoginDto = ResponseLoginDto.builder().id(userEntity.getId()).username(userEntity.getUsername()).email(userEntity.getEmail()).build();
        ResponseDto<ResponseLoginDto> responseSuccessDto = ResponseDto.<ResponseLoginDto>builder()
                .status(1)
                .message("Login successfully.")
                .data(responseLoginDto)
                .build();
        return ResponseEntity.ok(responseSuccessDto);
    }
}
