package com.pokemoney.hadoop.hbase.dto.sync;


import lombok.*;

/**
 * Sync ledger input DTO.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SyncLedgerInputDto {
    /**
     * ledger ID.
     */
    private Long ledgerId;
    /**
     * name
     */
    private String name;
    /**
     * budget
     */
    private Float budget;
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
