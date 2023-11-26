package com.pokemoney.hadoop.hbase.dto.sync;

/**
 * Sync ledger input DTO.
 */
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
    private String owner;
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
