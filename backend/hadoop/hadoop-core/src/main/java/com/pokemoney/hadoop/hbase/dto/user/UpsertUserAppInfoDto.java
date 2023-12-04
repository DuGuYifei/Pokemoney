package com.pokemoney.hadoop.hbase.dto.user;

import lombok.*;

/**
 * user app info upsert DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpsertUserAppInfoDto {
    /**
     * json string of categories
     */
    private String jsonCategories;
    /**
     * json string of subcategories
     */
    private String jsonSubcategories;
}
