package com.pokemoney.hadoop.hbase.dto.invitation;

import com.pokemoney.hadoop.hbase.dto.fund.FundDto;
import lombok.*;

/**
 * The DTOs for the response of invitation of fund.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AcceptFundInvitationResponseDto {
    /**
     * The fund DTO.
     */
    private FundDto fund;
}
