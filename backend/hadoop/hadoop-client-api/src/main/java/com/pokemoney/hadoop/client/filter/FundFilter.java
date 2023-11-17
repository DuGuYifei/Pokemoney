package com.pokemoney.hadoop.client.filter;

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
    private String owner;

    /**
     * The editors.
     */
    private String[] editors;

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
}
