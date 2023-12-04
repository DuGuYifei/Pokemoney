package com.pokemoney.hadoop.hbase.dto.user;

import com.pokemoney.hadoop.hbase.dto.category.CategoryDto;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import lombok.*;

import java.util.List;

/**
 * user app info DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAppInfoDto {
    /**
     * categories dto
     */
    private List<CategoryDto> categories;
    /**
     * subcategories dto
     */
    private List<SubcategoryDto> subcategories;

    /**
     * from app model
     *
     * @param appModel app model
     * @return app info DTO
     */
    public static UserAppInfoDto fromAppModel(UserModel.AppInfoModel appModel) {
        return UserAppInfoDto.builder()
                .categories(appModel.getCategories().values().stream().toList())
                .subcategories(appModel.getSubcategories().values().stream().toList())
                .build();
    }
}
