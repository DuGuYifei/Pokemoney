package com.pokemoney.hadoop.client.service;

import com.pokemoney.commons.http.errors.GenericInternalServerError;
import com.pokemoney.commons.http.errors.GenericNotFoundError;
import com.pokemoney.hadoop.client.Constants;
import com.pokemoney.hadoop.hbase.dto.editor.AddEditorInputDto;
import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import com.pokemoney.hadoop.hbase.dto.invitation.FundInvitationDto;
import com.pokemoney.hadoop.hbase.dto.invitation.LedgerInvitationDto;
import com.pokemoney.hadoop.hbase.dto.user.NotificationDto;
import com.pokemoney.hadoop.hbase.dto.user.UpsertUserDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.UserMapper;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import com.pokemoney.leaf.service.api.LeafGetRequestDto;
import com.pokemoney.leaf.service.api.LeafResponseDto;
import com.pokemoney.leaf.service.api.LeafTriService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * User table service
 */
@Slf4j
@Service
public class UserService {
    /**
     * Leaf triple service
     */
    @DubboReference(version = "1.0.0", group = "leaf", protocol = "tri", timeout = 10000)
    private final LeafTriService leafTriService;
    /**
     * user mapper
     */
    private final UserMapper userMapper;

    /**
     * Constructor
     *
     * @param leafTriService leaf triple service
     * @param userMapper     user mapper
     */
    public UserService(LeafTriService leafTriService, UserMapper userMapper) {
        this.leafTriService = leafTriService;
        this.userMapper = userMapper;
    }

    /**
     * Get user by user id
     *
     * @param userId user id
     * @return user model
     */
    public UserModel getUserByUserId(Long userId) {
        Integer regionId = RowKeyUtils.getRegionId(userId);
        return userMapper.getUserByUserId(regionId, userId, null);
    }

    /**
     * Insert user
     *
     * @param upsertUserDto upsert user dto
     * @return the number of rows affected
     */
    public int insertUser(UpsertUserDto upsertUserDto) {
        return userMapper.insertUser(upsertUserDto);
    }

    /**
     * Update user
     *
     * @param upsertUserDto upsert user dto
     * @return the number of rows affected
     */
    public int updateUser(UpsertUserDto upsertUserDto) {
        return userMapper.updateUser(upsertUserDto);
    }

    /**
     * Add new fund editor
     *
     * @param userId         user id
     * @param editorId       editor id
     * @param editorName     editor name
     * @param editorInputDto editor input dto
     * @throws GenericNotFoundError      user not found
     * @throws GenericInternalServerError invite fail
     */
    public void notifyNewFundEditor(Long userId, Long editorId, String editorName, AddEditorInputDto editorInputDto) throws GenericNotFoundError, GenericInternalServerError {
        UserModel editorModel = getUserByUserId(editorId);
        if (editorModel == null) {
            log.error("editor not found, userId: {}", editorId);
            throw new GenericNotFoundError("editor not found");
        }
        NotificationDto notificationDto = NotificationDto.getNotificationsFromNotificationModel(editorModel.getNotifications());
        LeafResponseDto leafResponseDto = leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey( Constants.LEAF_HBASE_INVITATION).build());
        Long invitationId = Long.parseLong(leafResponseDto.getId());
        EditorDto editorDto = new EditorDto(
                editorId,
                editorInputDto.getInvitedEmail(),
                editorName
        );
        notificationDto.addNewFundInvitation(new FundInvitationDto(invitationId, editorDto, editorInputDto.getTargetId()));
        String notificationJson = notificationDto.generateJsonString();
        UpsertUserDto upsertUserDto = new UpsertUserDto(
                editorModel.getRegionId(),
                editorModel.getUserId(),
                null,
                null,
                null,
                null,
                null,
                null,
                notificationJson
        );
        try{
            updateUser(upsertUserDto);
        } catch (Exception e) {
            log.error("update user failed when invite fund editor, userId: {}, editorId: {}, exception: {}", userId, editorId, e);
            throw new GenericInternalServerError("Invite fail");
        }
    }

    /**
     * Add new ledger editor
     *
     * @param userId         user id
     * @param editorId       editor id
     * @param editorName     editor name
     * @param editorInputDto editor input dto
     * @throws GenericNotFoundError      user not found
     * @throws GenericInternalServerError invite fail
     */
    public void notifyNewLedgerEditor(Long userId, Long editorId, String editorName, AddEditorInputDto editorInputDto) throws GenericNotFoundError, GenericInternalServerError {
        UserModel editorModel = getUserByUserId(editorId);
        if (editorModel == null) {
            log.error("editor not found, userId: {}", editorId);
            throw new GenericNotFoundError("editor not found");
        }
        NotificationDto notificationDto = NotificationDto.getNotificationsFromNotificationModel(editorModel.getNotifications());
        LeafResponseDto leafResponseDto = leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey( Constants.LEAF_HBASE_INVITATION).build());
        Long invitationId = Long.parseLong(leafResponseDto.getId());
        EditorDto editorDto = new EditorDto(
                editorId,
                editorInputDto.getInvitedEmail(),
                editorName
        );
        notificationDto.addNewLedgerInvitation(new LedgerInvitationDto(invitationId, editorDto, editorInputDto.getTargetId()));
        String notificationJson = notificationDto.generateJsonString();
        UpsertUserDto upsertUserDto = new UpsertUserDto(
                editorModel.getRegionId(),
                editorModel.getUserId(),
                null,
                null,
                null,
                null,
                null,
                null,
                notificationJson
        );
        try{
            updateUser(upsertUserDto);
        } catch (Exception e) {
            log.error("update user failed when invite ledger editor, userId: {}, editorId: {}, exception: {}", userId, editorId, e);
            throw new GenericInternalServerError("Invite fail");
        }
    }
}
