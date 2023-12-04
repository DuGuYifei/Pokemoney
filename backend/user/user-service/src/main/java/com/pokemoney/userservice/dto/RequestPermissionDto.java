package com.pokemoney.userservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for permission request
 */
@Getter
@Setter
public class RequestPermissionDto {
    /**
     * The permission bit, provided by user. Constraints:
     * Must be not null.
     * Must be greater than 1, 0 is basic.
     */
    @NotNull(message = "Permission bit must be not null.")
    @Min(value = 1, message = "Permission bit must be greater than 1, 0 is basic.")
    private Integer permissionBit;

    /**
     * The service name, provided by user. Constraints:
     * Must be not blank.
     * Must be less than 50 characters.
     */
    @NotBlank(message = "Service name must be not blank.")
    @Size(max = 20, message = "Service name must be less than 50 characters.")
    private String serviceName;
}
