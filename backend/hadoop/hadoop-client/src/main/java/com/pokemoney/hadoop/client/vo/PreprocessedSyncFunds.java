package com.pokemoney.hadoop.client.vo;

import com.pokemoney.hadoop.hbase.dto.fund.FundDto;
import com.pokemoney.hadoop.hbase.dto.fund.UpsertFundDto;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncFundInputDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Preprocessed sync funds.
 */
@Getter
@Setter
public class PreprocessedSyncFunds {
    /**
     * Current operation id
     */
    private Long curOperationId;
    /**
     * Update fund dto list.
     */
    private  List<UpsertFundDto> updateFundDtoList = new ArrayList<>();
    /**
     * Insert fund dto list.
     */
    private List<UpsertFundDto> insertFundDtoList = new ArrayList<>();
    /**
     * Return fund dto list.
     */
    private List<FundDto> returnFundDtoList = new ArrayList<>();
    /**
     * Update fund operation dto list.
     */
    private  List<OperationDto> updateFundOperationDtoList = new ArrayList<>();
    /**
     * Insert fund operation dto list.
     */
    private List<OperationDto> insertFundOperationDtoList = new ArrayList<>();
    /**
     * No permission update fund input dto list.
     */
    private List<SyncFundInputDto> noPermissionUpdateFundInputDtoList = new ArrayList<>();

    /**
     * Constructor
     *
     * @param syncOperationId operation id
     */
    public PreprocessedSyncFunds(long syncOperationId) {
        this.curOperationId = syncOperationId;
    }
}
