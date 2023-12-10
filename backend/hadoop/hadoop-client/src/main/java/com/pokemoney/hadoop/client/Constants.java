package com.pokemoney.hadoop.client;


import lombok.Value;

/**
 * Constants values for hadoop client
 */
public class Constants {
    /**
     * The constant number for transaction lazy transfer.
     */
     public static final Integer TransactionLazyTransferNum = 2000;
    /**
     * The biggest number of preset subcategory id.
     */
    public static final Long MAX_PRESET_SUBCATEGORY_ID = 100L;
    /**
     * The constant name of service in permission table.
     */
    public static final String SERVICE_NAME_PERMISSION_TABLE = "hadoop-client";
}
