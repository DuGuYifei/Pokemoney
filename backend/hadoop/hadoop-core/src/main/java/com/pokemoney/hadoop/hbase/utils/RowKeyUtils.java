package com.pokemoney.hadoop.hbase.utils;

import com.pokemoney.hadoop.hbase.Constants;

/**
 * This class is used to generate row key for HBase.
 */
public class RowKeyUtils {
    /**
     * Get work id as region id from snowflake id.
     *
     * @param snowflakeId Snowflake id.
     * @return Region id.
     */
    public static Integer getRegionId (long snowflakeId) {
        // bit 43-52 is work id
        return (int)((snowflakeId >> 12) & 0b1111111111);
    }

    /**
     * Get user id from snowflake id.
     *
     * @param regionId Region id.
     * @param userId Snowflake id.
     * @param ledgerId Ledger id.
     * @return Row key.
     */
    public static String getLedgerRowKey(String regionId, String userId, String ledgerId) {
        return regionId + Constants.ROW_KEY_DELIMITER + userId + Constants.ROW_KEY_DELIMITER + ledgerId;
    }

    /**
     * Get fund id from snowflake id.
     *
     * @param regionId Region id.
     * @param userId Snowflake id.
     * @param fundId Fund id.
     * @return Row key.
     */
    public static String getFundRowKey(String regionId, String userId, String fundId) {
        return regionId + Constants.ROW_KEY_DELIMITER + userId + Constants.ROW_KEY_DELIMITER + fundId;
    }
}
