package com.pokemoney.user.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "t_roles")
public class RoleEntity {
    /**
     * RoleEntity id.
     */
    @Id
    private Integer id;

    /**
     * RoleEntity name.
     */
    @Column(unique = true)
    @JsonProperty("role_name")
    private String roleName;

    /**
     * Permission level
     */
    @JsonProperty("permission_level")
    private Integer permissionLevel;

    /**
     * RoleEntity description.
     */
    private String description;

    /**
     * RoleEntity users.
     */
    @OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY)
    private List<UserEntity> users;
}
