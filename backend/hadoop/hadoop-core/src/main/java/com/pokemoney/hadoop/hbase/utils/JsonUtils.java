package com.pokemoney.hadoop.hbase.utils;


import com.pokemoney.hadoop.hbase.dto.category.CategoryDto;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncSubcategoryInputDto;
import com.pokemoney.hadoop.hbase.phoenix.model.CategoryModel;
import com.pokemoney.hadoop.hbase.phoenix.model.SubcategoryModel;
import org.apache.hbase.thirdparty.com.google.gson.Gson;
import org.apache.hbase.thirdparty.com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
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
     * @return object
     */
    public static Map<String, CategoryDto> categoryFromJson(String json) {
        Type type = new TypeToken<Map<String, CategoryDto>>() {}.getType();
        return GSON.fromJson(json, type);
    }

    /**
     * Convert subcategory json to dto map
     *
     * @param json json string
     * @return object
     */
    public static Map<String, SubcategoryDto> subcategoryFromJson(String json) {
        Type type = new TypeToken<Map<String, SubcategoryDto>>() {}.getType();
        return GSON.fromJson(json, type);
    }

    /**
     * Convert subcategory list to json string
     *
     * @param subcategoryDtos subcategory list
     * @return json string
     */
    public static String subcategoryDtoToJson(SubcategoryDto[] subcategoryDtos) {
        Map<String, SubcategoryModel> subcategoryModelMap = new HashMap<>();
        for (SubcategoryDto subcategoryDto : subcategoryDtos) {
            SubcategoryModel subcategoryModel = new SubcategoryModel(
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
