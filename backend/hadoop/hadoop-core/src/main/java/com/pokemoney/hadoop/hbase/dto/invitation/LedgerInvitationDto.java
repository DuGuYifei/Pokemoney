package com.pokemoney.hadoop.hbase.dto.invitation;

import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;

/**
 * The DTOs for the invitation of ledger.
 */
public class LedgerInvitationDto {
//    id: ID!
//    invitedBy: Editor!
//    ledgerId: ID!
    /**
     * The ID of the invitation.
     */
    private String id;
    /**
     * The editor who invited.
     */
    private EditorDto invitedBy;
    /**
     * The ID of the ledger.
     */
    private Long ledgerId;
}
