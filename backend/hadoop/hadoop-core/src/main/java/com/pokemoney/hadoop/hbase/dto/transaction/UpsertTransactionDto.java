package com.pokemoney.hadoop.hbase.dto.transaction;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * upsert transaction DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpsertTransactionDto {
    /**
     * table name
     */
    private String tableName;
    /**
     * region id
     */
    private Integer regionId;
    /**
     * user id
     */
    private Long userId;
    /**
     * reverse transaction id
     */
    private Long reverseTransactionId;
    /**
     * transaction ID.
     */
    private Long transactionId;
    /**
     * amount of this transaction.
     */
    private Float money;
    /**
     * type ID of this transaction.
     */
    private Integer typeId;
    /**
     * relevant entity of this transaction.
     */
    private String relevantEntity;
    /**
     * comment of this transaction.
     */
    private String comment;
    /**
     * fund ID of this transaction.
     */
    private Long fundId;
    /**
     * category ID of this transaction.
     */
    private Integer categoryId;
    /**
     * subcategory ID of this transaction.
     */
    private Long subcategoryId;
    /**
     * ledger book ID of this transaction.
     */
    private Long ledgerId;
    /**
     * happen time of this transaction.
     */
    private Long happenAt;
    /**
     * update by whom
     */
    private Long updateBy;
    /**
     * update time of this transaction.
     */
    private Long updateAt;
    /**
     * deleted flag of this transaction.
     */
    private Integer delFlag;
}
