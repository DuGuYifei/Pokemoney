package com.pokemoney.hadoop.hbase.dto.editor;

import lombok.*;



/**
 * add editor input DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddEditorInputDto {
    /**
     * email of editor
     */
    private String email;
    /**
     * Ledger id or fund id
     */
    private Long targetId;
}
