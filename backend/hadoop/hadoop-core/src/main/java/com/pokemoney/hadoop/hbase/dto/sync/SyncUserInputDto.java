package com.pokemoney.hadoop.hbase.dto.sync;

import lombok.*;

/**
 * Sync user input DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SyncUserInputDto {
    /**
     * The user id.
     */
    private Long userId;
    /**
     * email
     */
    private String email;
    /**
     * The username.
     */
    private String name;
    /**
     * Update at
     */
    private Long updateAt;
}
