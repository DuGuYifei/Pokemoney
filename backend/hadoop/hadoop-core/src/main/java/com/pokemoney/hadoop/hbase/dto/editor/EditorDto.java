package com.pokemoney.hadoop.hbase.dto.editor;

import lombok.*;

/**
 * editor DTO
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditorDto {
    /**
     * user ID.
     */
    private Long userId;
    /**
     * email
     */
    private String email;
    /**
     * name
     */
    private String name;
}
