package com.pokemoney.hadoop.hbase.dto.sync;

import lombok.*;

/**
 * Sync subcategory input DTO.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SyncSubcategoryInput {
    /**
     * subcategory ID.
     */
    private Long subcategoryId;
    /**
     * category ID.
     */
    private Integer categoryId;
    /**
     * subcategory name.
     */
    private Long subcategoryName;
    /**
     * delete flag.
     */
    private Integer delFlag;
}
