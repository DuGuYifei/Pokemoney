package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.Getter;
import lombok.Setter;

/**
 * The ledger book model.
 */
@Getter
@Setter
public class LedgerBookModel {
    /**
     * Region id of Row key.
     */
    private String regionId;

    /**
     * User id of Row key who create the ledger book.
     */
    private String userId;

    /**
     * Ledger book id of Row key.
     */
    private String ledgerIdRowKey;

    /**
     * Ledger book info.
     */
    private LedgerBookInfo ledgerBookInfo;

    /**
     * Update column family.
     */
    private UpdateInfoColumnFamily updateInfoColumnFamily;

    /**
     * Ledger book info.
     */
    @Getter
    @Setter
    public static class LedgerBookInfo {
        /**
         * The ledger id.
         */
        private Long id;
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
        private String owner;
        /**
         * The ledger editors.
         */
        private String[] editors;
        /**
         * The ledger created at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
         */
        private String createAt;
    }
}
