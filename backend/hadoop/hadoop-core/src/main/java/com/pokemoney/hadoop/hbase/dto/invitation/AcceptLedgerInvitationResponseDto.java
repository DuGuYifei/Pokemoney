package com.pokemoney.hadoop.hbase.dto.invitation;

import com.pokemoney.hadoop.hbase.dto.ledger.LedgerDto;
import com.pokemoney.hadoop.hbase.dto.transaction.TransactionDto;

import java.util.List;

/**
 * The DTOs for the response of invitation of ledger.
 */
public class AcceptLedgerInvitationResponseDto {
    /**
     * The ledger.
     */
    private LedgerDto ledger;
    /**
     * The transactions.
     */
    private List<TransactionDto> transactions;
}
