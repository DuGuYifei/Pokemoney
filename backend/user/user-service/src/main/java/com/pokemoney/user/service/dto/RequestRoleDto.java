package com.pokemoney.user.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemoney.user.service.dto.validation.AddRoleValidationGroup;
import com.pokemoney.user.service.dto.validation.UpdateRoleValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for role request
 */
@Getter
@Setter
public class RequestRoleDto {
    /**
     * ID of role. Constraints:
     * When add new role, it should be null.
     * When update role, it should be not null.
     */
    @Null(groups = AddRoleValidationGroup.class, message = "Role id must be null, you don't need to pass this param.")
    @NotNull(groups = UpdateRoleValidationGroup.class, message = "Role id must be not null.")
    private Integer id;

    /**
     * The role name, provided by user. Constraints:
     * Must be not blank.
     * Must be less than 20 characters.
     */
    @NotBlank(message = "Role name must be not blank.")
    @Size(max = 20, message = "Role name must be less than 20 characters.")
    @JsonProperty("role_name")
    private String roleName;

    /**
     * The permission level, provided by user. Constraints:
     * Must be not null.
     */
    @NotNull(message = "Permission level must be not null.")
    @JsonProperty("permission_level")
    private Integer permissionLevel;

    /**
     * The role description, provided by user. Constraints:
     * Must be not blank.
     * Must be less than 50 characters.
     */
    @NotBlank(message = "Role description must be not blank.")
    @Size(max = 50, message = "Role description must be less than 50 characters.")
    private String description;
}
