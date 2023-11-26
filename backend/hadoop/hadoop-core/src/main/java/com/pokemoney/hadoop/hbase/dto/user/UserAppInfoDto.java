package com.pokemoney.hadoop.hbase.dto.user;

import com.pokemoney.hadoop.hbase.dto.category.CategoryDto;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;
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
}
