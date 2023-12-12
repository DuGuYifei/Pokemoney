package com.pokemoney.leaf.snowflake;

import com.pokemoney.leaf.common.Result;
import com.pokemoney.leaf.IDGen;
import com.pokemoney.leaf.common.PropertyFactory;
import org.junit.Test;

import java.util.Properties;

public class SnowflakeIDGenImplTest {
    @Test
    public void testGetId() {
        Properties properties = PropertyFactory.getProperties();

        IDGen idGen = new SnowflakeIDGenImpl(properties.getProperty("leaf.zk.list"), 8080);
        for (int i = 1; i < 1000; ++i) {
            Result r = idGen.get("a");
            System.out.println(r);
        }
    }
}
