package com.pokemoney.hadoop.hbase.dto.ledger;

import lombok.*;

import java.util.List;

/**
 * Update ledger DTO.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpsertLedgerDto {
    /**
     * region ID.
     */
    private Integer regionId;
    /**
     * user ID.
     */
    private Long userId;
    /**
     * ledger ID.
     */
    private Long ledgerId;
    /**
     * name
     */
    private String name;
    /**
     * user id of the owner of ledger book.
     */
    private Long owner;
    /**
     * editors of the editors of ledger book.
     */
    private List<Long> editors;
    /**
     * amount
     */
    private Float budget;
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
