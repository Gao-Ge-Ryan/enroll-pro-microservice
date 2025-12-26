package top.gaogle.base.listener;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.rocketmq.config.RocketMQConfigProperties;

/**
 * @author gaogle
 */
@Component
@RocketMQMessageListener(
        topic = "",
        consumerGroup = "${rocketmq.consumer.group.common}",
        consumeMode = ConsumeMode.CONCURRENTLY,
        messageModel = MessageModel.CLUSTERING)
public class PushMessageListener extends SuperService implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Autowired
    private RocketMQConfigProperties prop;

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        try {
            // 订阅主题
            defaultMQPushConsumer.subscribe(prop.getTopic().get("common"), "*");
            // 设置开始消费位置
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onMessage(String message) {
        log.info("Received message: " + message);
    }
}
