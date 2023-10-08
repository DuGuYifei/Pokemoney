package com.pokemoney.commons.redis;

import com.pokemoney.commons.redis.validation.RedisGetValueGroup;
import com.pokemoney.commons.redis.validation.RedisSetValueGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * DTO for redis hash key value which will be used in redis-service.
 */
@Data
@Builder
public class RedisHashKeyValueDto {
    /**
     * The key of redis. Constraints:
     * Must be not blank.
     */
    @NotBlank(groups = {RedisGetValueGroup.class, RedisSetValueGroup.class}, message = "Key must be not blank.")
    private String key;

    /**
     * The value of redis. Constraints:
     * Must be not blank if set value.
     */
    @NotBlank(groups = {RedisSetValueGroup.class}, message = "Map value must be not blank.")
    private Map<String, String> value;

    /**
     * Value timeout in unit of second. Constraints:
     * 1. not required.
     * 2. If not provided, the default is no timeout.
     * 3. If provided, must be between 1 and 2592000 seconds (30 days).
     * 4. As return value, if it is -2 means the key does not exist
     * 5. As return value, -1 means the key exists but has no associated expire.
     */
    @Min(groups = {RedisGetValueGroup.class, RedisSetValueGroup.class}, value = 1, message = "Value timeout must be between 1 and 2592000 seconds (30 days).")
    @Max(groups = {RedisGetValueGroup.class, RedisSetValueGroup.class}, value = 2592000, message = "Value timeout must be between 1 and 2592000 seconds (30 days).")
    private Long timeout;

    /**
     * Namespace or field prefix of the key. Constraints:
     * 1. not required.
     * 2. If not provided, the default is no prefix.
     * 3. If provided, must be less than 64 characters.
     */
    @Size(groups = {RedisGetValueGroup.class, RedisSetValueGroup.class}, max = 64, message = "Namespace or field prefix must be less than 64 characters.")
    private String prefix;
}
