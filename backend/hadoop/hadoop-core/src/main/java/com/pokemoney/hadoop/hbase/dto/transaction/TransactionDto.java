package com.pokemoney.hadoop.hbase.dto.transaction;

import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import lombok.*;

/**
 * ledger book DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionDto {
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
