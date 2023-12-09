package com.pokemoney.hadoop.client.controller.restful;

import com.pokemoney.commons.http.dto.ResponseDto;
import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.commons.http.errors.GenericInternalServerError;
import com.pokemoney.commons.http.errors.GenericNotFoundError;
import com.pokemoney.hadoop.client.exception.GenericGraphQlForbiddenException;
import com.pokemoney.hadoop.client.service.AuthService;
import com.pokemoney.hadoop.client.service.FundService;
import com.pokemoney.hadoop.client.service.LedgerService;
import com.pokemoney.hadoop.client.service.UserService;
import com.pokemoney.hadoop.hbase.Constants;
import com.pokemoney.hadoop.hbase.dto.editor.AddEditorInputDto;
import com.pokemoney.hadoop.hbase.dto.editor.RemoveEditorInputDto;
import com.pokemoney.hadoop.hbase.dto.fund.UpsertFundDto;
import com.pokemoney.hadoop.hbase.dto.invitation.*;
import com.pokemoney.hadoop.hbase.dto.ledger.UpsertLedgerDto;
import com.pokemoney.hadoop.hbase.dto.user.NotificationDto;
import com.pokemoney.hadoop.hbase.dto.user.UpsertUserDto;
import com.pokemoney.hadoop.hbase.dto.user.UserFundInfoDto;
import com.pokemoney.hadoop.hbase.dto.user.UserLedgerBookInfoDto;
import com.pokemoney.hadoop.hbase.phoenix.model.FundModel;
import com.pokemoney.hadoop.hbase.phoenix.model.LedgerModel;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import com.pokemoney.user.service.api.*;
import com.pokemoney.user.service.api.exceptions.UserTriRpcException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.StatusRpcException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Invite editor controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/hadoop/client/invitation")
public class InvitationController {
    /**
     * Auth service
     */
    private final AuthService authService;
    /**
     * User trip service
     */
    @DubboReference(version = "1.0.0", group = "user", protocol = "tri", timeout = 10000)
    private final UserTriService userTriService;
    /**
     * user service
     */
    private final UserService userService;
    /**
     * ledger service
     */
    private final LedgerService ledgerService;
    /**
     * fund service
     */
    private final FundService fundService;

    /**
     * Constructor
     *
     * @param authService    auth service
     * @param userTriService user trip service
     * @param userService    user service
     * @param ledgerService  ledger service
     * @param fundService    fund service
     */
    public InvitationController(AuthService authService, UserTriService userTriService, UserService userService, LedgerService ledgerService, FundService fundService) {
        this.authService = authService;
        this.userTriService = userTriService;
        this.userService = userService;
        this.ledgerService = ledgerService;
        this.fundService = fundService;
    }

    private VerifyUserJwtWithServiceNameResponseDto preHandle (Long userId, HttpServletRequest request) throws GenericForbiddenError {
        String auth = request.getHeader("Authorization");
        try {
            return authService.verifyUser(userId, auth);
        } catch (GenericGraphQlForbiddenException e) {
            throw new GenericForbiddenError(e.getMessage());
        }
    }

