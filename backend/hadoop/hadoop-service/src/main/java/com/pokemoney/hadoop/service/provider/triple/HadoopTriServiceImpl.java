package com.pokemoney.hadoop.service.provider.triple;

import com.pokemoney.commons.proto.ResponseCommonPart;
import com.pokemoney.hadoop.hbase.dto.user.UpsertUserAppInfoDto;
import com.pokemoney.hadoop.hbase.dto.user.UpsertUserDto;
import com.pokemoney.hadoop.hbase.enums.CategoryEnum;
import com.pokemoney.hadoop.hbase.phoenix.dao.UserMapper;
import com.pokemoney.hadoop.hbase.utils.JsonUtils;
import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import com.pokemoney.hadoop.service.api.HadoopTriService;
import com.pokemoney.hadoop.service.api.InsertUserRequestDto;
import com.pokemoney.hadoop.service.api.InsertUserResponseDto;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * This class is the implementation of the HadoopTriService interface.
 */
@DubboService(version = "1.0.0", protocol = "tri", group = "hadoop", timeout = 10000)
public class HadoopTriServiceImpl implements HadoopTriService {
    /**
     * User mapper.
     */
    private final UserMapper userMapper;

    /**
     * Constructor.
     *
     * @param userMapper User mapper.
     */
    public HadoopTriServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * insert a new user.
     *
     * @param request {@link InsertUserRequestDto}
     * @return {@link InsertUserResponseDto}
     */
    @Override
    public InsertUserResponseDto insertUser(InsertUserRequestDto request) {
        long userId = request.getUserId();
        String userName = request.getUsername();
        String email = request.getEmail();

        UpsertUserAppInfoDto upsertUserAppInfoDto = UpsertUserAppInfoDto.builder()
                .jsonCategories(JsonUtils.GSON.toJson(CategoryEnum.getInitCategoryDtoList()))
                .jsonSubcategories(JsonUtils.GSON.toJson(CategoryEnum.getInitSubcategoryList()))
                .build();
        UpsertUserDto upsertUserDto = new UpsertUserDto(
                RowKeyUtils.getRegionId(userId),
                userId,
                email,
                userName,
                System.currentTimeMillis(),
                null,
                null,
                upsertUserAppInfoDto,
                null
        );
        userMapper.insertUser(upsertUserDto);
        return InsertUserResponseDto.newBuilder()
                .setResponseCommonPart(
                        ResponseCommonPart.newBuilder()
                                .setStatus(1)
                                .setMessage("Insert successfully.")
                                .build()
                )
                .build();
    }
}
