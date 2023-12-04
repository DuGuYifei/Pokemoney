package com.pokemoney.hadoop.client.vo;

import com.pokemoney.hadoop.hbase.dto.transaction.UpsertTransactionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private List<UpsertTransactionDto> returnTransactionDtoList = new ArrayList<>();
    /**
     * Update transaction operation dto list.
     */
    private List<UpsertTransactionDto> updateTransactionOperationDtoList = new ArrayList<>();
    /**
     * Insert transaction operation dto list.
     */
    private List<UpsertTransactionDto> insertTransactionOperationDtoList = new ArrayList<>();
    /**
     * No permission update transaction input dto list.
     */
    private List<UpsertTransactionDto> noPermissionUpdateTransactionInputDtoList = new ArrayList<>();

    /**
     * Constructor
     *
     * @param syncOperationId operation id
     */
    public PreprocessedSyncTransactions(long syncOperationId) {
        this.curOperationId = syncOperationId;
    }
}
