package com.pokemoney.hadoop.hbase;

/**
 * Constants values for hbase
 */
public class Constants {
    /**
     * The constant table name for ledger book.
     */
    public static final String LEDGER_BOOK_TABLE = "t_ledgers";
    /**
     * The constant table name for users.
     */
    public static final String USER_TABLE = "t_users";
    /**
     * The constant table name for fund.
     */
    public static final String FUND_TABLE = "t_funds";
    /**
     * The constant table name for operation.
     */
    public static final String OPERATION_TABLE = "t_operations";
    /**
     * The constant table name for transaction.
     */
    public static final String TRANSACTION_TABLE_PREFIX = "t_transactions";
    /**
     * The constant row key delimiter for composite row key.
     */
    public static final String ROW_KEY_DELIMITER = " ";
    /**
     * The minimum snowflake id. 2023-11-30
     */
    public static final Long MIN_SNOWFLAKE_ID = 1729999999999999999L;
}
