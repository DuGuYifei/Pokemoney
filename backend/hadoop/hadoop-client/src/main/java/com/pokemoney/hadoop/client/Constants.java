package com.pokemoney.hadoop.client;

/**
 * Constants values for hadoop client
 */
public class Constants {
    /**
     * The constant key for get snowflake id for ledger book.
     */
    public static final String LEAF_HBASE_LEDGER_BOOK = "hbase_ledger";
    /**
     * The constant key for get snowflake id for fund.
     */
    public static final String LEAF_HBASE_FUND = "hbase_fund";
    /**
     * The constant key for get snowflake id for subcategory.
     */
    public static final String LEAF_HBASE_SUBCATEGORY = "hbase_subcategory";
    /**
     * The constant key for get snowflake id for transaction.
     */
    public static final String LEAF_HBASE_TRANSACTION = "hbase_transaction";
    /**
     * The constant key for get snowflake id for operation.
     */
    public static final String LEAF_HBASE_OPERATION = "hbase_operation";
    /**
     * The constant number for transaction lazy transfer.
     */
     public static final Integer TransactionLazyTransferNum = 2000;
}
