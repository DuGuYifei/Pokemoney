package com.pokemoney.redisservice.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

/**
 * This class is the entity which is used as object in test.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {
    /**
     * Test field 1.
     */
    private String testField1;
    /**
     * Test field 2.
     */
    private Integer testField2;
}
