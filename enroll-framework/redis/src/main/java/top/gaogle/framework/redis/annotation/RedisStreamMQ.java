package top.gaogle.framework.redis.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import top.gaogle.framework.redis.config.BasicAckStreamMQConsumeListener;
import top.gaogle.framework.redis.config.RedisStreamMQConfiguration;
import top.gaogle.framework.redis.config.RedisStreamMQProperties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static top.gaogle.framework.redis.config.RedisStreamMQConsumer.DEFAULT_GROUP;

/**
 * stream消费者创建注解
 *
 * @author gaogle
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
@Import({RedisStreamMQConfiguration.class, BasicAckStreamMQConsumeListener.class, RedisStreamMQProperties.class})
public @interface RedisStreamMQ {

    /**
     * 消息主题
     * 不设计数组: 1. 接口应遵循单一职责 2.数组带来ack应答问题
     */
    String streamName();

    /**
     * 消费组
     * 不设计数组: 1. 接口应遵循单一职责 2.数组带来ack应答问题
     */
    String groupName() default DEFAULT_GROUP;

}
