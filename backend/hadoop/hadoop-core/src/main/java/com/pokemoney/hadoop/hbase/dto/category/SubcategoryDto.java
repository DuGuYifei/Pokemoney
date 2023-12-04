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
public class SubcategoryDto {
    /**
     * subcategory ID.
     */
    @SerializedName("subcategory_id")
    private Long subcategoryId;
    /**
     * category ID.
     */
    @SerializedName("category_id")
    private Integer categoryId;
    /**
     * subcategory name.
     */
    @SerializedName("subcategory_name")
    private String subcategoryName;
    /**
     * update time.
     */
    @SerializedName("update_at")
    private Long updateAt;
    /**
     * delete flag.
     */
    @SerializedName("del_flag")
    private Integer delFlag;
}
