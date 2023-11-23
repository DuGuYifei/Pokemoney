package com.pokemoney.hadoop.hbase.dto.transaction;

import lombok.*;

/**
 * General transaction DTO.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionDto {
    /**
     * The transaction id.
     */
    private Long transactionId;
    /**
     * The transaction money.
     */
    private Float money;
    /**
     * The transaction type id.
     */
    private Integer typeId;
    /**
     * The transaction relevant entity.
     */
    private String relevantEntity;
    /**
     * The transaction comment.
     */
    private String comment;
    /**
     * The transaction fund id.
     */
    private Long fundId;
    /**
     * The transaction category id.
     */
    private Integer categoryId;
    /**
     * The transaction subcategory id.
     */
    private Long subcategoryId;
    /**
     * The transaction ledger id.
     */
    private Long ledgerId;
    /**
     * The transaction happen at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private String happenAt;
    /**
     * The transaction updated at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private String updatedAt;
    /**
     *
     */
}
