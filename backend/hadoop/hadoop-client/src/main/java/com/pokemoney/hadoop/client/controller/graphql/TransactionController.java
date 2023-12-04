package com.pokemoney.hadoop.client.controller.graphql;

import com.pokemoney.hadoop.hbase.dto.filter.TransactionFilter;
import com.pokemoney.hadoop.hbase.dto.transaction.TransactionDto;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Transaction controller
 */
@Controller
public class TransactionController {
    /**
     * get transaction by transaction id
     *
     * @param transactionId transaction id
     * @return transaction
     */
    public TransactionDto getTransaction(Long transactionId) {
        return null;
    }

    /**
     * get transactions by filter
     *
     * @param filter filter
     * @return transactions
     */
    public List<TransactionDto> getTransactions(TransactionFilter filter) {
        return null;
    }
}
