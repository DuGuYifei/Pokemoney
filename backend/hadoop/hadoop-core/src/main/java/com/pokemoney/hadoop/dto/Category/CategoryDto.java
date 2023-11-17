package com.pokemoney.hadoop.dto.Category;

import lombok.*;

/**
 * Category DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryDto {
    /**
     * Category ID.
     */
    private Long categoryId;
    /**
     * Category name.
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
    }
}
