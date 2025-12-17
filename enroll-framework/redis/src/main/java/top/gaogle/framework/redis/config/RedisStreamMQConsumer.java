package top.gaogle.framework.redis.config;

import org.springframework.data.redis.connection.stream.MapRecord;

public interface RedisStreamMQConsumer {

    /**
     * @param message 消息对象
     */
    void dealMsg(MapRecord<String, String, String> message);

    void fallBack(MapRecord<String, String, String> message);


}
