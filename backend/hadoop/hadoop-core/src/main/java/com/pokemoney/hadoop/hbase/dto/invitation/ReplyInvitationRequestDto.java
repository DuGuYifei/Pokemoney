package com.pokemoney.hadoop.hbase.dto.invitation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The DTOs for replying invitation.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyInvitationRequestDto {
    /**
     * The user id.
     */
    private Long userId;
    /**
     * The invitation id.
     */
    private Long invitationId;
    /**
     * The user id.
     */
    private Boolean isAccept;
}
