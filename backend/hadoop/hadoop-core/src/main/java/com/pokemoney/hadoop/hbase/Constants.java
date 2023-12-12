package com.pokemoney.hadoop.hbase;

/**
 * Constants values for hbase
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
     * The constant key for get snowflake id for invitation.
     */
    public static final String LEAF_HBASE_INVITATION = "hbase_invitation";
    /**
     * The constant key for get snowflake id for operation.
     */
    public static final String LEAF_HBASE_OPERATION = "hbase_operation";
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
    public static final String TRANSACTION_TABLE_PREFIX = "t_transactions_";
    /**
     * The constant row key delimiter for composite row key.
     */
    public static final String ROW_KEY_DELIMITER = " ";
    /**
     * The minimum snowflake id. 2023-11-30
     */
    public static final Long MIN_SNOWFLAKE_ID = 1729999999999999999L; //2023-11-30
    /**
     * The twepoch for snowflake id.
     */
    public static final Long TW_EPOCH = 1288834974657L; // 2010-11-04
}
