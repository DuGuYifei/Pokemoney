package com.pokemoney.hadoop.client.controller;

import com.pokemoney.hadoop.dto.ledger.LedgerBookDto;
import com.pokemoney.hadoop.client.filter.LedgerBookFilter;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Ledger book graphql controller
 */
@Controller
public class LedgerBookController {
    /**
     * Get ledger book by ledger id.
     *
     * @param ledgerId ledger id
     * @return ledger book {@link LedgerBookDto}
     */
    @QueryMapping
    public LedgerBookDto getLedger(@Argument Long ledgerId) {
        System.out.println("ledgerId: " + ledgerId);
        return LedgerBookDto.builder()
                .budget(1000.0f)
                .ledgerId(ledgerId)
                .createdAt("2020-01-01 00:00:00")
                .editors(new String[0])
                .name("test")
                .owner("test")
                .build();
    }

    /**
     * Get ledger books by filter.
     *
     * @param filter filter {@link LedgerBookFilter}
     * @return ledger books {@link LedgerBookDto}
     */
    @QueryMapping
    public List<LedgerBookDto> getLedgers(@Argument LedgerBookFilter filter) {
        List<LedgerBookDto> ledgers = new ArrayList<>();
        ledgers.add(LedgerBookDto.builder().build());
        return ledgers;
    }
}
