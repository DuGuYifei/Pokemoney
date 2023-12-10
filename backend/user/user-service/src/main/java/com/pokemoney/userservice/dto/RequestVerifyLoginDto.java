package com.pokemoney.userservice.dto;

import com.pokemoney.commons.validation.constraints.AtLeastOneNotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for verify login request.
 */
@Builder
@Setter
@Getter
@AtLeastOneNotBlank(fields = {"email", "username"}, message = "Email or username must be not blank.")
public class RequestVerifyLoginDto {
    /**
     * The email of user, provided by user. Constraints:
     * Must be not blank (has been verified in {@link RequestLoginDto}).
     * Must be valid email.
     * Must be less than 50 characters.
     */
    @Size(max = 50, message = "Email must be less than 50 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$", message = "Email must be valid.")
    private String email;

    /**
     * The username of user, provided by user. Constraints:
     * Must be not blank (has been verified in {@link RequestLoginDto}).
     * Must be between 4 and 20 characters.
     * Must be alphanumeric with underscore and hyphen.
     */
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username must be alphanumeric or underscore and hyphen.")
    private String username;

    /**
     * The password of user, provided by user. Constraints:
     * The constraints have been handled by {@link RequestLoginDto}.
     * Must be not blank.
     * Must be between 8 and 20 characters.
     */
    private String password;
}
