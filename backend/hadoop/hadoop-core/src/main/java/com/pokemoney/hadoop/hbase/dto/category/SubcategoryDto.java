package com.pokemoney.hadoop.hbase.dto.category;

import lombok.*;

/**
 * category DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Subcategory {
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
    private String updateAt;
    /**
     * delete time.
     */
    private String deleteAt;
}
