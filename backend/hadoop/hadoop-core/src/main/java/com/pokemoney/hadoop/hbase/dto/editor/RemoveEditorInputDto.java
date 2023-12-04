package com.pokemoney.hadoop.hbase.dto.editor;

import lombok.*;

/**
 * remove editor input DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RemoveEditorInputDto {
    /**
     * user ID of editor
     */
    private Long userId;
    /**
     * Ledger id or fund id
     */
    private Long targetId;
}
