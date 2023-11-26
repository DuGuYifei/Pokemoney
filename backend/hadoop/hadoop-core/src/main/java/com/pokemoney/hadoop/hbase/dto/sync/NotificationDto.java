package com.pokemoney.hadoop.hbase.dto.sync;

import com.pokemoney.hadoop.hbase.dto.invitation.FundInvitationDto;
import com.pokemoney.hadoop.hbase.dto.invitation.LedgerInvitationDto;
import lombok.*;

import java.util.List;

/**
 * Notification DTO.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationDto {
    /**
     * Ledger invitation.
     */
    private List<LedgerInvitationDto> ledgerInvitation;
    /**
     * Fund invitation.
     */
    private List<FundInvitationDto> fundInvitation;
}