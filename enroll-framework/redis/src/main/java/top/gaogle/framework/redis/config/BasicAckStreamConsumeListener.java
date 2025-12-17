package top.gaogle.framework.redis.config;

import org.springframework.aop.support.AopUtils;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import top.gaogle.framework.redis.annotation.RedisStream;
import top.gaogle.framework.redis.service.RedisService;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * 手动ack消费者
 *
 * @author gaogle
 * @since 1.0.0
 */
@Component
public class BasicAckStreamConsumeListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Resource
    private RedisService redisStreamUtil;
    @Resource
    private Map<String, RedisConsumer> redisConsumer;

    /**
     * 监听器
     */
    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        System.out.println("监听到消息messageId:{}" + message.getId());
        //stream的key值
        String streamName = message.getStream();
        //消息ID
        RecordId recordId = message.getId();
        //消息内容
        Map<String, String> msg = message.getValue();
        for (Map.Entry<String, RedisConsumer> redisConsumerEntry : redisConsumer.entrySet()) {
            // 获取目标类
            RedisConsumer redisConsumer = redisConsumerEntry.getValue();
            Class<?> targetClass = AopUtils.getTargetClass(redisConsumer);
            RedisStream redisStream = targetClass.getAnnotation(RedisStream.class);
            if (Objects.isNull(redisStream)) {
                continue;
            }
            if (!Objects.equals(streamName, redisStream.streamName())) {
                continue;
            }
            try {
                redisConsumer.dealMsg(message);
                //逻辑处理完成后，ack消息，删除消息，group为消费组名称
                redisStreamUtil.ack(streamName, redisStream.groupName(), recordId.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // 不能直接删除 需要业务层自己维护 等到所有消费组都消费完才删除 一般可以不考虑删除
//            redisStreamUtil.del(streamName, recordId.getValue());
        }
        System.out.println("【streamName】= " + streamName + ",【recordId】= " + recordId + ",【msg】=" + msg);
    }
}
