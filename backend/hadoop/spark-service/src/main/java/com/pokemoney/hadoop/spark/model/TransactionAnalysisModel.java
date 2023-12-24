package com.pokemoney.hadoop.spark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The simple analysis model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAnalysisModel {
    private Long id;
    private Double sumMoney;
    private Integer categoryId;
    private Integer typeId;
    private Integer timeAnalysisId;
}
