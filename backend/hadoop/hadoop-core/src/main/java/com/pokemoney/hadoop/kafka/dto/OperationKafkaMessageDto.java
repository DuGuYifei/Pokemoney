package com.pokemoney.hadoop.kafka.dto;

import lombok.*;

/**
 * Hbase operation table message DTO.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OperationKafkaMessageDto {
    /**
     * row key - region id
     */
    private Integer regionId;
    /**
     * row key - user id
     */
    private Long userId;
    /**
     * operation_info.target_table
     */
    private String targetTable;
    /**
     * operation_info.target_row_key
     */
    private String targetRowKey;
    /**
     * update_info.update_at
     */
    private Long updateAt;
}
