package com.pokemoney.hadoop.dto.ledger;

import lombok.*;

/**
 * Mutate ledger book DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MutateLedgerBookDto {
    /**
     * The ledger id.
     */
    private Long ledgerId;

    /**
     * The ledger name.
     */
    private String name;

    /**
     * The ledger budget.
     */
    private Float budget;

    /**
     * The ledger owner.
     */
    private String owner;

    /**
     * The ledger editors.
     */
    private String[] editors;

    /**
     * The ledger updated at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private String updatedAt;
}
