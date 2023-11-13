package com.pokemoney.leaf.service.triple;

import com.pokemoney.commons.proto.Response;
import com.pokemoney.leaf.service.api.LeafGetRequestDto;
import com.pokemoney.leaf.service.api.LeafTriService;
import com.pokemoney.leaf.service.api.LeafResponseDto;
import com.pokemoney.leaf.common.Result;
import com.pokemoney.leaf.common.Status;
import com.pokemoney.leaf.service.exception.NoKeyException;
import com.pokemoney.leaf.service.service.SegmentService;
import com.pokemoney.leaf.service.service.SnowflakeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Leaf service triple protocol endpoint.
 */
@DubboService(version = "1.0.0", protocol = "tri", group = "leaf", timeout = 10000)
public class LeafTriServiceImpl implements LeafTriService {
    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger(LeafTriServiceImpl.class);

    /**
     * Segment service.
     */
    private final SegmentService segmentService;

    /**
     * Snowflake service.
     */
    private final SnowflakeService snowflakeService;

    /**
     * Constructor.
     *
     * @param segmentService   segment service
     * @param snowflakeService snowflake service
     */
    public LeafTriServiceImpl(SegmentService segmentService, SnowflakeService snowflakeService) {
        this.segmentService = segmentService;
        this.snowflakeService = snowflakeService;
    }

    /**
     * Get segment id.
     *
     * @param request {@link LeafGetRequestDto}
     * @return {@link Response}
     */
    @Override
    public LeafResponseDto getSegmentId(LeafGetRequestDto request) {
        String key = request.getKey();
        String res = get(key, segmentService.getId(key));
        return LeafResponseDto.newBuilder().setId(res).build();
    }

    /**
     * Get snowflake id.
     *
     * @param request {@link LeafGetRequestDto}
     * @return {@link Response}
     */
    @Override
    public LeafResponseDto getSnowflakeId(LeafGetRequestDto request) {
        String key = request.getKey();
        String res = get(key, snowflakeService.getId(key));
        return LeafResponseDto.newBuilder().setId(res).build();
    }

    /**
     * Get id.
     *
     * @param key {@link String}
     * @param id  {@link Result}
     * @return {@link Response}
     */
    private String get(@PathVariable("key") String key, Result id) {
        Result result;
        if (key == null || key.isEmpty()) {
            throw new NoKeyException();
        }
        result = id;
        if (result.getStatus().equals(Status.EXCEPTION)) {
            logger.error(result.toString());
            throw new RpcException(RpcException.BIZ_EXCEPTION, result.toString());
        }
        return String.valueOf(result.getId());
    }
}
