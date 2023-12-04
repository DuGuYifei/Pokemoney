package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.*;

import java.sql.Array;

/**
 * The fund model.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FundModel {
    /**
     * region id of Row key.
     */
    private Integer regionId;

    /**
     * user id of Row key who create the fund.
     */
    private Long userId;

    /**
     * fund id of Row key.
     */
    private Long fundId;

    /**
     * Fund info.
     */
    private FundInfo fundInfo;

    /**
     * Update column family.
     */
    private UpdateInfoColumnFamily updateInfo;

    /**
     * Fund info.
     */
    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FundInfo {
        /**
         * Fund name.
         */
        private String name;
        /**
         * Fund balance.
         */
        private Float balance;
        /**
         * Fund editors.
         */
        private Array editors;
        /**
         * Fund owner.
         */
        private Long owner;
        /**
         * Fund create at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
         */
        private Long createAt;
    }
}
