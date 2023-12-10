package com.pokemoney.hadoop.hbase.dto.fund;


import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import com.pokemoney.hadoop.hbase.phoenix.model.FundModel;
import lombok.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fund DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FundDto {
//    /**
//     * fund field from request to database field mapping.
//     */
//    public static final Map<String, String> FIELD_NAME_MAPPING = new HashMap<>() {{
//        put("fundId", "fund_id");
//        put("name", "fund_info.name");
//        put("balance", "fund_info.balance");
//        put("editors", "fund_info.editors");
//        put("owner", "fund_info.owner");
//        put("createAt", "fund_info.create_at");
//        put("updateAt", "update_info.update_at");
//        put("delFlag", "update_info.del_flag");
//    }};
    /**
     * fund ID.
     */
    private Long fundId;
    /**
     * fund name.
     */
    private String name;
    /**
     * balance of fund.
     */
    private Float balance;
    /**
     * user id of the owner of fund.
     */
    private Long owner;
    /**
     * editors of the editors of fund.
     */
    private List<EditorDto> editors;
    /**
     * create time.
     */
    private Long createAt;
    /**
     * update time.
     */
    private Long updateAt;
    /**
     * delete flag.
     */
    private Integer delFlag;
}
