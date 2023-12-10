package com.pokemoney.hadoop.hbase.dto.filter;

import lombok.*;

/**
 * The graph filter for fund
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FundFilter {
    /**
     * The min balance.
     */
    private Float minBalance;

    /**
     * The max balance.
     */
    private Float maxBalance;

    /**
     * The owner.
     */
    private Long owner;

    /**
     * The editors ids.
     */
    private Long[] editors;

    /**
     * The min create at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long minCreateAt;

    /**
     * The max create at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
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
