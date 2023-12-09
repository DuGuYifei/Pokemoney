package com.pokemoney.hadoop.hbase.dto.category;

import com.pokemoney.hadoop.hbase.utils.JsonUtils;
import lombok.*;
import org.apache.hbase.thirdparty.com.google.gson.annotations.SerializedName;
import org.apache.hbase.thirdparty.com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


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


    /**
     * Convert subcategory json to dto map
     *
     * @param json json string
     * @return SubcategoryDto list
     */
    public static List<SubcategoryDto> subcategoryListFromJson(String json) {
        Type type = new TypeToken<List<SubcategoryDto>>() {}.getType();
        return JsonUtils.GSON.fromJson(json, type);
    }
}
