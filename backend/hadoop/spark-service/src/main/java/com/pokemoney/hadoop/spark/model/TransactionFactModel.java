package com.pokemoney.hadoop.spark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The transaction fact model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFactModel {
    private Long id;
    private Double money;
    private Long fundId;
    private Long ledgerId;
    private Integer typeId;
    private Integer categoryId;
    private Long happenAt;
    private Integer happenTimeId;
    private Integer happenTimeAnalysisId;
    private Long updateBy;
    private Long updateAt;
    private Integer updateTimeId;
    private Integer updateTimeAnalysisId;
    private Integer delFlag;
}
