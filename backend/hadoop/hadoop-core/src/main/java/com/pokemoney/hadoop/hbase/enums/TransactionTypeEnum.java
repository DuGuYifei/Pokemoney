package com.pokemoney.hadoop.hbase.enums;

/**
 * transaction type
 */
public enum TransactionType {
    /**
     * income
     */
    INCOME(1),
    /**
     * expense
     */
    EXPENSE(2),
    /**
     * receivable
     */
    RECEIVABLE(3),
    /**
     * payable
     */
    PAYABLE(4),
    /**
     * receivable_backs
     */
    RECEIVABLE_BACKS(5),
    /**
     * payable_backs
     */
    PAYABLE_BACKS(6),
    /**
     * self_transfer
     */
    SELF_TRANSFER(7);

    /**
     * value
     */
    private final int value;

    /**
     * constructor
     *
     * @param value value
     */
    TransactionType(int value) {
        this.value = value;
    }

    /**
     * get value
     *
     * @return value
     */
    public int getValue() {
        return value;
    }
}
