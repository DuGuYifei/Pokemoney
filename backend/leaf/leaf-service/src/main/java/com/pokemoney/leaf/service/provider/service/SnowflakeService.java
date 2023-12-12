package com.pokemoney.leaf.service.provider.service;

import com.pokemoney.leaf.IDGen;
import com.pokemoney.leaf.common.Result;
import com.pokemoney.leaf.common.ZeroIDGen;
import com.pokemoney.leaf.service.provider.Constants;
import com.pokemoney.leaf.service.provider.exception.InitException;
import com.pokemoney.leaf.snowflake.SnowflakeIDGenImpl;
import com.pokemoney.leaf.common.PropertyFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service("SnowflakeService")
public class SnowflakeService {
    private Logger logger = LogManager.getLogger(SnowflakeService.class);

    private IDGen idGen;

    public SnowflakeService() throws InitException {
        Properties properties = PropertyFactory.getProperties();
        boolean flag = Boolean.parseBoolean(properties.getProperty(Constants.LEAF_SNOWFLAKE_ENABLE, "true"));
        if (flag) {
            String zkAddress = properties.getProperty(Constants.LEAF_SNOWFLAKE_ZK_ADDRESS);
            int port = Integer.parseInt(properties.getProperty(Constants.LEAF_SNOWFLAKE_PORT));
            int zkConnectTimeout = Integer.parseInt(properties.getProperty(Constants.LEAF_SNOWFLAKE_ZK_TIMEOUT));
            idGen = new SnowflakeIDGenImpl(zkAddress, port, zkConnectTimeout);
            if(idGen.init()) {
                logger.info("Snowflake Service Init Successfully");
            } else {
                throw new InitException("Snowflake Service Init Fail");
            }
        } else {
            idGen = new ZeroIDGen();
            logger.info("Zero ID Gen Service Init Successfully");
        }
    }

    public Result getId(String key) {
        return idGen.get(key);
    }
}
