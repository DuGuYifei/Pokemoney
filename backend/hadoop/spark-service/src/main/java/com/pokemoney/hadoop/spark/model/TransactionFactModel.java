package com.pokemoney.hadoop.spark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The transaction fact model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFactModel {
    private Long id;
    private BigDecimal money;
    private Long fundId;
    private Long ledgerId;
    private Integer typeId;
    private Integer categoryId;
    private Date happenAt;
    private Integer happenTimeId;
    private Integer happenTimeAnalysisId;
    private Long updateBy;
    private Date updateAt;
    private Integer updateTimeId;
    private Integer updateTimeAnalysisId;
    private Integer delFlag;
}
