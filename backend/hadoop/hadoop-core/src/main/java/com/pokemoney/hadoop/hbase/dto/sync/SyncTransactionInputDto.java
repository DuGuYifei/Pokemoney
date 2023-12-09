package com.pokemoney.hadoop.hbase.dto.sync;

import lombok.*;



/**
 * Sync transaction input DTO.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SyncTransactionInputDto {
    /**
     * transaction ID.
     */
    private Long transactionId;
    /**
     * money
     */
    private Float money;
    /**
     * type ID
     */
    private Integer typeId;
    /**
     * relevant entity
     */
    private String relevantEntity;
    /**
     * comment
     */
    private String comment;
    /**
     * fund ID
     */
    private Long fundId;
    /**
     * category ID
     */
    private Integer categoryId;
    /**
     * subcategory ID
     */
    private Long subcategoryId;
    /**
     * ledger ID
     */
    private Long ledgerId;
    /**
     * happen at
     */
    private Long happenAt;
    /**
     * update at
     */
    private Long updateAt;
    /**
     * delete flag
     */
    private Integer delFlag;
}
