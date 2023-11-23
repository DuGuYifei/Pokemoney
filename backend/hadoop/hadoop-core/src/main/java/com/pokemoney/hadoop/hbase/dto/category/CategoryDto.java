package com.pokemoney.hadoop.hbase.dto.category;

import lombok.*;

/**
 * category DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryDto {
    /**
     * category ID.
     */
    private Long categoryId;
    /**
     * category name.
     */
    private String categoryName;

    /**
     * subCategories.
     */
    private SubCategoryDto[] subCategories;

    /**
     * SubCategory DTO
     */
    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SubCategoryDto {
        /**
         * SubCategory ID.
         */
        private Long subCategoryId;
        /**
         * SubCategory name.
         */
        private String subCategoryName;
        /**
         * The time when update.
         */
        private String updateAt;
    }
}