    /**
     * invite fund editor
     *
     * @param editor {@link AddEditorInputDto}
     * @param request {@link HttpServletRequest}
     * @return ResponseDto<?> without 'data' field
     * @throws GenericForbiddenError generic forbidden error
     * @throws GenericNotFoundError generic not found error
     * @throws GenericInternalServerError generic internal server error
     */
    @PostMapping("/fund/invite")
    public ResponseEntity<ResponseDto<?>> inviteFundEditor(@RequestBody AddEditorInputDto editor, HttpServletRequest request) throws GenericForbiddenError, GenericNotFoundError, GenericInternalServerError {
        Long userId = editor.getInvitorId();
        VerifyUserJwtWithServiceNameResponseDto verifiedUserInfo = preHandle(userId, request);
        FundModel fundModel;
        // check if the user is the owner of this fund
        try {
            fundModel = fundService.getFundModel(editor.getTargetId(), userId, null);
            if (fundModel == null) {
                log.error("Fund not found, fund id: {}", editor.getTargetId());
                throw new GenericNotFoundError("Fund not found. Please sync this fund firstly.");
            }
            if (!fundModel.getFundInfo().getOwner().equals(userId)) {
                log.error("You are not the owner of this fund, fund id: {}", editor.getTargetId());
                throw new GenericForbiddenError("You are not the owner of this fund");
            }
        } catch (SQLException e) {
            log.error("Failed to get fund, fund id: {}", editor.getTargetId(), e);
            throw new GenericInternalServerError("Failed to find the fund");
        }
        // check if the user is already an editor of this fund
        GetUserInfoResponseDto editorInfo;
        try {
            editorInfo = userTriService.getUserInfoByEmail(GetUserInfoByEmailRequestDto.newBuilder().setEmail(editor.getInvitedEmail()).build());
            List<Long> editors = fundModel.getFundInfo().getEditors();
            for (Long editorId : editors) {
                if (editorId.equals(editorInfo.getUserId())) {
                    log.warn("This user is already an editor of this fund, fund id: {}", editor.getTargetId());
//                    throw new GenericForbiddenError("This user is already an editor of this fund");
                    return ResponseEntity.ok(ResponseDto.builder().status(2).message("This user is already an editor of this fund").build());
                }
            }
            // notify the editor
            userService.notifyNewFundEditor(userId, editorInfo.getUserId(), editorInfo.getUsername(), editor);
        } catch(RpcException e) {
            if (e.getCause() instanceof ExecutionException executionException) {
                if (executionException.getCause() instanceof StatusRpcException statusRpcException) {
                    if (UserTriRpcException.USER_NOT_FOUND.equals(statusRpcException.getStatus())) {
                        log.error("User with this email not found, email: {}", editor.getInvitedEmail());
                        throw new GenericNotFoundError("User with this email not found");
                    }
                }
            }
            log.error("Failed to get user info by email, email: {}", editor.getInvitedEmail(), e);
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(ResponseDto.builder().status(1).message("Success").build());
    }

    /**
     * remove fund editor
     *
     * @param editor {@link RemoveEditorInputDto}
     * @return ResponseDto<?> without 'data' field
     */
    @PostMapping("/fund/remove")
    public ResponseEntity<ResponseDto<?>> removeFundEditor(@RequestBody RemoveEditorInputDto editor, HttpServletRequest request) throws GenericForbiddenError, GenericInternalServerError, GenericNotFoundError {
        Long userId = editor.getInvitorId();
        VerifyUserJwtWithServiceNameResponseDto verifiedUserInfo = preHandle(userId, request);
        Long editorId = editor.getRemovedId();
        // remove editor in this fund's editors
        FundModel fundModel;
        try {
            fundModel = fundService.getFundModel(editor.getTargetId(), userId, null);
            if (fundModel == null) {
                log.error("Fund not found, fund id: {}", editor.getTargetId());
                throw new GenericNotFoundError("Fund not found");
            }
            if (!fundModel.getFundInfo().getOwner().equals(userId)) {
                log.error("You are not the owner of this fund, fund id: {}", editor.getTargetId());
                throw new GenericForbiddenError("You are not the owner of this fund");
            }
        } catch (SQLException | GenericNotFoundError e) {
            log.error("Failed to get fund, fund id: {}", editor.getTargetId(), e);
            throw new GenericInternalServerError("Failed to find the fund");
        }

        {
            List<Long> editors = fundModel.getFundInfo().getEditors();
            for (java.util.Iterator<Long> iterator = editors.iterator(); iterator.hasNext();) {
                Long editorIdInList = iterator.next();
                if (editorIdInList.equals(editorId)) {
                    iterator.remove();
                    fundModel.getFundInfo().setEditors(editors);
                    break;
                }
            }
        }

        // remove fund row key in editor's fund info
        UserModel editorModel;
        editorModel = userService.getUserByUserId(editorId);
        if (editorModel == null) {
            log.error("editor not found, userId: {}", editorId);
            throw new GenericNotFoundError("editor not found");
        }
        List<String> funds = editorModel.getFundInfo().getFunds();
        for (java.util.Iterator<String> iterator = funds.iterator(); iterator.hasNext();) {
            String fundRowKey = iterator.next();
            Long fundsLong = Long.parseLong(fundRowKey.split(Constants.ROW_KEY_DELIMITER)[2]);
            if (fundsLong.equals(editor.getTargetId())) {
                iterator.remove();
                UpsertUserDto upsertUserDto = new UpsertUserDto(
                        RowKeyUtils.getRegionId(editorId),
                        editorId,
                        null,
                        null,
                        null,
                        new UserFundInfoDto(funds, null),
                        null,
                        null,
                        null
                );
                userService.updateUser(upsertUserDto);
                break;
            }
        }
        return ResponseEntity.ok(ResponseDto.builder().status(1).message("success").build());
    }

    /**
     * invite ledger editor
     *
     * @param editor {@link AddEditorInputDto}
     * @param request {@link HttpServletRequest}
     * @return ResponseDto<?> without 'data' field
     * @throws GenericForbiddenError generic forbidden error
     * @throws GenericNotFoundError generic not found error
     * @throws GenericInternalServerError generic internal server error
     */
    @PostMapping("/ledger/invite")
    public ResponseEntity<ResponseDto<?>> inviteLedgerEditor(@RequestBody AddEditorInputDto editor, HttpServletRequest request) throws GenericForbiddenError, GenericNotFoundError, GenericInternalServerError {
        Long userId = editor.getInvitorId();
        VerifyUserJwtWithServiceNameResponseDto verifiedUserInfo = preHandle(userId, request);
        LedgerModel ledgerModel;
        // check if the user is the owner of this ledger
        try {
            ledgerModel = ledgerService.getLedgerModel(editor.getTargetId(), userId, null);
            if (ledgerModel == null) {
                log.error("Ledger not found, ledger id: {}", editor.getTargetId());
                throw new GenericNotFoundError("Ledger not found. Please sync this ledger firstly.");
            }
            if (!ledgerModel.getLedgerInfo().getOwner().equals(userId)) {
                log.error("You are not the owner of this ledger, ledger id: {}", editor.getTargetId());
                throw new GenericForbiddenError("You are not the owner of this ledger");
            }
        } catch (SQLException e) {
            log.error("Failed to get ledger, ledger id: {}", editor.getTargetId(), e);
            throw new GenericInternalServerError("Failed to find the ledger");
        }
        // check if the user is already an editor of this ledger
        GetUserInfoResponseDto editorInfo;
        try {
            editorInfo = userTriService.getUserInfoByEmail(GetUserInfoByEmailRequestDto.newBuilder().setEmail(editor.getInvitedEmail()).build());
            List<Long> editors = ledgerModel.getLedgerInfo().getEditors();
            for (Long editorId : editors) {
                if (editorId.equals(editorInfo.getUserId())) {
                    log.warn("This user is already an editor of this ledger, ledger id: {}", editor.getTargetId());
//                    throw new GenericForbiddenError("This user is already an editor of this ledger");
                    return ResponseEntity.ok(ResponseDto.builder().status(2).message("This user is already an editor of this ledger").build());
                }
            }
            // notify the editor
            userService.notifyNewLedgerEditor(userId, editorInfo.getUserId(), editorInfo.getUsername(), editor);
        } catch(RpcException e) {
            if (e.getCause() instanceof ExecutionException executionException) {
                if (executionException.getCause() instanceof StatusRpcException statusRpcException) {
                    if (UserTriRpcException.USER_NOT_FOUND.equals(statusRpcException.getStatus())) {
                        log.error("User with this email not found, email: {}", editor.getInvitedEmail());
                        throw new GenericNotFoundError("User with this email not found");
                    }
                }
            }
            log.error("Failed to get user info by email, email: {}", editor.getInvitedEmail(), e);
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(ResponseDto.builder().status(1).message("success").build());
    }

    /**
     * remove ledger editor
     *
     * @param editor {@link RemoveEditorInputDto}
     * @param request {@link HttpServletRequest}
     * @return ResponseDto<?> without 'data' field
     * @throws GenericForbiddenError generic forbidden error
     * @throws GenericNotFoundError generic not found error
     * @throws GenericInternalServerError generic internal server error
     */
    @PostMapping("/ledger/remove")
    public ResponseEntity<ResponseDto<?>> removeLedgerEditor(@RequestBody RemoveEditorInputDto editor, HttpServletRequest request) throws GenericForbiddenError, GenericInternalServerError, GenericNotFoundError {
        Long userId = editor.getInvitorId();
        VerifyUserJwtWithServiceNameResponseDto verifiedUserInfo = preHandle(userId, request);
        Long editorId = editor.getRemovedId();
        // remove editor in this ledger's editors
        LedgerModel ledgerModel;
        try {
            ledgerModel = ledgerService.getLedgerModel(editor.getTargetId(), userId, null);
            if (ledgerModel == null) {
                log.error("Ledger not found, ledger id: {}", editor.getTargetId());
                throw new GenericNotFoundError("Ledger not found");
            }
            if (!ledgerModel.getLedgerInfo().getOwner().equals(userId)) {
                log.error("You are not the owner of this ledger, ledger id: {}", editor.getTargetId());
                throw new GenericForbiddenError("You are not the owner of this ledger");
            }
        } catch (SQLException | GenericNotFoundError e) {
            log.error("Failed to get ledger, ledger id: {}", editor.getTargetId(), e);
            throw new GenericInternalServerError("Failed to find the ledger");
        }

        {
            List<Long> editors = ledgerModel.getLedgerInfo().getEditors();
            for (java.util.Iterator<Long> iterator = editors.iterator(); iterator.hasNext();) {
                Long editorIdInList = iterator.next();
                if (editorIdInList.equals(editorId)) {
                    iterator.remove();
                    ledgerModel.getLedgerInfo().setEditors(editors);
                    break;
                }
            }
        }

        // remove ledger row key in editor's ledger info
        UserModel editorModel;
        editorModel = userService.getUserByUserId(editorId);
        if (editorModel == null) {
            log.error("editor not found, userId: {}", editorId);
            throw new GenericNotFoundError("editor not found");
        }
        List<String> ledgers = editorModel.getLedgerInfo().getLedgers();
        for (java.util.Iterator<String> iterator = ledgers.iterator(); iterator.hasNext();) {
            String ledgerRowKey = iterator.next();
            Long ledgersLong = Long.parseLong(ledgerRowKey.split(Constants.ROW_KEY_DELIMITER)[2]);
            if (ledgersLong.equals(editor.getTargetId())) {
                iterator.remove();
                UpsertUserDto upsertUserDto = new UpsertUserDto(
                        RowKeyUtils.getRegionId(editorId),
                        editorId,
                        null,
                        null,
                        null,
                        null,
                        new UserLedgerBookInfoDto(ledgers, null),
                        null,
                        null
                );
                userService.updateUser(upsertUserDto);
                break;
            }
        }
        return ResponseEntity.ok(ResponseDto.builder().status(1).message("success").build());
    }

    /**
     * reply fund or ledger invitation
     *
     * @param replyInvitationRequestDto {@link ReplyInvitationRequestDto}
     * @return ResponseDto with data {@link AcceptFundInvitationResponseDto}
     * @throws GenericForbiddenError generic forbidden error
     * @throws GenericInternalServerError generic internal server error
     * @throws GenericNotFoundError generic not found error
     */
    @PostMapping("/reply")
    public ResponseEntity<ResponseDto<AcceptFundInvitationResponseDto>> replyFundInvitation(@RequestBody ReplyInvitationRequestDto replyInvitationRequestDto, HttpServletRequest request) throws GenericForbiddenError, GenericInternalServerError, GenericNotFoundError {
        Long userId = replyInvitationRequestDto.getUserId();
        VerifyUserJwtWithServiceNameResponseDto verifiedUserInfo = preHandle(userId, request);
        // remove the invitation
        UserModel userModel = userService.getUserByUserId(userId);
        if (userModel == null) {
            log.error("User not found, user id: {}", userId);
            throw new GenericNotFoundError("User not found");
        }
        NotificationDto notificationDto = NotificationDto.getNotificationsFromNotificationModel(userModel.getNotifications());
        List<FundInvitationDto> fundInvitationDtoList = notificationDto.getFundInvitation();
        List<LedgerInvitationDto> ledgerInvitationDtoList = notificationDto.getLedgerInvitation();
        if ((fundInvitationDtoList == null || fundInvitationDtoList.isEmpty()) && (ledgerInvitationDtoList == null || ledgerInvitationDtoList.isEmpty())) {
            log.error("Invitation not found, invitation id: {}", replyInvitationRequestDto.getInvitationId());
            throw new GenericNotFoundError("Fund invitation not found");
        }
        FundInvitationDto fundInvitationDto = null;
        if (fundInvitationDtoList != null) {
            for (FundInvitationDto fd : fundInvitationDtoList) {
                if (fd.getId().equals(replyInvitationRequestDto.getInvitationId())) {
                    fundInvitationDto = fd;
                    // delete this invitation
                    fundInvitationDtoList.remove(FundInvitationDto.builder().id(replyInvitationRequestDto.getInvitationId()).build());
                    break;
                }
            }
        }
        if (fundInvitationDto != null){
            UserFundInfoDto userFundInfoDto = UserFundInfoDto.fromFundModel(userModel.getFundInfo());
            // if accept
            if (replyInvitationRequestDto.getIsAccept()) {
                Long inviteBy = fundInvitationDto.getInvitedBy().getUserId();
                // add the id of fund into user's fund info
                String fundRowKey = RowKeyUtils.getFundRowKey(RowKeyUtils.getRegionId(inviteBy).toString(), inviteBy.toString(), fundInvitationDto.getFundId().toString());
                if (userFundInfoDto.getFunds().contains(fundRowKey)) {
                    log.warn("You are already an editor of this fund, fund id: {}, user id: {}", fundInvitationDto.getFundId(), userId);
                    return ResponseEntity.ok(ResponseDto.<AcceptFundInvitationResponseDto>builder().status(2).message("You are already an editor of this fund").build());
                }
                userFundInfoDto.getFunds().add(fundRowKey);
                userFundInfoDto.setDelFunds(null);
//                userService.updateUser(new UpsertUserDto(
//                        userModel.getRegionId(),
//                        userModel.getUserId(),
//                        null,
//                        null,
//                        null,
//                        userFundInfoDto,
//                        null,
//                        null,
//                        null
//                ));
                // update the id in fund's editors
                try {
                    FundModel fundModel = fundService.getFundModel(fundInvitationDto.getFundId(), inviteBy, null);
                    if (fundModel == null) {
                        log.error("Fund not found, fund id: {}, user id: {}", fundInvitationDto.getFundId(), userId);
                        throw new GenericNotFoundError("Fund not found");
                    }
                    List<Long> editors = fundModel.getFundInfo().getEditors();
                    if (editors.contains(userId)) {
                        log.warn("You are already an editor of this fund, fund id: {}, user id: {}", fundInvitationDto.getFundId(), userId);
                        return ResponseEntity.ok(ResponseDto.<AcceptFundInvitationResponseDto>builder().status(2).message("You are already an editor of this fund").build());
                    }
                    editors.add(userId);
                    UpsertFundDto upsertFundDto = new UpsertFundDto(
                            fundModel.getRegionId(),
                            fundModel.getUserId(),
                            fundModel.getFundId(),
                            null,
                            null,
                            editors,
                            null,
                            null,
                            System.currentTimeMillis(),
                            null
                    );
                    fundService.updateFundsByRowKey(new ArrayList<>(List.of(upsertFundDto)));
                } catch (SQLException e) {
                    log.error("Failed to get fund, fund id: {}, user id: {}", fundInvitationDto.getFundId(), userId, e);
                    throw new GenericInternalServerError("Failed to find the fund");
                }
            } else {
                userFundInfoDto.setFunds(null);
                userFundInfoDto.setDelFunds(null);
            }
            // update notification
            NotificationDto newNotificationDto = NotificationDto.builder()
                    .fundInvitation(fundInvitationDtoList)
                    .ledgerInvitation(ledgerInvitationDtoList)
                    .build();
            UpsertUserDto userDto = new UpsertUserDto(
                    userModel.getRegionId(),
                    userModel.getUserId(),
                    null,
                    null,
                    null,
                    userFundInfoDto,
                    null,
                    null,
                    newNotificationDto.generateJsonString()
            );
            userService.updateUser(userDto);
        } else {
            LedgerInvitationDto ledgerInvitationDto = null;
            if (ledgerInvitationDtoList != null) {
                for (LedgerInvitationDto ld : ledgerInvitationDtoList) {
                    if (ld.getId().equals(replyInvitationRequestDto.getInvitationId())) {
                        ledgerInvitationDto = ld;
                        // delete this invitation
                        ledgerInvitationDtoList.remove(LedgerInvitationDto.builder().id(replyInvitationRequestDto.getInvitationId()).build());
                        break;
                    }
                }
            }
            if (ledgerInvitationDto != null) {
                UserLedgerBookInfoDto userLedgerBookInfoDto = UserLedgerBookInfoDto.fromLedgerBookModel(userModel.getLedgerInfo());
                // if accept
                if (replyInvitationRequestDto.getIsAccept()) {
                    Long inviteBy = ledgerInvitationDto.getInvitedBy().getUserId();
                    // add the id of ledger into user's ledger info
                    String ledgerRowKey = RowKeyUtils.getLedgerRowKey(RowKeyUtils.getRegionId(inviteBy).toString(), inviteBy.toString(), ledgerInvitationDto.getLedgerId().toString());
                    if (userLedgerBookInfoDto.getLedgers().contains(ledgerRowKey)) {
                        log.warn("You are already an editor of this ledger, ledger id: {}, user id: {}", ledgerInvitationDto.getLedgerId(), userId);
                        return ResponseEntity.ok(ResponseDto.<AcceptFundInvitationResponseDto>builder().status(2).message("You are already an editor of this ledger").build());
                    }
                    userLedgerBookInfoDto.getLedgers().add(ledgerRowKey);
                    userLedgerBookInfoDto.setDelLedgers(null);
//                    userService.updateUser(new UpsertUserDto(
//                            userModel.getRegionId(),
//                            userModel.getUserId(),
//                            null,
//                            null,
//                            null,
//                            null,
//                            userLedgerBookInfoDto,
//                            null,
//                            null
//                    ));
                    // update the id in ledger's editors
                    try {
                        LedgerModel ledgerModel = ledgerService.getLedgerModel(ledgerInvitationDto.getLedgerId(), inviteBy, null);
                        if (ledgerModel == null) {
                            log.error("Ledger not found, ledger id: {}, user id: {}", ledgerInvitationDto.getLedgerId(), userId);
                            throw new GenericNotFoundError("Ledger not found");
                        }
                        List<Long> editors = ledgerModel.getLedgerInfo().getEditors();
                        if (editors.contains(userId)) {
                            log.warn("You are already an editor of this ledger, ledger id: {}, user id: {}", ledgerInvitationDto.getLedgerId(), userId);
                            return ResponseEntity.ok(ResponseDto.<AcceptFundInvitationResponseDto>builder().status(2).message("You are already an editor of this ledger").build());
                        }
                        editors.add(userId);
                        UpsertLedgerDto upsertLedgerDto = new UpsertLedgerDto(
                                ledgerModel.getRegionId(),
                                ledgerModel.getUserId(),
                                ledgerModel.getLedgerId(),
                                null,
                                null,
                                editors,
                                null,
                                null,
                                System.currentTimeMillis(),
                                null
                        );
                        ledgerService.updateLedgersByRowKey(new ArrayList<>(List.of(upsertLedgerDto)));
                    } catch (SQLException e) {
                        log.error("Failed to get ledger, ledger id: {}", ledgerInvitationDto.getLedgerId(), e);
                        throw new GenericInternalServerError("Failed to find the ledger");
                    }
                }
                // update notification
                NotificationDto newNotificationDto = NotificationDto.builder()
                        .fundInvitation(fundInvitationDtoList)
                        .ledgerInvitation(ledgerInvitationDtoList)
                        .build();
                UpsertUserDto userDto = new UpsertUserDto(
                        userModel.getRegionId(),
                        userModel.getUserId(),
                        null,
                        null,
                        null,
                        null,
                        userLedgerBookInfoDto,
                        null,
                        newNotificationDto.generateJsonString()
                );
                userService.updateUser(userDto);
            } else {
                log.error("Invitation not found, invitation id: {}", replyInvitationRequestDto.getInvitationId());
                throw new GenericNotFoundError("Invitation not found. Maybe it has been answered in your other device.");
            }
        }
        return ResponseEntity.ok(ResponseDto.<AcceptFundInvitationResponseDto>builder().status(1).message("success").build());
    }
}
