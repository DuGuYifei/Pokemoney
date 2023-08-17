package com.pokemoney.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO for registering user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterUserDto {

    /**
     * The username of user, provided by user.
     * Must be between 4 and 20 characters.
     * Must be alphanumeric with underscore and hyphen.
     */
    @NotBlank
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username must be alphanumeric with underscore and hyphen.")
    private String username;

    /**
     * The password of user, provided by user.
     * Must be between 8 and 20 characters.
     */
    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;

    /**
     * The email of user, provided by user.
     * Must be valid email.
     */
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Email must be valid.")
    private String email;

}
