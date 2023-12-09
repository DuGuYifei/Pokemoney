package com.pokemoney.hadoop.client.vo;

import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncTransactionInputDto;
import com.pokemoney.hadoop.hbase.dto.transaction.TransactionDto;
import com.pokemoney.hadoop.hbase.dto.transaction.UpsertTransactionDto;
import com.pokemoney.hadoop.hbase.phoenix.model.FundModel;
import com.pokemoney.hadoop.hbase.phoenix.model.LedgerModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Preprocessed sync transactions.
 */
@Getter
@Setter
public class PreprocessedSyncTransactions {
    /**
     * Current operation id
     */
    Long curOperationId;
    /**
     * Update transaction dto list.
     */
    private List<UpsertTransactionDto> updateTransactionDtoList = new ArrayList<>();
    /**
     * Insert transaction dto list.
     */
    private List<UpsertTransactionDto> insertTransactionDtoList = new ArrayList<>();
    /**
     * Return transaction dto list.
     */
    private List<TransactionDto> returnTransactionDtoList = new ArrayList<>();
    /**
     * Update transaction operation dto list.
     */
    private List<OperationDto> updateTransactionOperationDtoList = new ArrayList<>();
    /**
     * Insert transaction operation dto list.
     */
    private List<OperationDto> insertTransactionOperationDtoList = new ArrayList<>();
    /**
     * No permission update transaction input dto list.
     */
    private List<SyncTransactionInputDto> noPermissionUpdateTransactionInputDtoList = new ArrayList<>();
    /**
     * User's ledger id to model map.
     */
    Map<Long, LedgerModel> ledgerIdToModelMap = new HashMap<>();
    /**
     * User's fund id to model map.
     */
    Map<Long, FundModel> fundIdToModelMap = new HashMap<>();

    /**
     * Constructor
     *
     * @param syncOperationId operation id
     */
    public PreprocessedSyncTransactions(long syncOperationId) {
        this.curOperationId = syncOperationId;
    }
}
