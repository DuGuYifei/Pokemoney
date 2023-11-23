package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.Getter;
import lombok.Setter;

/**
 * The transaction model.
 */
@Getter
@Setter
public class TransactionModel {
    /**
     * region id of Row key.
     */
    private String regionId;

    /**
     * user id of Row key.
     */
    private String userId;

    /**
     * reverse transaction id of Row key.
     */
    private String reverseTransactionId;

    /**
     * The transaction info.
     */
    private TransactionInfo transactionInfo;

    /**
     * Update column family.
     */
    private UpdateInfoColumnFamily updateInfoColumnFamily;

    /**
     * The transaction info.
     */
    @Getter
    @Setter
    public static class TransactionInfo {
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
        private String happenAt;
    }

    public void generateReverseTransactionId(Long transactionId) {
        long reverseTransactionIdLong = Long.MAX_VALUE - transactionId;
        this.reverseTransactionId = String.valueOf(reverseTransactionIdLong);
    }
}
