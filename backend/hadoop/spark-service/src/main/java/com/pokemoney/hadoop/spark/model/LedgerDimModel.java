package com.pokemoney.hadoop.spark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date createAt;
    private Date updateAt;
    private Integer delFlag;
}
