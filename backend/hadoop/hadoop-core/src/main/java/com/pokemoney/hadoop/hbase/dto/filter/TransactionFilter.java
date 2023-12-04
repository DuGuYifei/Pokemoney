package com.pokemoney.hadoop.hbase.dto.filter;

import lombok.*;

/**
 * The graphql filter for transaction
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
    private Long[] ledgerIds;
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
