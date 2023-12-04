package com.pokemoney.hadoop.hbase.phoenix.model;

import lombok.*;

/**
 * The table log operation
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OperationModel {
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
     * The operation info.
     */
    private OperationInfoModel operationInfo;

    /**
     * The operation info model
     */
    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OperationInfoModel {
        /**
         * The operation id.
         */
        private Long id;
        /**
         * The operation target table name
         */
        private String targetTable;
        /**
         * The operation target row key
         */
        private String targetRowKey;
    }

    /**
     * The date time when the operation is created or update
     */
    private Long updateAt;
}
