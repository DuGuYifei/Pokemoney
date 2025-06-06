package com.pokemoney.hadoop.client.vo;

import com.pokemoney.hadoop.hbase.dto.ledger.LedgerDto;
import com.pokemoney.hadoop.hbase.dto.ledger.UpsertLedgerDto;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncLedgerInputDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * It is used to store the preprocessed sync ledgers.
 */
@Getter
@Setter
public class PreprocessedSyncLedgers {
    /**
     * Current operation id
     */
    private Long curOperationId;
    /**
     * Update ledger dto list.
     */
    private List<UpsertLedgerDto> updateLedgerDtoList = new ArrayList<>();
    /**
     * Insert ledger dto list.
     */
    private List<UpsertLedgerDto> insertLedgerDtoList = new ArrayList<>();
    /**
     * Return ledger dto list.
     */
    private List<LedgerDto> returnLedgerDtoList = new ArrayList<>();
    /**
     * Update ledger operation dto list.
     */
    private List<OperationDto> ledgerOperationDtoList = new ArrayList<>();
    /**
     * No permission update ledger input dto list.
     */
    private List<SyncLedgerInputDto> noPermissionUpdateLedgerInputDtoList = new ArrayList<>();
    /**
     * Map of new ledger id and new snowflake id.
     */
    private Map<Long, Long> newLedgerIdAndSnowflakeIdMap = new HashMap<>();
    /**
     * Constructor
     *
     * @param syncOperationId operation id
     */
    public PreprocessedSyncLedgers(long syncOperationId) {
        this.curOperationId = syncOperationId;
    }
}
