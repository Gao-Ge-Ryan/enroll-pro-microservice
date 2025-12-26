package top.gaogle.base.listener;


import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Component;
import top.gaogle.framework.commons.service.SuperService;

/**
 * @author gaogle
 */

@Component
@RocketMQMessageListener(
        topic = "${rocketmq.topic.rpc}",
        consumerGroup = "${rocketmq.consumer.group.rpc}")
public class RpcMessageListener extends SuperService implements RocketMQReplyListener<String, String> {
    @Override
    public String onMessage(String message) {
        log.info("rpc listener receive：" + message);
        return "reply string";
    }
}
