package com.pokemoney.hadoop.hbase.dto.user;

import com.pokemoney.hadoop.hbase.dto.invitation.FundInvitationDto;
import com.pokemoney.hadoop.hbase.dto.invitation.LedgerInvitationDto;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import com.pokemoney.hadoop.hbase.utils.JsonUtils;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Notification DTO
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class NotificationDto {
    /**
     * The fund invitation list.
     */
    private List<FundInvitationDto> fundInvitation;
    /**
     * The ledger invitation list.
     */
    private List<LedgerInvitationDto> ledgerInvitation;

    /**
     * Get notification DTO from notification model
     *
     * @param notificationModel notification model
     * @return notification DTO
     */
    public static NotificationDto getNotificationsFromNotificationModel(UserModel.NotificationModel notificationModel) {
        return JsonUtils.GSON.fromJson(notificationModel.getNotificationsJson(), NotificationDto.class);
    }

    /**
     * Generate json string of notification DTO
     *
     * @return json string of notification DTO
     */
    public String generateJsonString() {
        return JsonUtils.GSON.toJson(this);
    }

    /**
     * Add new fund invitation
     *
     * @param fundInvitationDto fund invitation DTO
     */
    public void addNewFundInvitation(FundInvitationDto fundInvitationDto) {
        if (fundInvitation == null) {
            fundInvitation = new ArrayList<>();
        }
        fundInvitation.add(fundInvitationDto);
    }

    /**
     * Add new ledger invitation
     *
     * @param ledgerInvitationDto ledger invitation DTO
     */
    public void addNewLedgerInvitation(LedgerInvitationDto ledgerInvitationDto) {
        if (ledgerInvitation == null) {
            ledgerInvitation = new ArrayList<>();
        }
        ledgerInvitation.add(ledgerInvitationDto);
    }

    /**
     * Remove fund invitation
     *
     * @param fundInvitationDto fund invitation DTO
     */
    public void removeFundInvitation(FundInvitationDto fundInvitationDto) {
        if (fundInvitation != null) {
            fundInvitation.remove(fundInvitationDto);
        }
    }

    /**
     * Remove ledger invitation
     *
     * @param ledgerInvitationDto ledger invitation DTO
     */
    public void removeLedgerInvitation(LedgerInvitationDto ledgerInvitationDto) {
        if (ledgerInvitation != null) {
            ledgerInvitation.remove(ledgerInvitationDto);
        }
    }
}
