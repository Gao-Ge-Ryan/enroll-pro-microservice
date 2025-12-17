package top.gaogle.framework.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.PendingMessagesSummary;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Component;
import top.gaogle.framework.commons.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * spring redis 工具类
 *
 * @author gaogle
 * @since 1.0.0
 */
@Component
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 判断key是否存在
     *
     * @param key stream名称
     * @return Boolean
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 新增消息
     *
     * @param stream stream名称
     * @param value  map数据类型
     *               return            返回 消息id
     */
    public String addStreamMsg(String stream, Map<String, Object> value) {
        return Objects.requireNonNull(redisTemplate.opsForStream().add(stream, value)).getValue();
    }

    /**
     * 读取消息
     *
     * @param stream stream名称
     */
    @SuppressWarnings("unchecked")
    public List<MapRecord<String, Object, Object>> read(String stream) {
        return redisTemplate.opsForStream().read(StreamOffset.fromStart(stream));
    }


    /**
     * 删除消息
     *
     * @param stream stream名称
     * @param msgId  消息id
     */
    public void deleteStreamMsg(String stream, String msgId) {
        redisTemplate.opsForStream().delete(stream, msgId);
    }

    /**
     * 创建消费者组用于消费stream
     *
     * @param stream stream名称
     * @param group  组名称
     */
    public void createGroup(String stream, String group) {
        redisTemplate.opsForStream().createGroup(stream, group);
    }


    /**
     * 查询消费者组
     *
     * @param stream stream名称
     */
    public StreamInfo.XInfoGroups queryGroups(String stream) {
        return redisTemplate.opsForStream().groups(stream);
    }

    /**
     * 查询消费者组下面的所有消费者
     *
     * @param stream stream名称
     * @param group  组名称
     * @return {@link StreamInfo.XInfoConsumers}
     */
    public StreamInfo.XInfoConsumers queryConsumers(String stream, String group) {
        return redisTemplate.opsForStream().consumers(stream, group);
    }

    /**
     * 消费者组确认消息
     *
     * @param stream stream名称
     * @param group  组名称
     */
    public Long ack(String stream, String group, String... recordIds) {
        return redisTemplate.opsForStream().acknowledge(stream, group, recordIds);
    }

    /**
     * 获取 Pending 中的摘要
     *
     * @param stream stream名称
     * @param group  组名称
     */
    public PendingMessagesSummary getPendingSummary(String stream, String group) {
        StreamOperations<String, Object, Object> streamOps = redisTemplate.opsForStream();
        // 获取 pending list 中未确认的消息概要
        return streamOps.pending(stream, group);
    }

    /**
     * 获取pending list集合(消费出现异常 没有ack时)
     *
     * @param stream stream名称
     * @param group  组名称
     */
    public List<MapRecord<String, Object, Object>> getPendingList(String stream, String group) {
        StreamOperations<String, Object, Object> streamOps = redisTemplate.opsForStream();

        // 获取 pending list 中未确认的消息概要
        PendingMessagesSummary pendingSummary = streamOps.pending(stream, group);
        if (pendingSummary == null) {
            return Collections.emptyList();
        }

        // 所有pending消息的数量
        long totalPendingMessages = pendingSummary.getTotalPendingMessages();
        if (totalPendingMessages == 0L) {
            return Collections.emptyList();
        }

        // 消费组名称
        String groupName = pendingSummary.getGroupName();
        // pending队列中的最小ID
        String minMessageId = pendingSummary.minMessageId();
        // pending队列中的最大ID
        String maxMessageId = pendingSummary.maxMessageId();

        // 获取 pending list 中具体的消息
        return streamOps.range(stream, Range.closed(minMessageId, maxMessageId), RedisZSetCommands.Limit.limit().count(10));
    }

    /**
     * 获取pending list中某个元素(消费出现异常 没有ack时)
     *
     * @param stream stream名称
     * @param msgId  消息id
     */
    public List<MapRecord<String, Object, Object>> getPendingByMsgId(String stream, String msgId) {
        StreamOperations<String, Object, Object> streamOps = redisTemplate.opsForStream();
        if (StringUtil.isEmpty(msgId)) {
            return Collections.emptyList();
        }
        return streamOps.range(stream, Range.closed(msgId, msgId), RedisZSetCommands.Limit.unlimited());
    }

}
