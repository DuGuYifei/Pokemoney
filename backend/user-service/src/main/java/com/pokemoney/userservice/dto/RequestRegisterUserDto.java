package com.pokemoney.userservice.dto;

import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.validation.RegisterValidationGroup;
import com.pokemoney.userservice.dto.validation.TryRegisterValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for registering user request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestRegisterUserDto {

    /**
     * User id. Provided by leaf-service.
     * Must be null when validating it from user.
     */
    @Null(groups = {TryRegisterValidationGroup.class, RegisterValidationGroup.class}, message = "User id must be null.")
    private Long id;

    /**
     * The username of user, provided by user.
     * Must be not blank.
     * Must be between 4 and 20 characters.
     * Must be alphanumeric with underscore and hyphen.
     */
    @NotBlank(groups = {TryRegisterValidationGroup.class, RegisterValidationGroup.class}, message = "Username must be not blank.")
    @Size(groups = {TryRegisterValidationGroup.class, RegisterValidationGroup.class}, min = 4, max = 20, message = "Username must be between 4 and 20 characters.")
    @Pattern(groups = {TryRegisterValidationGroup.class, RegisterValidationGroup.class}, regexp = "^[a-zA-Z0-9_-]+$", message = "Username must be alphanumeric or underscore and hyphen.")
    private String username;

    /**
     * The password of user, provided by user.
     * Must be not blank.
     * Must be between 8 and 20 characters.
     */
    @NotBlank(groups = {TryRegisterValidationGroup.class, RegisterValidationGroup.class}, message = "Password must be not blank.")
    @Size(groups = {TryRegisterValidationGroup.class, RegisterValidationGroup.class}, min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;

    /**
     * The email of user, provided by user.
     * Must be not blank.
     * Must be valid email.
     */
    @NotBlank(groups = {TryRegisterValidationGroup.class, RegisterValidationGroup.class}, message = "Email must be not blank.")
    @Pattern(groups = {TryRegisterValidationGroup.class, RegisterValidationGroup.class}, regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Email must be valid.")
    @Size(groups = {TryRegisterValidationGroup.class, RegisterValidationGroup.class}, max = 50, message = "Email must be less than 50 characters.")
    private String email;

    /**
     * The verification code of registration,
     * send to user's email and provided by user again.
     * It must be {@link Constants#VERIFICATION_CODE_LENGTH}. digits.
     */
    @Null(groups = TryRegisterValidationGroup.class, message = "Verification code must be null.")
    @NotBlank(groups = RegisterValidationGroup.class, message = "Verification code must be not blank.")
    @Pattern(groups = RegisterValidationGroup.class, regexp = "^[0-9]{" + Constants.VERIFICATION_CODE_LENGTH + "}$", message = "Verification code must be " + Constants.VERIFICATION_CODE_LENGTH + " digits.")
    private String verificationCode;
}
