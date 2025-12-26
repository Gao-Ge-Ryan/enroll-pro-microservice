package top.gaogle.base.listener;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author gaogle
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "${rocketmq.topic.order}",
        consumerGroup = "${rocketmq.consumer.group.order}",
        consumeMode = ConsumeMode.ORDERLY)
public class OrderMessageListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("Received message: " + message);
    }
}
