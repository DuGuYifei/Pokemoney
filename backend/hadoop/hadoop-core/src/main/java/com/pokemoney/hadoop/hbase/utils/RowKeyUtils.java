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
     * Get ledger row key
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
     * Get fund row key
     *
     * @param regionId Region id.
     * @param userId Snowflake id.
     * @param fundId Fund id.
     * @return Row key.
     */
    public static String getFundRowKey(String regionId, String userId, String fundId) {
        return regionId + Constants.ROW_KEY_DELIMITER + userId + Constants.ROW_KEY_DELIMITER + fundId;
    }

    /**
     * Get transaction row key
     *
     * @param regionId Region id.
     * @param userId Snowflake id.
     * @param ledgerId Ledger id.
     * @param transactionId Transaction id.
     * @return Row key.
     */
    public static String getTransactionRowKey(String regionId, String userId, String ledgerId, String transactionId) {
        return regionId + Constants.ROW_KEY_DELIMITER + userId + Constants.ROW_KEY_DELIMITER + ledgerId + Constants.ROW_KEY_DELIMITER + transactionId;
    }
}
