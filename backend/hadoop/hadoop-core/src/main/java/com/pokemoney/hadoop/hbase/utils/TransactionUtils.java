package com.pokemoney.hadoop.hbase.utils;

import com.pokemoney.hadoop.hbase.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is used to generate row key for HBase.
 */
public class TransactionUtils {
    /**
     * Get the table name of transaction from timestamp in millis.
     *
     * @param millis the time millis
     * @return the table name
     */
    public static String GetTableNameFromTimestamp(Long millis) {
        Date date = new Date(millis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM");
        if (date.getTime() < Constants.TW_EPOCH) {
            return Constants.TRANSACTION_TABLE_PREFIX + "bf202312";
        }
        return Constants.TRANSACTION_TABLE_PREFIX + simpleDateFormat.format(date);
    }

    /**
     * Get the table name of transaction from snowflake id.
     *
     * @param snowflakeId the snowflake id
     * @return the table name
     */
    public static String GetTableNameFromSnowflakeId(Long snowflakeId) {
        return GetTableNameFromTimestamp(snowflakeId >> 22 + Constants.TW_EPOCH);
    }
}
