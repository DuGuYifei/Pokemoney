package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Column family: update.
 */
@Getter
@Setter
public class UpdateInfoColumnFamily {
    /**
     * Row update at. Milliseconds since epoch 1970-01-01 00:00:00 UTC.
     */
    private Long updateAt;
    /**
     * Row delete flag.
     */
    private Boolean delFlag;
}
