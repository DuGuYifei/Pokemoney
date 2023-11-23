package com.pokemoney.hadoop.hbase.dto.fund;

import lombok.*;

/**
 * General Fund DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FundDto {
    /**
     * The field name mapping to column name.
     */
    public static final String[] FIELD_NAME_MAPPING = {
            "fundId", "fund_info.fund_id",
            "name", "fund_info.name",
            "balance", "fund_info.balance",
            "owner", "fund_info.owner",
            "editors", "fund_info.editors",
            "createAt", "fund_info.create_at",
            "updateAt", "update_info.update_at"
    };
    /**
     * Fund ID.
     */
    private Long fundId;
    /**
     * Fund name.
     */
    private String name;
    /**
     * Fund balance.
     */
    private Float balance;
    /**
     * Fund editors.
     */
    private String[] editors;
    /**
     * Fund owner.
     */
    private String owner;
    /**
     * Fund create at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long createAt;
    /**
     * Fund update at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long updateAt;
}
