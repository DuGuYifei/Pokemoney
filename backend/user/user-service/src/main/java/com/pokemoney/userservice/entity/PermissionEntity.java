package com.pokemoney.userservice.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "t_permissions")
public class PermissionEntity {
    /**
     * PermissionEntity id.
     */
    @Id
    private Integer id;

    /**
     * PermissionEntity service name.
     */
    private String serviceName;

    /**
     * PermissionEntity permission bit.
     */
    private Integer permissionBit;
}
