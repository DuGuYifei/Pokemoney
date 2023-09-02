package com.pokemoney.redisservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * This class is the DTO of register verification
 */
@Data
@Builder
public class RegisterVerificationDto {
    /**
     * User email. Constraints:
     * Must be not blank.
     * Must be valid email.
     * Must be less than 50 characters.
     */
    @NotBlank(message = "Email must be not blank.")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Email must be valid.")
    @Size(max = 50, message = "Email must be less than 50 characters.")
    private String email;

    /**
     * Verification code. Constraints:
     * Must be not blank.
     * Must be between 1 and 64 characters.
     */
    @NotBlank(message = "Verification code must be not blank.")
    @Size(min = 1, max = 64, message = "Verification code must be between 1 and 64 characters.")
    private String verificationCode;

    /**
     * Verification code timeout in unit of second. Constraints:
     * 1. not required.
     * 2. If not provided, the default is no timeout.
     * 3. If provided, must be between 1 and 2592000 seconds (30 days).
     */
    @Size(min = 1, max = 2592000, message = "Verification code timeout must be between 1 and 64 characters.")
    private Long timeout;
}
