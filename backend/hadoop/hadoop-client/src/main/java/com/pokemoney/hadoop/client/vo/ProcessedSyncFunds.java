package com.pokemoney.hadoop.client.vo;

import com.pokemoney.hadoop.hbase.dto.fund.FundDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncFundInputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The result of processed sync funds.
 */
@Getter
@Setter
@AllArgsConstructor
public class ProcessedSyncFunds {
    /**
     * List of processed sync fund dto.
     */
    private List<FundDto> processedSyncFunds;
    /**
     * No permission changed fund dto list.
     */
    private List<SyncFundInputDto> noPermissionChangedFunds;
    /**
     * max operation id.
     */
    private Long maxOperationId;
}
