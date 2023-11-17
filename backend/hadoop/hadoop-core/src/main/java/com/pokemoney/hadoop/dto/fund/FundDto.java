package com.pokemoney.hadoop.dto.fund;

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
     * Fund owner.
     */
    private String owner;
    /**
     * Fund editors.
     */
    private String[] editors;
    /**
     * Fund create at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long createAt;
    /**
     * Fund update at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long updateAt;
}
