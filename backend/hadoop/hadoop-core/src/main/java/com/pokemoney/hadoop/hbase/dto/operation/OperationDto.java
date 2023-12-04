package com.pokemoney.hadoop.hbase.dto.operation;


import lombok.*;

/**
 * The operation dto
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OperationDto {
    /**
     * The region id.
     */
    private Integer regionId;
    /**
     * The user id.
     */
    private Long userId;
    /**
     * long.max - operationId
     */
    private Long reverseOperationId;
    /**
     * The operation id
     */
    private String operationId;
    /**
     * The operation target table name
     */
    private String targetTable;
    /**
     * The operation target row key
     */
    private String targetRowKey;
    /**
     * The date time when the operation is created or update
     */
    private Long updateAt;
}
