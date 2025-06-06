package com.pokemoney.hadoop.hbase.dto.user;

import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import lombok.*;

import java.util.Arrays;
import java.util.List;

/**
 * user DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
//    /**
//     * Field maps to phoenix.
//     */
//    public static final Map<String, String> FIELD_NAME_MAPPING = Map.ofEntries(
//            Map.entry("userId", "user_id"),
//            Map.entry("email", "user_info.email"),
//            Map.entry("name", "user_info.name"),
//            Map.entry("fundInfo.funds", "fund_info.funds"),
//            Map.entry("fundInfo.delFunds", "fund_info.del_funds"),
//            Map.entry("ledgerInfo.ledgers", "ledger_info.ledgers"),
//            Map.entry("ledgerInfo.delLedgers", "ledger_info.del_ledgers"),
//            Map.entry("appInfo.categories", "app_info.categories"),
//            Map.entry("appInfo.delCategories", "app_info.del_categories"),
//            Map.entry("notification", "notification")
//    );
    /**
     * user ID.
     */
    private Long userId;
    /**
     * email
     */
    private String email;
    /**
     * name
     */
    private String name;
    /**
     * fund info
     */
    private UserFundInfoDto fundInfo;
    /**
     * Ledger book info
     */
    private UserLedgerBookInfoDto ledgerInfo;
    /**
     * App info
     */
    private UserAppInfoDto appInfo;
    /**
     * Notification
     */
    private NotificationDto notification;

    /**
     * Convert from user model to user DTO.
     *
     * @param userModel the user model
     * @return the user DTO
     */
    public static UserDto fromUserModel(UserModel userModel) {
        return UserDto.builder()
                .userId(userModel.getUserId())
                .email(userModel.getUserInfo().getEmail())
                .name(userModel.getUserInfo().getName())
                .fundInfo(UserFundInfoDto.fromFundModel(userModel.getFundInfo()))
                .ledgerInfo(UserLedgerBookInfoDto.fromLedgerBookModel(userModel.getLedgerInfo()))
                .appInfo(UserAppInfoDto.fromAppModel(userModel.getAppInfo()))
                .notification(NotificationDto.getNotificationsFromNotificationModel(userModel.getNotifications()))
                .build();
    }

    public UserDto checkNullList() {
        if (this.fundInfo == null) {
            this.fundInfo = UserFundInfoDto.builder().build();
        }
        if (this.fundInfo.getFunds() == null) {
            this.fundInfo.setFunds(List.of());
        }
        if (this.fundInfo.getDelFunds() == null) {
            this.fundInfo.setDelFunds(List.of());
        }
        if (this.ledgerInfo == null) {
            this.ledgerInfo = UserLedgerBookInfoDto.builder().build();
        }
        if (this.ledgerInfo.getLedgers() == null) {
            this.ledgerInfo.setLedgers(List.of());
        }
        if (this.ledgerInfo.getDelLedgers() == null) {
            this.ledgerInfo.setDelLedgers(List.of());
        }
        if (this.appInfo == null) {
            this.appInfo = UserAppInfoDto.builder().build();
        }
        if (this.appInfo.getCategories() == null) {
            this.appInfo.setCategories(List.of());
        }
        if (this.appInfo.getSubcategories() == null) {
            this.appInfo.setSubcategories(List.of());
        }
        if (this.notification == null) {
            this.notification = NotificationDto.builder().build();
        }
        if (this.notification.getFundInvitation() == null) {
            this.notification.setFundInvitation(List.of());
        }
        if (this.notification.getLedgerInvitation() == null) {
            this.notification.setLedgerInvitation(List.of());
        }
        return this;
    }
}
