package com.pokemoney.hadoop.client.service;

import com.pokemoney.hadoop.client.Constants;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncSubcategoryInputDto;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import com.pokemoney.leaf.service.api.LeafGetRequestDto;
import com.pokemoney.leaf.service.api.LeafTriService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Subcategory service in user table
 */
@Slf4j
@Service
public class SubcategoryService {
    /**
     * leaf triple service
     */
    @DubboReference(version = "1.0.0", protocol = "tri", group = "leaf", timeout = 10000)
    private final LeafTriService leafTriService;

    /**
     * Constructor
     *
     * @param leafTriService leaf triple service
     */
    public SubcategoryService(LeafTriService leafTriService) {
        this.leafTriService = leafTriService;
    }

    /**
     * Get subcategory by subcategory id
     *
     * @param syncSubcategoryInputDtos sync subcategory input dto
     * @param userModel                user model
     * @return subcategory dto list
     */
    public List<SubcategoryDto> syncSubcategory(List<SyncSubcategoryInputDto> syncSubcategoryInputDtos, UserModel userModel) {
        Map<Long, SubcategoryDto> subcategoryDtoMap = new HashMap<>();
        for (SyncSubcategoryInputDto syncSubcategoryInputDto : syncSubcategoryInputDtos) {
            if (syncSubcategoryInputDto.getSubcategoryId() <= Constants.MAX_PRESET_SUBCATEGORY_ID) {
                continue;
            }
            SubcategoryDto subcategoryDto = fromSyncSubcategoryInputDto(syncSubcategoryInputDto);
            subcategoryDtoMap.put(subcategoryDto.getSubcategoryId(), subcategoryDto);
        }
        Map<Long, SubcategoryDto> modelSubcategoryDtoMap = userModel.getAppInfo().getSubcategories();
        for (Map.Entry<Long, SubcategoryDto> modelEntry : modelSubcategoryDtoMap.entrySet()) {
            SubcategoryDto modelDto = modelEntry.getValue();
            SubcategoryDto subcategoryDto = subcategoryDtoMap.get(modelEntry.getKey());
            if (subcategoryDto == null) {
                subcategoryDtoMap.put(modelEntry.getKey(), modelDto);
            } else {
                if (modelDto.getUpdateAt() > subcategoryDto.getUpdateAt()) {
                    subcategoryDtoMap.put(modelEntry.getKey(), modelDto);
                }
            }
        }
        return subcategoryDtoMap.values().stream().toList();
    }

    /**
     * Convert sync subcategory input dto to subcategory dto
     *
     * @param syncSubcategoryInputDto sync subcategory input dto
     * @return subcategory dto
     */
    public SubcategoryDto fromSyncSubcategoryInputDto(SyncSubcategoryInputDto syncSubcategoryInputDto) {
        Long id = syncSubcategoryInputDto.getSubcategoryId();
        if (id < com.pokemoney.hadoop.hbase.Constants.MIN_SNOWFLAKE_ID) {
            try{
                id = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_SUBCATEGORY).build()).getId());
            } catch (Exception e) {
                log.error("Get snowflake id error", e);
                throw new RuntimeException("Get snowflake id error", e);
            }
        }
        return new SubcategoryDto(
                id,
                syncSubcategoryInputDto.getCategoryId(),
                syncSubcategoryInputDto.getSubcategoryName(),
                syncSubcategoryInputDto.getUpdateAt(),
                syncSubcategoryInputDto.getDelFlag()
        );
    }
}
