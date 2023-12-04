package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.*;

import java.sql.Array;

/**
 * The ledger book model.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LedgerModel {
    /**
     * Region id of Row key.
     */
    private Integer regionId;

    /**
     * User id of Row key who create the ledger book.
     */
    private Long userId;

    /**
     * Ledger book id of Row key.
     */
    private Long ledgerId;

    /**
     * Ledger book info.
     */
    private LedgerInfo ledgerInfo;

    /**
     * Update column family.
     */
    private UpdateInfoColumnFamily updateInfo;

    /**
     * Ledger book info.
     */
    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LedgerInfo {
        /**
         * The ledger name.
         */
        private String name;
        /**
         * The ledger budget.
         */
        private Float budget;
        /**
         * The ledger owner.
         */
        private Long owner;
        /**
         * The ledger editors.
         */
        private Array editors;
        /**
         * The ledger created at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
         */
        private Long createAt;
    }
}
