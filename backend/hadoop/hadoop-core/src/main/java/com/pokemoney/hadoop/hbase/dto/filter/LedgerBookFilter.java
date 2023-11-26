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
    private Long owner;

    /**
     * The editors.
     */
    private Long[] editors;

    /**
     * The min created at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long minCreatedAt;

    /**
     * The max created at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long maxCreatedAt;

    /**
     * The min updated at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long minUpdatedAt;

    /**
     * The max updated at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long maxUpdatedAt;
    /**
     * The deleted flag.
     */
    private Integer delFlag;
}
