package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.*;

/**
 * category DTO
 */
@Getter
@Setter
public class CategoryModel {
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
    private com.pokemoney.hadoop.hbase.dto.category.CategoryDto.SubCategoryDto[] subCategories;

    /**
     * SubCategory DTO
     */
    @Getter
    @Setter
    public static class SubCategoryModel {
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

