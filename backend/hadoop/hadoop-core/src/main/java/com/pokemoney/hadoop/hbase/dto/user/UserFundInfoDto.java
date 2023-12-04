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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFundInfoDto {
    /**
     * fund IDs
     */
    private List<Long> fundIds;
    /**
     * deleted fund IDs
     */
    private List<Long> delFundIds;

    /**
     * from fund model
     *
     * @param fundModel fund model
     * @return fund info DTO
     */
    public static UserFundInfoDto fromFundModel(UserModel.FundInfoModel fundModel) {
        return UserFundInfoDto.builder()
                .fundIds(Arrays.stream(fundModel.getFunds()).toList())
                .delFundIds(Arrays.stream(fundModel.getDelFunds()).toList())
                .build();
    }
}
