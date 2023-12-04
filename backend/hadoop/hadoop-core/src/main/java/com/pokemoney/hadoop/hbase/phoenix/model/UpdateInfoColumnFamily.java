package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.*;

/**
 * Column family: update.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateInfoColumnFamily {
    /**
     * Row update at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long updateAt;
    /**
     * Row delete flag.
     */
    private Integer delFlag;
}
