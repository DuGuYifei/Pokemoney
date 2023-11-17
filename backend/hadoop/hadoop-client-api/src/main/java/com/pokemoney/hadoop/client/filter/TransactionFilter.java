package com.pokemoney.hadoop.client.filter;

/**
 * The graphql filter for transaction
 */
public class TransactionFilter {
    /**
     * The min money.
     */
    private Float minMoney;

    /**
     * The max money.
     */
    private Float maxMoney;

    /**
     * The type id array.
     */
    private Integer[] typeIds;

    /**
     * Relevant entities.
     */
    private String[] relevantEntities;

    /**
     * The fund id array.
     */
    private Long[] fundIds;

    /**
     * The category ids.
     */
    private Integer[] categoryIds;

    /**
     * The subcategory ids.
     */
    private Long[] subcategoryIds;

    /**
     * The ledger book ids.
     */
    private Long ledgerIds;

    /**
     * The min updated at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long minUpdatedAt;

    /**
     * The max updated at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long maxUpdatedAt;
}
