package com.pokemoney.hadoop.hbase.dto.fund;

import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import lombok.*;


import java.util.List;

/**
 * Update fund DTO.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpsertFundDto {
    /**
     * region ID.
     */
    private Integer regionId;
    /**
     * user ID.
     */
    private Long userId;
    /**
     * fund ID.
     */
    private Long fundId;
    /**
     * name
     */
    private String name;
    /**
     * user id of the owner of fund.
     */
    private Long owner;
    /**
     * editors of the editors of fund.
     */
    private List<Long> editors;
    /**
     * balance
     */
    private Float balance;
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
