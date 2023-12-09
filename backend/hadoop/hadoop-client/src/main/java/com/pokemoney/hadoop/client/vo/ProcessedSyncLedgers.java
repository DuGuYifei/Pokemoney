package com.pokemoney.hadoop.client.vo;

import com.pokemoney.hadoop.hbase.dto.ledger.LedgerDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncLedgerInputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The result of processed sync ledgers.
 */
@Getter
@Setter
@AllArgsConstructor
public class ProcessedSyncLedgers {
    /**
     * List of processed sync ledger dto.
     */
    private List<LedgerDto> processedSyncLedgers;
    /**
     * No permission changed ledger dto list.
     */
    private List<SyncLedgerInputDto> noPermissionChangedLedgers;
    /**
     * max operation id.
     */
    private Long maxOperationId;
}
