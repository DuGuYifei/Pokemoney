package com.pokemoney.hadoop.hbase.dto.user;

import lombok.*;

/**
 * user DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpsertUserDto {
    /**
     * region ID.
     */
    private Integer regionId;
    /**
     * user ID.
     */
    private Long userId;
    /**
     * email
     */
    private String email;
    /**
     * name
     */
    private String name;
    /**
     * fund info
     */
    private UserFundInfoDto fundInfo;
    /**
     * Ledger book info
     */
    private UserLedgerBookInfoDto ledgerInfo;
    /**
     * App info
     */
    private UpsertUserAppInfoDto appInfo;
}
