package com.pokemoney.hadoop.hbase.dto.user;

import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import lombok.*;

import java.util.Arrays;
import java.util.List;

/**
 * user ledger book info DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLedgerBookInfoDto {
    /**
     * ledger book IDs
     */
    private List<Long> ledgerIds;
    /**
     * deleted ledger book IDs
     */
    private List<Long> delLedgerIds;

    /**
     * from ledger book model
     *
     * @param ledgerBookModel ledger book model
     * @return ledger book info DTO
     */
    public static UserLedgerBookInfoDto fromLedgerBookModel(UserModel.LedgerInfoModel ledgerBookModel) {
        return UserLedgerBookInfoDto.builder()
                .ledgerIds(Arrays.stream(ledgerBookModel.getLedgers()).toList())
                .delLedgerIds(Arrays.stream(ledgerBookModel.getDelLedgers()).toList())
                .build();
    }
}
