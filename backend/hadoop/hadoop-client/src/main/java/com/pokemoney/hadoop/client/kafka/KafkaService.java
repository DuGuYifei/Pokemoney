package com.pokemoney.hadoop.client.kafka;

import com.pokemoney.hadoop.hbase.utils.JsonUtils;
import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import com.pokemoney.hadoop.kafka.dto.OperationKafkaMessageDto;
import com.pokemoney.hadoop.kafka.dto.TopicConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Kafka service
 */
@Slf4j
@Service
public class KafkaService {
    /**
     * The template of kafka
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Constructor
     *
     * @param kafkaTemplate    kafka template
     */
    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

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
        String jsonOperationMsg = JsonUtils.GSON.toJson(operationKafkaMessageDto);
        try {
            sendSyncMessage(TopicConstants.OPERATION_INSERT_TOPIC, jsonOperationMsg);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Send sync message error, topic: {}, data: {}", TopicConstants.OPERATION_INSERT_TOPIC, jsonOperationMsg, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Send sync message
     *
     * @param topic topic
     * @param data data
     * @throws ExecutionException execution exception
     * @throws InterruptedException interrupted exception
     */
    public void sendSyncMessage(String topic, String data) throws ExecutionException, InterruptedException {
        SendResult<String, String> sendResult = kafkaTemplate.send(topic, data).get();
        RecordMetadata recordMetadata = sendResult.getRecordMetadata();
        log.info("Send sync message success, topic: {}, partition: {}, offset: {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
    }
}
