package com.pokemoney.hadoop.spark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * The simple analysis model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAnalysisModel {
    private Long id;
    private BigDecimal sumMoney;
    private Integer categoryId;
    private Integer typeId;
    private Integer timeAnalysisId;
}
