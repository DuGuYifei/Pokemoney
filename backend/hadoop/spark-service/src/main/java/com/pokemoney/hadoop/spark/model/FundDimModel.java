package com.pokemoney.hadoop.spark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The fund dimension model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundDimModel {
    private Long fundId;
    private Long ownerId;
    private String name;
    private Double balance;
    private Long createAt;
    private Long updateAt;
    private Integer delFlag;
}
