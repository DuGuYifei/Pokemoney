package com.pokemoney.hadoop.hbase.dto.invitation;

import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import lombok.*;

/**
 * The DTOs for the invitation of fund.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FundInvitationDto {
    /**
     * The ID of invitation.
     */
    private Long id;
    /**
     * The editor who invited.
     */
    private EditorDto invitedBy;
    /**
     * The ID of fund.
     */
    private Long fundId;
}
