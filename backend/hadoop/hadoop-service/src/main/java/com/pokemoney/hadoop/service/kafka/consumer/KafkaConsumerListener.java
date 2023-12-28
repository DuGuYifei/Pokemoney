package com.pokemoney.hadoop.service.kafka.consumer;

import com.pokemoney.hadoop.kafka.dto.TopicConstants;
import com.pokemoney.hadoop.service.kafka.producer.OperationMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
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
    @KafkaListener(topics = "operation_insert", groupId = "hadoop-service")
    public void listenerMessage(String message, Acknowledgment ack) {
        System.out.println(message);
        log.info("Receive kafka message, value: {}", message);
//        log.info("Receive kafka message, value: {}, headers: {}, partition: {}, topic: {}", record.value(), record.headers(), record.partition(), record.topic());
        try {
            operationMessageConsumer.consumeOperationMessage(message);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Consume operation message error", e);
            if(message.startsWith("{")) {
                operationMessageProducer.sendNewOperationMessage(message);
            }
        }
    }
}
