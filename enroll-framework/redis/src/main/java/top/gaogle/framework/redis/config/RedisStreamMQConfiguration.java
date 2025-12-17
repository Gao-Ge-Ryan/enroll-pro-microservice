package top.gaogle.framework.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import top.gaogle.framework.redis.pojo.RedisMq;
import top.gaogle.framework.redis.service.RedisService;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * stream配置绑定关系
 *
 * @author gaogle
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(value = "redis.stream.mq.enable", havingValue = "true")
public class RedisStreamMQConfiguration {
    private static final Logger log = LoggerFactory.getLogger(RedisStreamMQConfiguration.class);
    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    @Resource
    private BasicAckStreamMQConsumeListener basicAckStreamMQConsumeListener;
    @Resource
    private RedisService redisStreamUtil;
    @Resource
    private RedisStreamMQProperties redisStreamMQProperties;

    private static final int BATCHSIZE = 5;     // 一次最多获取多少条消息
    private static final long POLL_TIMEOUT = 3;  // Stream 中没有消息时，阻塞多长时间，需要比 `spring.redis.timeout` 的时间小

    @Bean(initMethod = "start", destroyMethod = "stop")
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer() {
        //创建线程池
        AtomicInteger index = new AtomicInteger(1);
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                processors,
                processors,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), // 限制队列大小
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("async-stream-consumer-" + index.getAndIncrement());
                    thread.setDaemon(true);
                    return thread;
                }
        );

        /*StreamMessage监听器容器配置
          streamMessageListenerContainer中的 .batchSize(1) 设置需要着重说一下。
          意思是在消费者在监听到数据的时候，一次从redis中取出的多少条数据，假设我设置1，
          就意味着我的监听器会redis中取出1条未消费的数据，随后进入消费者逻辑，处理完毕之后返回；
          继续由监听器读取1条数据，在进入消费者逻辑；这个值设置得越小消息处理数据越快，但是也会增加redis链接的资源。
          较大的 batchSize 可以减少与 Redis 服务器的交互次数，降低网络通信开销，提高处理效率。
          较小的 batchSize 适用于需要低延迟处理的场景，但会增加网络通信开销和 CPU 使用率。
         */
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .batchSize(BATCHSIZE)
                        .executor(executor)      // 运行 Stream 的 poll task
                        .pollTimeout(Duration.ofSeconds(POLL_TIMEOUT))
                        .errorHandler(throwable -> log.error("出现异常就来这里了{}", String.valueOf(throwable))) // 获取消息的过程或获取到消息给具体的消息者处理的过程中，发生了异常的处理
                        .build();
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer =
                StreamMessageListenerContainer.create(redisConnectionFactory, options);
        streamMessageListenerContainer.start();
        return streamMessageListenerContainer;
    }


    // 创建不同的订阅者
    @Bean
    public List<Subscription> createSubscription(StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer) {
        // 创建不同的订阅者
        List<RedisMq> configs = Objects.requireNonNull(redisStreamMQProperties.getConfigs(), "config error: config is null");
        List<Subscription> subscriptions = new ArrayList<>();
        for (RedisMq config : configs) {
            String streamName = config.getStreamName();
            String groupName = config.getGroupName();
            String consumerName = config.getConsumerName();
            initStreamAndGroup(streamName, groupName);
            //配置消费组
            StreamMessageListenerContainer.ConsumerStreamReadRequest<String> build = StreamMessageListenerContainer
                    .StreamReadRequest
                    .builder(StreamOffset.create(streamName, ReadOffset.lastConsumed()))
                    .consumer(Consumer.from(groupName, consumerName))
                    .autoAcknowledge(false)
                    // 重要！
                    .cancelOnError(t -> false).build();
            Subscription subscription = listenerContainer.register(build, basicAckStreamMQConsumeListener);
            subscriptions.add(subscription);
        }
        return subscriptions;
    }

    //初始化stream和消费者组
    private void initStreamAndGroup(String streamKey, String groupName) {
        try {
            // 1. 如果 Stream 不存在，直接创建 Stream 和 Group
            if (!redisStreamUtil.hasKey(streamKey)) {
                redisStreamUtil.createGroup(streamKey, groupName);
                log.info("Stream {} and group {} initialized", streamKey, groupName);
                return;
            }

            // 2. Stream 存在但 Group 可能不存在，精确检查
            if (!isGroupExists(streamKey, groupName)) {
                redisStreamUtil.createGroup(streamKey, groupName);
                log.info("Group {} initialized for existing stream {}", groupName, streamKey);
            }
        } catch (Exception e) {
            log.error("初始化Stream/Group失败", e);
            throw new IllegalStateException(e);
        }
    }

    private boolean isGroupExists(String streamKey, String groupName) {
        try {
            // 使用 RedisTemplate 或 Lettuce 的简化实现
            return Optional.ofNullable(redisStreamUtil.queryGroups(streamKey))
                    .map(groups -> groups.stream().anyMatch(g -> groupName.equals(g.groupName())))
                    .orElse(false);
        } catch (Exception e) {
            log.error("Failed to check group existence", e);
            return false;
        }
    }

}
