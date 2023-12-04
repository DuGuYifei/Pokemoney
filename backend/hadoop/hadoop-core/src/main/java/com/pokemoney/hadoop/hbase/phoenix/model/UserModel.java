package com.pokemoney.hadoop.hbase.phoenix.model;

import com.pokemoney.hadoop.hbase.dto.category.CategoryDto;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;
import com.pokemoney.hadoop.hbase.utils.JsonUtils;
import lombok.*;

import java.util.Map;

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
    private UserInfo userInfo;

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
     * User info
     */
    @Getter
    @Setter
    public static class UserInfo {
        /**
         * User name.
         */
        private String name;
        /**
         * User email.
         */
        private String email;
    }
    /**
     * Fund info
     */
    @Getter
    @Setter
    public static class FundInfoModel {
        /**
         * Fund IDs.
         */
        private Long[] funds;
        /**
         * Deleted fund IDs.
         */
        private Long[] delFunds;
    }
    /**
     * Ledger book info
     */
    @Getter
    @Setter
    public static class LedgerInfoModel {
        /**
         * Ledger book IDs.
         */
        private Long[] ledgers;
        /**
         * Deleted ledger book IDs.
         */
        private Long[] delLedgers;
    }
    /**
     * App info
     */
    @Getter
    @Setter
    public static class AppInfoModel {
        /**
         * category map
         */
        private Map<String, CategoryDto> categories;
        /**
         * category list in json
         */
        private String jsonCategories;
        /**
         * subcategory map
         */
        private Map<String, SubcategoryDto> subcategories;
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
            this.categories = JsonUtils.categoryFromJson(jsonCategories);
        }

        /**
         * Set subcategory list in json and subcategory list.
         *
         * @param jsonSubcategories the subcategory list in json
         */
        public void setJsonSubcategories(String jsonSubcategories) {
            this.jsonSubcategories = jsonSubcategories;
            this.subcategories = JsonUtils.subcategoryFromJson(jsonSubcategories);
        }
    }
}
