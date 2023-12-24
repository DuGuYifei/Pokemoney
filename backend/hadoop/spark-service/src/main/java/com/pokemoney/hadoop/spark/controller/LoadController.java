package com.pokemoney.hadoop.spark.controller;

import com.pokemoney.hadoop.spark.service.SparkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Load data warehouse controller
 */
@RestController
@RequestMapping("/api/v1/spark/load")
public class LoadController {
    /**
     * The spark service.
     */
    private final SparkService sparkService;

    /**
     * The constructor.
     *
     * @param sparkService the spark service.
     */
    public LoadController(SparkService sparkService) {
        this.sparkService = sparkService;
    }


    /**
     * Load fund data. Auth by user:root, password:111
     */
    @PostMapping("/fund")
    public void loadFund() {
        sparkService.upsertAllFunds();
    }

    /**
     * Load ledger data. Auth by user:root, password:111
     */
    @PostMapping("/ledger")
    public void loadLedger() {
        sparkService.upsertAllLedgers();
    }

    /**
     * Load transaction data. Auth by user:root, password:111
     */
    @PostMapping("/transaction")
    public void loadTransaction() {
        sparkService.upsertAllTransactionsCurrentMonth();
    }
}
