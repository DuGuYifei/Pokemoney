package com.pokemoney.hadoop.hbase.utils;


import com.pokemoney.hadoop.hbase.dto.category.CategoryDto;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;
import org.apache.hbase.thirdparty.com.google.gson.Gson;
import org.apache.hbase.thirdparty.com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to convert object to json string.
 */
public class JsonUtils {
    /**
     * Gson instance.
     */
    public static final Gson GSON = new Gson();

    /**
     * Convert category json to dtp map
     *
     * @param json json string
     * @return CategoryDto list
     */
    public static List<CategoryDto> categoryListFromJson(String json) {
        Type type = new TypeToken<List<CategoryDto>>() {}.getType();
        return GSON.fromJson(json, type);
    }

    /**
     * Convert subcategory json to dto map
     *
     * @param json json string
     * @return SubcategoryDto list
     */
    public static List<SubcategoryDto> subcategoryListFromJson(String json) {
        Type type = new TypeToken<List<SubcategoryDto>>() {}.getType();
        return GSON.fromJson(json, type);
    }

    /**
     * Convert subcategory list to json string
     *
     * @param subcategoryDtos subcategory list
     * @return json string
     */
    public static String subcategoryDtoToJson(SubcategoryDto[] subcategoryDtos) {
        Map<String, SubcategoryDto> subcategoryModelMap = new HashMap<>();
        for (SubcategoryDto subcategoryDto : subcategoryDtos) {
            SubcategoryDto subcategoryModel = new SubcategoryDto(
                    subcategoryDto.getSubcategoryId(),
                    subcategoryDto.getCategoryId(),
                    subcategoryDto.getSubcategoryName(),
                    subcategoryDto.getUpdateAt(),
                    subcategoryDto.getDelFlag()
            );
            subcategoryModelMap.put(String.valueOf(subcategoryModel.getSubcategoryId()), subcategoryModel);
        }
        return GSON.toJson(subcategoryModelMap);
    }
}
