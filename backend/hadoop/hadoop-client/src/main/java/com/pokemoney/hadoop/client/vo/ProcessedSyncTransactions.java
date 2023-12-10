package com.pokemoney.hadoop.client.vo;

import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncTransactionInputDto;
import com.pokemoney.hadoop.hbase.dto.transaction.TransactionDto;
import com.pokemoney.hadoop.hbase.phoenix.model.FundModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * The result of processed sync transactions.
 */
@Getter
@Setter
@AllArgsConstructor
public class ProcessedSyncTransactions {
    /**
     * List of processed sync transaction dto.
     */
    private List<TransactionDto> processedSyncTransactions;
    /**
     * No permission changed transaction dto list.
     */
    private List<SyncTransactionInputDto> noPermissionChangedTransactions;
    /**
     * Fund model with new balance list.
     */
    private Map<Long, FundModel> fundModels;
    /**
     * max operation id.
     */
    private Long maxOperationId;
}
