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
     * deleted flag.
     */
    private Integer delFlag;
}
