package top.gaogle.register.service.task.test;

import org.springframework.data.redis.connection.stream.MapRecord;
import top.gaogle.framework.redis.annotation.RedisStream;
import top.gaogle.framework.redis.config.RedisConsumer;

@RedisStream(streamName = "goodsStream",groupName = "goodsGroup")

public class GoodsConsumer implements RedisConsumer {

    @Override
    public void dealMsg(MapRecord<String, String, String> message) {
//        log.info("开始消费,当前线程{}=============",Thread.currentThread().getName());
    }

    @Override
    public void fallBack(MapRecord<String, String, String> message) {

    }
}
