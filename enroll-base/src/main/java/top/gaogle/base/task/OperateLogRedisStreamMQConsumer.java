package top.gaogle.base.task;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import top.gaogle.base.dao.master.OperateLogMapper;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.commons.util.JsonUtil;
import top.gaogle.framework.commons.util.StringUtil;
import top.gaogle.framework.log.pojo.OperateLog;
import top.gaogle.framework.redis.annotation.RedisStreamMQ;
import top.gaogle.framework.redis.config.RedisStreamMQConsumer;
import top.gaogle.framework.redis.service.StringRedisService;

import java.util.Map;

import static top.gaogle.framework.log.aspect.LogAspect.OPERATE_LOG_KEY;

/**
 * 操作日志消费者
 *
 * @author gaogle
 * @since 1.0.0
 */
@RedisStreamMQ(streamName = "${redis.stream.mq.configs.operateLog.streamName}")
public class OperateLogRedisStreamMQConsumer extends SuperService implements RedisStreamMQConsumer {
    private final StringRedisService redisService;
    private final OperateLogMapper operateLogMapper;

    public OperateLogRedisStreamMQConsumer(StringRedisService redisService, OperateLogMapper operateLogMapper) {
        this.redisService = redisService;
        this.operateLogMapper = operateLogMapper;
    }

    @Override
    public void dealMsg(MapRecord<String, String, String> message) {
        //stream的key值
        String streamName = message.getStream();
        //消息ID
        RecordId recordId = message.getId();
        //消息内容
        Map<String, String> msg = message.getValue();
        String operateLogJson = msg.get(OPERATE_LOG_KEY);
        if (StringUtil.isNotBlank(operateLogJson)) {
            OperateLog operateLog = JsonUtil.json2ObjectByGson(operateLogJson, OperateLog.class);
            operateLogMapper.insert(operateLog);
        }
        redisService.deleteStreamMsg(streamName, recordId.getValue());
    }

    @Override
    public void fallBack(MapRecord<String, String, String> message) {
        //stream的key值
        String streamName = message.getStream();
        //消息ID
        RecordId recordId = message.getId();
        //消息内容
        Map<String, String> msg = message.getValue();
        log.error("OperateLogRedisStreamMQConsumer消费失败,message:{}", JsonUtil.object2JsonByJackson(message));
        redisService.deleteStreamMsg(streamName, recordId.getValue());
    }
}
