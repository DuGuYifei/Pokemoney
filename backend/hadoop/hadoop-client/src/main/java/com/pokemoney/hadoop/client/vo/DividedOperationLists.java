package com.pokemoney.hadoop.client.vo;

import com.pokemoney.hadoop.hbase.phoenix.model.OperationModel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

/**
 * Divided operation lists.
 */
@Getter
@Setter
public class DividedOperationLists {
    /**
     * Fund operation list.
     */
    private LinkedList<OperationModel> FundOperationList = new LinkedList<>();
    /**
     * Ledger operation list.
     */
    private LinkedList<OperationModel> LedgerOperationList = new LinkedList<>();
    /**
     * Transaction operation list.
     */
    private LinkedList<OperationModel> TransactionOperationList = new LinkedList<>();
}
