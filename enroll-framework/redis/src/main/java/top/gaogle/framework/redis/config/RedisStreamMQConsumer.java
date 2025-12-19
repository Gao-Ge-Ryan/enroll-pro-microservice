package top.gaogle.framework.redis.config;

import org.springframework.data.redis.connection.stream.MapRecord;

public interface RedisStreamMQConsumer {

    String REDIS_STREAM_MQ_KEY = "REDIS_STREAM_MQ";
    String DEFAULT_GROUP = "DEFAULT_GROUP";
    String DEFAULT_GROUP_CONSUMER = "DEFAULT_GROUP_CONSUMER";

    /**
     * @param message 消息对象
     */
    void dealMsg(MapRecord<String, String, String> message);

    void fallBack(MapRecord<String, String, String> message);


}
