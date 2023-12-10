package com.pokemoney.hadoop.hbase.phoenix.model;

import com.pokemoney.hadoop.hbase.dto.category.CategoryDto;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;
import com.pokemoney.hadoop.hbase.utils.JsonUtils;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The user model.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserModel {
    /**
     * region id of Row key.
     */
    private Integer regionId;

    /**
     * user id of Row key.
     */
    private Long userId;

    /**
     * User info.
     */
    private UserInfoModel userInfo;

    /**
     * Fund info.
     */
    private FundInfoModel fundInfo;

    /**
     * Ledger book info.
     */
    private LedgerInfoModel ledgerInfo;

    /**
     * App info.
     */
    private AppInfoModel appInfo;

    /**
     * Notification info.
     */
    private NotificationModel notifications;

    /**
     * User info
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfoModel {
        /**
         * User name.
         */
        private String name;
        /**
         * User email.
         */
        private String email;
        /**
         * Update user info at.
         */
        private Long updateUserInfoAt;
    }
    /**
     * Fund info
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FundInfoModel {
        /**
         * Fund RowKey.
         */
        private List<String> funds;
        /**
         * Deleted fund RowKey.
         */
        private List<String> delFunds;
    }
    /**
     * Ledger book info
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LedgerInfoModel {
        /**
         * Ledger book RowKey.
         */
        private List<String> ledgers;
        /**
         * Deleted ledger book RowKey.
         */
        private List<String> delLedgers;
    }
    /**
     * App info
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AppInfoModel {
        /**
         * category map
         */
        private Map<Integer, CategoryDto> categories;
        /**
         * category list in json
         */
        private String jsonCategories;
        /**
         * subcategory map
         */
        private Map<Long, SubcategoryDto> subcategories;
        /**
         * subcategory list in json
         */
        private String jsonSubcategories;

        /**
         * Set category list in json and category list.
         *
         * @param jsonCategories the category list in json
         */
        public void setJsonCategories(String jsonCategories) {
            this.jsonCategories = jsonCategories;
            List<CategoryDto> categories = JsonUtils.categoryListFromJson(jsonCategories);
            this.categories = categories.stream().collect(Collectors.toMap(CategoryDto::getCategoryId, obj-> obj));
        }

        /**
         * Set subcategory list in json and subcategory list.
         *
         * @param jsonSubcategories the subcategory list in json
         */
        public void setJsonSubcategories(String jsonSubcategories) {
            this.jsonSubcategories = jsonSubcategories;
            List<SubcategoryDto> subcategories = SubcategoryDto.subcategoryListFromJson(jsonSubcategories);
            this.subcategories = subcategories.stream().collect(Collectors.toMap(SubcategoryDto::getSubcategoryId, obj-> obj));
        }
    }

    /**
     * Notification model
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationModel {
        /**
         * Notification json string.
         */
        private String notificationsJson;
    }
}
