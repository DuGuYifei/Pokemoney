package com.pokemoney.hadoop.dto.user;

import com.pokemoney.hadoop.dto.Category.CategoryDto;
import lombok.*;

/**
 * General user DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
    /**
     * User info field
     */
    private UserInfo userInfo;

    /**
     * Fund info field
     */
    private FundInfo fundInfo;

    /**
     * Ledger book info field
     */
    private LedgerBookInfo ledgerBookInfo;

    /**
     * App info field
     */
    private AppInfo appInfo;

    /**
     * User info
     */
    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserInfo {
        /**
         * User ID.
         */
        private Long userId;
    }
    /**
     * Fund info
     */
    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FundInfo {
        /**
         * Fund IDs.
         */
        private String[] funds;
        /**
         * Deleted fund IDs.
         */
        private String[] delFunds;
    }
    /**
     * Ledger book info
     */
    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LedgerBookInfo {
        /**
         * Ledger book IDs.
         */
        private String[] ledgerBooks;
        /**
         * Deleted ledger book IDs.
         */
        private String[] delLedgerBooks;
    }
    /**
     * App info
     */
    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AppInfo {
        /**
         * Category list
         */
        private CategoryDto[] categories;
    }
}
