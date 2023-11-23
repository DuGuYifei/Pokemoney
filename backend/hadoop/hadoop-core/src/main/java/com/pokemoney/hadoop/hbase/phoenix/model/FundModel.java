package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.Getter;
import lombok.Setter;

/**
 * The fund model.
 */
@Getter
@Setter
public class FundModel {
    /**
     * region id of Row key.
     */
    private String regionId;

    /**
     * user id of Row key who create the fund.
     */
    private String userId;

    /**
     * fund id of Row key.
     */
    private String fundId;

    /**
     * Fund info.
     */
    private FundInfo fundInfo;

    /**
     * Update column family.
     */
    private UpdateInfoColumnFamily updateInfoColumnFamily;

    /**
     * Fund info.
     */
    @Getter
    @Setter
    public static class FundInfo {
        /**
         * Fund ID.
         */
        private Long id;
        /**
         * Fund name.
         */
        private String name;
        /**
         * Fund balance.
         */
        private Float balance;
        /**
         * Fund owner.
         */
        private String owner;
        /**
         * Fund editors.
         */
        private String[] editors;
        /**
         * Fund create at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
         */
        private Long createAt;
    }
}
