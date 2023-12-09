package com.pokemoney.commons.transfer;

import java.util.Date;

/**
 * The transfer about snowflake id.
 */
public class SnowflakeTransfer {
    /**
     * The constant twepoch.
     */
    private static final long twepoch = 1288834974657L;

    /**
     * The constant workerIdBits.
     */
    public static Long getTimestampFromSnowflakeId(Long snowflakeId) {
        return (snowflakeId >> 22) + twepoch;
    }

    /**
     * Get Date from snowflake id.
     */
    public static Date getDateFromSnowflakeId(Long snowflakeId) {
        return new Date(getTimestampFromSnowflakeId(snowflakeId));
    }
}
