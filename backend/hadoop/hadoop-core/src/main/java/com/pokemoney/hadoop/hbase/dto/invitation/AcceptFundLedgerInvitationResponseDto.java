package com.pokemoney.hadoop.hbase.dto.invitation;

import com.pokemoney.hadoop.hbase.dto.fund.FundDto;
import com.pokemoney.hadoop.hbase.dto.ledger.LedgerDto;
import lombok.*;

/**
 * The DTOs for the response of invitation of fund.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AcceptFundLedgerInvitationResponseDto {
    /**
     * The fund DTO.
     */
    private FundDto fund;
    private LedgerDto ledger;
}
