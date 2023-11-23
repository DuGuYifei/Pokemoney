package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.*;

/**
 * The user model.
 */
@Getter
@Setter
public class UserModel {
    /**
     * region id of Row key.
     */
    private String regionId;

    /**
     * user id of Row key.
     */
    private String userId;

    /**
     * User info.
     */
    private UserInfo userInfo;

    /**
     * Fund info.
     */
    private FundInfo fundInfo;

    /**
     * App info.
     */
    private AppInfo appInfo;

    /**
     * User info
     */
    public static class UserInfo {
        /**
         * User ID.
         */
        private Long id;
    }
    /**
     * Fund info
     */
    @Getter
    @Setter
    public static class FundInfo {
        /**
         * Fund IDs.
         */
        private String[] funds;
        /**
         * Deleted fund IDs.
         */
        private String[] delFunds;
    }
    /**
     * Ledger book info
     */
    @Getter
    @Setter
    public static class LedgerBookInfo {
        /**
         * Ledger book IDs.
         */
        private String[] ledgerBooks;
        /**
         * Deleted ledger book IDs.
         */
        private String[] delLedgerBooks;
    }
    /**
     * App info
     */
    @Getter
    @Setter
    public static class AppInfo {
        /**
         * category list
         */
        private CategoryModel[] categories;
    }
}
