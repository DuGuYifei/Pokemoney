package com.pokemoney.hadoop.hbase.dto.user;

import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import lombok.*;

import java.util.Arrays;
import java.util.List;

/**
 * user fund info DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFundInfoDto {
    /**
     * fund row keys
     */
    private List<String> funds;
    /**
     * deleted fund row keys
     */
    private List<String> delFunds;

    /**
     * from fund model
     *
     * @param fundModel fund model
     * @return fund info DTO
     */
    public static UserFundInfoDto fromFundModel(UserModel.FundInfoModel fundModel) {
        return UserFundInfoDto.builder()
                .funds(fundModel.getFunds())
                .delFunds(fundModel.getDelFunds())
                .build();
    }
}
