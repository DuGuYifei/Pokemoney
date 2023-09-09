package com.pokemoney.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for login request.
 */
@Getter
@Setter
public class RequestLoginDto {
    /**
     * The username or email of user, provided by user. Constraints:
     * Must be not blank.
     * Other constraints will be handled in {@link VerifyLoginDto}.
     */
    @NotBlank(message = "Username or email must be not blank.")
    private String usernameOrEmail;

    /**
     * The password of user, provided by user. Constraints:
     * Must be not blank.
     * Must be between 8 and 20 characters.
     */
    @NotBlank(message = "Password must be not blank.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;
}
