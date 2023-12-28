package com.pokemoney.hadoop.spark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date createAt;
    private Date updateAt;
    private Integer delFlag;
}
