package com.pokemoney.hadoop.hbase.dto.user;

import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import lombok.*;

/**
 * user DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
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
    private UserAppInfoDto appInfo;

    public static UserDto fromUserModel(UserModel userModel) {
        return UserDto.builder()
                .userId(userModel.getUserId())
                .email(userModel.getUserInfo().getEmail())
                .name(userModel.getUserInfo().getName())
                .fundInfo(UserFundInfoDto.fromFundModel(userModel.getFundInfo()))
                .ledgerInfo(UserLedgerBookInfoDto.fromLedgerBookModel(userModel.getLedgerInfo()))
                .appInfo(UserAppInfoDto.fromAppModel(userModel.getAppInfo()))
                .build();
    }
}
