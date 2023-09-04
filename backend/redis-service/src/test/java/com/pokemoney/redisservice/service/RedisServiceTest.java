package com.pokemoney.redisservice.service;

import com.pokemoney.redisservice.entity.TestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

/**
 * Tests for {@link RedisService}.
 */
@SpringBootTest
public class RedisServiceTest {
    @Autowired
    private RedisService redisService;

    /**
     * Test for insert a {@link TestEntity} into Redis.
     */
    @Test
    public void testInsertObject() {
        TestEntity testEntity = new TestEntity("testField1", 1);
        Boolean result = redisService.getRedisTemplate().opsForValue().setIfAbsent("testKey", testEntity);
        if (Boolean.TRUE.equals(result)) {
            TestEntity resultEntity = (TestEntity) redisService.getRedisTemplate().opsForValue().get("testKey");
            assert Objects.equals(resultEntity, testEntity);
        } else {
            redisService.getRedisTemplate().delete("testKey");
            redisService.getRedisTemplate().opsForValue().setIfAbsent("testKey", testEntity);
            TestEntity resultEntity = (TestEntity) redisService.getRedisTemplate().opsForValue().get("testKey");
            assert Objects.equals(resultEntity, testEntity);
        }
    }
}
