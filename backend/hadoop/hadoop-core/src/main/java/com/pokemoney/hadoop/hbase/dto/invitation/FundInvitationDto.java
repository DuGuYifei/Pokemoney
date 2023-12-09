package com.pokemoney.hadoop.hbase.dto.invitation;

import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import lombok.*;

/**
 * The DTOs for the invitation of fund.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
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

    /**
     * The ID of ledger.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FundInvitationDto that)) return false;
        return id.equals(that.id);
    }

    /**
     * The hash code of invitation.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
