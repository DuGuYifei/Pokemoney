package com.pokemoney.hadoop.hbase.dto.filter;

import lombok.*;

/**
 * The graphql filter for ledger book
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LedgerBookFilter {
    /**
     * The min budget.
     */
    private Float minBudget;

    /**
     * The max budget.
     */
    private Float maxBudget;

    /**
     * The owner.
     */
    private String owner;

    /**
     * The editors.
     */
    private String[] editors;

    /**
     * The min created at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private String minCreatedAt;

    /**
     * The max created at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private String maxCreatedAt;

    /**
     * The min updated at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private String minUpdatedAt;

    /**
     * The max updated at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private String maxUpdatedAt;
}
