package com.pokemoney.hadoop.hbase.dto.invitation;

import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import lombok.*;

/**
 * The DTOs for the invitation of ledger.
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LedgerInvitationDto {
    /**
     * The ID of the invitation.
     */
    private Long id;
    /**
     * The editor who invited.
     */
    private EditorDto invitedBy;
    /**
     * The ID of the ledger.
     */
    private Long ledgerId;

    /**
     * equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LedgerInvitationDto that)) return false;
        return id.equals(that.id);
    }

    /**
     * hash code
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
