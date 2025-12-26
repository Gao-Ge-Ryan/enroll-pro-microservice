package top.gaogle.base.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.gaogle.framework.rocketmq.config.RocketMQConfigProperties;

/**
 * @author gaggle
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "",
        consumerGroup = "${rocketmq.consumer.group.batch}",
        consumeMode = ConsumeMode.CONCURRENTLY,
        messageModel = MessageModel.CLUSTERING)
public class BatchPushMessageListener implements RocketMQListener<Message>, RocketMQPushConsumerLifecycleListener {
    @Autowired
    private RocketMQConfigProperties prop;
    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        try {
            // 订阅主题
            defaultMQPushConsumer.subscribe(prop.getTopic().get("batch"), "*");
            // 设置开始消费位置
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    @Override
    public void onMessage(Message message) {
        log.info("收到消息: " + new String(message.getBody()));
    }
}
