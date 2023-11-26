package com.pokemoney.hadoop.hbase.dto.sync;

import com.pokemoney.hadoop.hbase.dto.user.UserAppInfoDto;
import com.pokemoney.hadoop.hbase.dto.user.UserFundInfoDto;
import lombok.*;

/**
 * Sync fund input DTO.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SyncFundInputDto {
    /**
     * fund ID.
     */
    private Long fundId;
    /**
     * name
     */
    private String name;
    /**
     * balance
     */
    private Float balance;
    /**
     * owner
     */
    private Long owner;
    /**
     * create at
     */
    private Long createAt;
    /**
     * update at
     */
    private Long updateAt;
    /**
     * delete flag
     */
    private Integer delFlag;
}
