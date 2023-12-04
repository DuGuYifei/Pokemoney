package com.pokemoney.hadoop.client.controller.restful;

import com.pokemoney.commons.http.dto.ResponseDto;
import com.pokemoney.hadoop.hbase.dto.editor.AddEditorInputDto;
import com.pokemoney.hadoop.hbase.dto.editor.RemoveEditorInputDto;
import com.pokemoney.hadoop.hbase.dto.invitation.AcceptFundInvitationResponseDto;
import com.pokemoney.hadoop.hbase.dto.invitation.AcceptLedgerInvitationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Invite editor controller
 */
@RestController
@RequestMapping("/api/v1/hadoop/client/invitation")
public class InvitationController {
    // TODO: 必须后端已经有了这个fund或者ledger，所以要返回请先同步到云端。
    /**
     * invite fund editor
     *
     * @param editor {@link AddEditorInputDto}
     * @return ResponseDto<?> without 'data' field
     */
    @PostMapping("/fund/invite")
    public ResponseEntity<ResponseDto<?>> inviteFundEditor(@RequestBody AddEditorInputDto editor) {
        return ResponseEntity.ok(ResponseDto.builder().build());
    }

    /**
     * remove fund editor
     *
     * @param editor {@link RemoveEditorInputDto}
     * @return ResponseDto<?> without 'data' field
     */
    @PostMapping("/fund/remove")
    public ResponseEntity<ResponseDto<?>> removeFundEditor(@RequestBody RemoveEditorInputDto editor) {
        return null;
    }

    /**
     * invite ledger editor
     *
     * @param editor {@link AddEditorInputDto}
     * @return ResponseDto<?> without 'data' field
     */
    @PostMapping("/ledger/invite")
    public ResponseEntity<ResponseDto<?>> inviteLedgerEditor(@RequestBody AddEditorInputDto editor) {
        return null;
    }

    /**
     * remove ledger editor
     *
     * @param editor {@link RemoveEditorInputDto}
     * @return ResponseDto<?> without 'data' field
     */
    @PostMapping("/ledger/remove")
    public ResponseEntity<ResponseDto<?>> removeLedgerEditor(@RequestBody RemoveEditorInputDto editor) {
        return null;
    }

    /**
     * reply fund invitation
     *
     * @param invitationId invitation id
     * @return ResponseDto with data {@link AcceptFundInvitationResponseDto}
     */
    @PostMapping("/fund/reply")
    public ResponseEntity<ResponseDto<AcceptFundInvitationResponseDto>> replyFundInvitation(Long invitationId, Boolean isAccept) {
        return null;
    }

    /**
     * reply ledger invitation
     *
     * @param invitationId invitation id
     * @return ResponseDto with data {@link AcceptLedgerInvitationResponseDto}
     */
    @PostMapping("/ledger/reply")
    public ResponseEntity<ResponseDto<AcceptLedgerInvitationResponseDto>> replyLedgerInvitation(Long invitationId, Boolean isAccept) {
        return null;
    }
}
