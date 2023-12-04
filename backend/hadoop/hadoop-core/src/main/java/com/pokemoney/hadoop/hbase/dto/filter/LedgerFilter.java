package com.pokemoney.hadoop.hbase.dto.filter;

import lombok.*;

/**
 * The graphql filter for ledger book
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LedgerFilter {
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
    private Long minCreateAt;

    /**
     * The max created at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long maxCreateAt;

    /**
     * The min update at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long minUpdateAt;

    /**
     * The max update at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long maxUpdateAt;
    /**
     * The deleted flag.
     */
    private Integer delFlag;
}
