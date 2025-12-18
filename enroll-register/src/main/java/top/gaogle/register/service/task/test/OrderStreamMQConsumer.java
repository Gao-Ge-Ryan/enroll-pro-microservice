package top.gaogle.register.service.task.test;

import org.springframework.data.redis.connection.stream.MapRecord;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.redis.annotation.RedisStreamMQ;
import top.gaogle.framework.redis.config.RedisStreamMQConsumer;

@RedisStreamMQ(streamName = "orderStream2323",groupName = "orderGroup")

public class OrderStreamMQConsumer extends SuperService implements RedisStreamMQConsumer {

    @Override
    public void dealMsg(MapRecord<String, String, String> message) {
        log.info("开始消费,当前线程{}=============",Thread.currentThread().getName());
    }

    @Override
    public void fallBack(MapRecord<String, String, String> message) {

    }
}
