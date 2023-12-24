package com.pokemoney.hadoop.spark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The ledger dimension model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerDimModel {
    private Long ledgerId;
    private Long ownerId;
    private String name;
    private Double budget;
    private Long createAt;
    private Long updateAt;
    private Integer delFlag;
}
