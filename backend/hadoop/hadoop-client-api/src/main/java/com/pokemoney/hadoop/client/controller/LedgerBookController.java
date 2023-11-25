package com.pokemoney.hadoop.client.controller;

import com.pokemoney.hadoop.hbase.dto.ledger.LedgerDto;
import com.pokemoney.hadoop.hbase.dto.filter.LedgerBookFilter;
import graphql.schema.DataFetchingEnvironment;
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
     * @return ledger book {@link LedgerDto}
     */
    @QueryMapping
    public LedgerDto getLedger(@Argument Long ledgerId, DataFetchingEnvironment env) {
        List<String> selectedFieldsName = env.getSelectionSet().getFields()
                .stream()
                .map(selectedField -> LedgerDto.FIELD_NAME_MAPPING.get(selectedField.getName()))
                .toList();
        System.out.println("selectedFieldsName: " + selectedFieldsName);
        System.out.println("ledgerId: " + ledgerId);
//        throw new GraphqlErrorException.Builder().message("test").path("test1").build();
        return LedgerDto.builder()
                .budget(1000.0f)
                .ledgerId(ledgerId)
                .createAt(1L)
                .updateAt(1L)
                .editors(List.of())
                .name("test")
                .owner(1L)
                .build();
    }

    /**
     * Get ledger books by filter.
     *
     * @param filter filter {@link LedgerBookFilter}
     * @return ledger books {@link LedgerDto}
     */
    @QueryMapping
    public List<LedgerDto> getLedgers(@Argument LedgerBookFilter filter) {
        List<LedgerDto> ledgers = new ArrayList<>();
        ledgers.add(LedgerDto.builder().build());
        return ledgers;
    }
}
