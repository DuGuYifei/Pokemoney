package com.pokemoney.hadoop.hbase.dto.fund;


import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * fund DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Fund {
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
    private Date createAt;
    /**
     * update time.
     */
    private Date updateAt;
    /**
     * delete flag.
     */
    private Integer delFlag;
}
