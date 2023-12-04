package com.pokemoney.hadoop.hbase.dto.ledger;

import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import lombok.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ledger book DTO
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LedgerDto {
    /**
     * ledger book field from request to database field mapping.
     */
    public static final Map<String, String> FIELD_NAME_MAPPING = new HashMap<>() {{
        put("ledgerId", "ledger_id");
        put("name", "ledger_info.name");
        put("budget", "ledger_info.budget");
        put("editors", "ledger_info.editors");
        put("owner", "ledger_info.owner");
        put("createAt", "ledger_info.create_at");
        put("updateAt", "update_info.update_at");
        put("delFlag", "update_info.del_flag");
    }};
    /**
     * ledger book ID.
     */
    private Long ledgerId;
    /**
     * ledger book name.
     */
    private String name;
    /**
     * budget of ledger book.
     */
    private Float budget;
    /**
     * user id of the owner of ledger book.
     */
    private Long owner;
    /**
     * editors of the editors of ledger book.
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
