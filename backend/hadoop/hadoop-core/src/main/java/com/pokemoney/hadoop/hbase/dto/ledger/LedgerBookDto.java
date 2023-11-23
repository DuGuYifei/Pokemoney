package com.pokemoney.hadoop.hbase.dto.ledger;

import lombok.*;

import java.util.Map;

/**
 * General ledger book DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LedgerBookDto {
    /**
     * The field name mapping to column name.
     */
    public static final Map<String, String> FIELD_NAME_MAPPING = Map.of(
            "ledgerId", "ledger_info.ledger_id",
            "name", "ledger_info.name",
            "budget", "ledger_info.budget",
            "owner", "ledger_info.owner",
            "editors", "ledger_info.editors",
            "createAt", "ledger_info.create_at",
            "updateAt", "update_info.update_at"
    );
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
     * The ledger created at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private String createAt;
    /**
     * The ledger updated at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private String updateAt;
}
