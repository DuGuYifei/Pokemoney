package com.pokemoney.hadoop.hbase.dto.sync;

import com.pokemoney.hadoop.hbase.dto.category.CategoryDto;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;
import com.pokemoney.hadoop.hbase.dto.fund.FundDto;
import com.pokemoney.hadoop.hbase.dto.ledger.LedgerDto;
import com.pokemoney.hadoop.hbase.dto.transaction.TransactionDto;
import com.pokemoney.hadoop.hbase.dto.user.NotificationDto;
import com.pokemoney.hadoop.hbase.dto.user.UserDto;
import lombok.*;

import java.util.List;

/**
 * Sync DTOs.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SyncResponseDto {
    /**
     * user dto
     */
    private UserDto user;
    /**
     * funds dto
     */
    private List<FundDto> funds;
    /**
     * ledgers dto
     */
    private List<LedgerDto> ledgers;
    /**
     * transactions dto
     */
    private List<TransactionDto> transactions;
    /**
     * categories dto
     */
    private List<CategoryDto> categories;
    /**
     * subcategories dto
     */
    private List<SubcategoryDto> subcategories;
    /**
     * notifications dto
     */
    private NotificationDto notifications;
    /**
     * operation ID
     */
    private Long operationId;
}
