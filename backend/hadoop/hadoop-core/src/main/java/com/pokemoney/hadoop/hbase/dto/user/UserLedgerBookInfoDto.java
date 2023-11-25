package com.pokemoney.hadoop.hbase.dto.category;

import lombok.*;

import java.util.List;

/**
 * user ledger book info DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLedgerBookInfoDto {
    /**
     * ledger book IDs
     */
    private List<Long> ledgerIds;
    /**
     * deleted ledger book IDs
     */
    private List<Long> deletedLedgerIds;
}
