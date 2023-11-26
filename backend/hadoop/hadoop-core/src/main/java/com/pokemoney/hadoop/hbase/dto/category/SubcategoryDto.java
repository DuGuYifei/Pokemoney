package com.pokemoney.hadoop.hbase.dto.category;

import lombok.*;

/**
 * category DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubcategoryDto {
    /**
     * subcategory ID.
     */
    private Long subcategoryId;
    /**
     * category ID.
     */
    private Long categoryId;
    /**
     * subcategory name.
     */
    private String subcategoryName;
    /**
     * update time.
     */
    private Long updateAt;
    /**
     * delete flag.
     */
    private Integer delFlag;
}
