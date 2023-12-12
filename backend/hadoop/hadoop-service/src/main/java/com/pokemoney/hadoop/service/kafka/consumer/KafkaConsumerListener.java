package com.pokemoney.hadoop.service.kafka.consumer;

import com.pokemoney.hadoop.kafka.dto.TopicConstants;
import com.pokemoney.hadoop.service.kafka.producer.OperationMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer services
 */
@Slf4j
@Service
public class KafkaConsumerListener {
    /**
     * Operation message consumer
     */
    private final OperationMessageConsumer operationMessageConsumer;

    /**
     * Operation message producer
     */
    private final OperationMessageProducer operationMessageProducer;

    /**
     * Constructor
     *
     * @param operationMessageConsumer operation message consumer
     * @param operationMessageProducer operation message producer
     */
    public KafkaConsumerListener(OperationMessageConsumer operationMessageConsumer, OperationMessageProducer operationMessageProducer) {
        this.operationMessageConsumer = operationMessageConsumer;
        this.operationMessageProducer = operationMessageProducer;
    }

    /**
     * Listener message
     */
    @KafkaListener(topics = {TopicConstants.OPERATION_INSERT_TOPIC}, concurrency = "2")
    public void listenerMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("Receive kafka message, key: {}, value: {}, headers: {}, partition: {}, topic: {}", record.key(), record.value(), record.headers(), record.partition(), record.topic());
        try {
            operationMessageConsumer.consumeOperationMessage(record.value());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Consume operation message error", e);
            operationMessageProducer.sendNewOperationMessage(record.value());
        }
    }
}
