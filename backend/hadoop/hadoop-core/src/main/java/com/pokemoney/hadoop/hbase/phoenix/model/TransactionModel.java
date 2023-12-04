package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.*;

import java.util.Date;

/**
 * The transaction model.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionModel {
    /**
     * region id of Row key.
     */
    private Integer regionId;

    /**
     * user id of Row key.
     */
    private Long userId;

    /**
     * reverse transaction id of Row key.
     */
    private String reverseTransactionId;

    /**
     * The transaction info.
     */
    private TransactionInfoModel transactionInfo;

    /**
     * Update by
     */
    private Long updateBy;

    /**
     * Update column family.
     */
    private UpdateInfoColumnFamily updateInfo;

    /**
     * The transaction info.
     */
    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TransactionInfoModel {
        /**
         * The transaction id.
         */
        private Long id;
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
        private Date happenAt;
    }

    public void generateReverseTransactionId(Long transactionId) {
        long reverseTransactionIdLong = Long.MAX_VALUE - transactionId;
        this.reverseTransactionId = String.valueOf(reverseTransactionIdLong);
    }
}
