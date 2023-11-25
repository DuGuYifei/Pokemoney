package com.pokemoney.hadoop.hbase.dto.category;

import lombok.*;

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
    private List<Long> deletedFundIds;
}
