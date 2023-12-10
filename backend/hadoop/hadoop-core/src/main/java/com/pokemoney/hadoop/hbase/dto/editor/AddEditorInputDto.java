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
     * User id
     */
    private Long invitorId;
    /**
     * email of editor
     */
    private String invitedEmail;
    /**
     * Ledger id or fund id
     */
    private Long targetId;
}
