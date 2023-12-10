package com.pokemoney.hadoop.hbase.dto.user;

import lombok.*;

/**
 * user DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
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
     * update user info at
     */
    private Long updateUserInfoAt;
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
    /**
     * Notification in json
     */
    private String notificationJson;
}