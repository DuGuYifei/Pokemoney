package com.pokemoney.hadoop.service.kafka.consumer;

import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.OperationMapper;
import com.pokemoney.hadoop.hbase.utils.JsonUtils;
import com.pokemoney.hadoop.kafka.dto.OperationKafkaMessageDto;
import com.pokemoney.hadoop.service.kafka.exception.LockAcquireException;
import com.pokemoney.hadoop.zookeeper.Constants;
import com.pokemoney.leaf.service.api.LeafGetRequestDto;
import com.pokemoney.leaf.service.api.LeafResponseDto;
import com.pokemoney.leaf.service.api.LeafTriService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.phoenix.shaded.org.apache.curator.framework.CuratorFramework;
import org.apache.phoenix.shaded.org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Consumer of operation message
 */
@Slf4j
@Service
public class OperationMessageConsumer {
    /**
     * Operation mapper
     */
    private final OperationMapper operationMapper;
    /**
     * Curator framework
     */
    private final CuratorFramework curatorFramework;
    /**
     * Leaf triple service
     */
    @DubboReference(version = "1.0.0", protocol = "tri", group = "leaf", timeout = 10000)
    private final LeafTriService leafTriService;

    /**
     * Constructor
     *
     * @param operationMapper  operation mapper
     * @param curatorFramework curator framework
     * @param leafTriService   leaf triple service
     */
    public OperationMessageConsumer(OperationMapper operationMapper, CuratorFramework curatorFramework, LeafTriService leafTriService) {
        this.operationMapper = operationMapper;
        this.curatorFramework = curatorFramework;
        this.leafTriService = leafTriService;
    }

    /**
     * Consume operation message
     *
     * @param operationMessage operation message
     * @throws Exception exception
     */
    public void consumeOperationMessage(String operationMessage) throws Exception {
        log.info("Consuming operation message: {}", operationMessage);
        OperationKafkaMessageDto operationKafkaMessageDto = JsonUtils.GSON.fromJson(operationMessage, OperationKafkaMessageDto.class);
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, Constants.ExclusiveOperationMutexPathPrefix + operationKafkaMessageDto.getUserId());
        try {
            if (lock.acquire(3, TimeUnit.MINUTES)) {
                LeafResponseDto leafResponseDto;
                try {
                    leafResponseDto = leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_OPERATION).build());
                } catch (Exception e) {
                    log.error("Failed to get snowflake id for operation", e);
                    return;
                }
                long operationId = Long.parseLong(leafResponseDto.getId());
                OperationDto operationDto = new OperationDto(
                        operationKafkaMessageDto.getRegionId(),
                        operationKafkaMessageDto.getUserId(),
                        Long.MAX_VALUE - operationId,
                        operationId,
                        operationKafkaMessageDto.getTargetTable(),
                        operationKafkaMessageDto.getTargetRowKey(),
                        operationKafkaMessageDto.getUpdateAt()
                );
                operationMapper.insertOperation(operationDto);
            } else {
                log.error("Failed to acquire lock");
                throw new LockAcquireException();
            }
        }catch (LockAcquireException e) {
            log.error("Failed to acquire lock", e);
            throw e;
        }catch (Exception e) {
            log.error("Failed to consume operation message", e);
            throw e;
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                log.error("Failed to release lock", e);
            }
        }
    }
}
