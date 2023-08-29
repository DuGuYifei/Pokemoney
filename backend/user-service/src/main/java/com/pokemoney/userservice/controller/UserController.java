package com.pokemoney.userservice.controller;

import com.pokemoney.commons.mail.MailDto;
import com.pokemoney.commons.mail.SmtpEmail;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestRegisterUserDto;
import com.pokemoney.userservice.dto.ResponseRegisterUserDto;
import com.pokemoney.userservice.dto.validation.RegisterValidationGroup;
import com.pokemoney.userservice.dto.validation.TryRegisterValidationGroup;
import com.pokemoney.userservice.entity.User;
import com.pokemoney.userservice.service.UserService;
import com.pokemoney.userservice.utils.CodeGenerator;
import com.pokemoney.userservice.utils.dto.ResponseSuccessDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller provides APIs for user entity.
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
     * Constructor.
     *
     * @param userService User service.
     */
    public UserController(UserService userService, SmtpEmail smtpEmail) {
        this.userService = userService;
        this.smtpEmail = smtpEmail;
    }

    /**
     * Try to register user, send verification to user's email.
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseSuccessDto> tryRegister(@Validated(TryRegisterValidationGroup.class) RequestRegisterUserDto requestRegisterUserDto) {
        int verificationCode = CodeGenerator.GenerateNumber(Constants.VERIFICATION_CODE_LENGTH);
        String verificationCodeStr = String.format("%0" + Constants.VERIFICATION_CODE_LENGTH + "d", verificationCode);
        // TODO: store the email and verification code in redis, ans set expiration
        MailDto mailDto = MailDto.builder()
                .to(new String[]{requestRegisterUserDto.getEmail()})
                .subject("Pokemoney Registration Verification")
                .text("Your verification code is " + verificationCodeStr)
                .build();
        smtpEmail.sendMimeMessage(mailDto);
        ResponseSuccessDto responseSuccessDto = ResponseSuccessDto.builder()
                .message("Verification code has been sent to your email.")
                .status(200)
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
