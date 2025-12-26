package top.gaogle.base.service;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.PullStatus;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.rocketmq.config.RocketMQConfigProperties;

import java.util.List;
import java.util.Set;

@Service
public class TestService extends SuperService {

    private final RocketMQTemplate rocketMQTemplate;

    private final RocketMQConfigProperties prop;

    public TestService(RocketMQTemplate rocketMQTemplate, RocketMQConfigProperties prop) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.prop = prop;
    }

    public void test() {

        // 同步发送
        rocketMQTemplate.syncSend(prop.getTopic().get("common"), "syncSend");
        // 异步发送
        rocketMQTemplate.asyncSend(prop.getTopic().get("common"), "asyncSend", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("Message sent successfully! MessageId: " + sendResult.getMsgId());
            }

            @Override
            public void onException(Throwable e) {
                log.error("Failed to send message: " + e.getMessage(), e);
            }
        });
        // 单程发送
        rocketMQTemplate.sendOneWay(prop.getTopic().get("common"), "sendOneWay");
    }

    public void testPull() throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer(prop.getConsumer().getGroup().get("pull"));
        consumer.setNamesrvAddr(prop.getNameServer());
        consumer.start();
        Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues(prop.getTopic().get("pull"));
        for(MessageQueue queue : messageQueues){
            // 从偏移量0开始拉取所有的数据
            long offset = 0;
            while(true){
                PullResult pullResult = consumer.pull(queue, "*", offset, 1);
                offset = pullResult.getNextBeginOffset();
                // 在队列中拉取不到消息就结束
                if(PullStatus.FOUND != pullResult.getPullStatus()) {
                    break;
                }
                List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                for (MessageExt m : messageExtList) {
                    log.info("当前队列为："+queue.getQueueId()+"，偏移量为："+offset+
                            "，拉取到数据："+ new String(m.getBody())+"，投递的时间为：" );
                }
            }
        }
        consumer.shutdown();
    }
}
