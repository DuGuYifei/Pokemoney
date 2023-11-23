package com.pokemoney.hadoop.hbase.utils;

/**
 * This class is used to generate row key for HBase.
 */
public class RowKey {
    /**
     * Get work id as region id from snowflake id.
     *
     * @param snowflakeId Snowflake id.
     * @return Region id.
     */
    public static String getRegionId (String snowflakeId) {
        // bit 43-52 is work id
        long id = Long.parseLong(snowflakeId);
        return String.valueOf((id >> 12) & 0b1111111111);
    }
}
