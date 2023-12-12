package com.pokemoney.hadoop.hbase.dto.category;

import lombok.*;
import org.apache.hbase.thirdparty.com.google.gson.annotations.SerializedName;

/**
 * category DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryDto {
    /**
     * category ID.
     */
    @SerializedName("category_id")
    private Integer categoryId;
    /**
     * category name.
     */
    @SerializedName("category_name")
    private String categoryName;
}
