package top.gaogle.base.service.task;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.redis.annotation.RedisStreamMQ;
import top.gaogle.framework.redis.config.RedisStreamMQConsumer;
import top.gaogle.framework.redis.service.RedisService;

import java.util.Map;

@RedisStreamMQ(streamName = "OPERATE_LOG", groupName = "OPERATE_LOG_GROUP")

public class OperateLogRedisStreamMQConsumer extends SuperService implements RedisStreamMQConsumer {
    private final RedisService redisService;

    public OperateLogRedisStreamMQConsumer(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void dealMsg(MapRecord<String, String, String> message) {

        //stream的key值
        String streamName = message.getStream();
        //消息ID
        RecordId recordId = message.getId();
        //消息内容
        Map<String, String> msg = message.getValue();
        log.info("开始消费,当前线程{}=============", Thread.currentThread().getName());


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
//        redisService.deleteStreamMsg(streamName, recordId.getValue());
    }
}
