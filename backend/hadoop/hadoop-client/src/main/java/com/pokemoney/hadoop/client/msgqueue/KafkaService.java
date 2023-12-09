package com.pokemoney.hadoop.client.msgqueue;

import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import com.pokemoney.hadoop.kafka.dto.OperationKafkaMessageDto;
import org.springframework.stereotype.Service;

/**
 * Kafka service
 */
@Service
public class KafkaService {
    /**
     * Send new operation message
     *
     * @param userId user id
     * @param targetTable target table
     * @param targetRowKey target row key
     * @param updateAt update at
     */
    public void sendNewOperationMessage(Long userId, String targetTable, String targetRowKey, Long updateAt) {
        Integer regionId = RowKeyUtils.getRegionId(userId);
        OperationKafkaMessageDto operationKafkaMessageDto = OperationKafkaMessageDto.builder()
                .regionId(regionId)
                .userId(userId)
                .targetTable(targetTable)
                .targetRowKey(targetRowKey)
                .updateAt(updateAt)
                .build();
        // TODO: implement
    }
}
