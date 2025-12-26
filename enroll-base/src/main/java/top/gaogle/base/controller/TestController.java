package top.gaogle.base.controller;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.gaogle.base.service.TestService;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.rocketmq.config.RocketMQConfigProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaogle
 * @since 1.0.0
 */
@RestController
@RequestMapping("/base/test")
public class TestController extends SuperService {

    private final TestService testService;
    private final RocketMQConfigProperties prop;
    private final RocketMQTemplate rocketMQTemplate;

    @Autowired
    public TestController(TestService testService, RocketMQConfigProperties prop, RocketMQTemplate rocketMQTemplate) {
        this.testService = testService;
        this.prop = prop;
        this.rocketMQTemplate = rocketMQTemplate;
    }
    @GetMapping
    public void test() {
        // push
        testService.test();

    }

    @GetMapping("/pull")
    public void testPull() throws Exception {
        // pull
        testService.testPull();

    }

    @GetMapping("/send")
    public String send() {
        // rpc发送
        String responseMessage = rocketMQTemplate.sendAndReceive(prop.getTopic().get("rpc"), "我是rpc message", String.class);
        log.info("rpc Listener发送结果："+responseMessage);
        return "ok";
    }

    @RequestMapping("/send/order")
    public String sendOrder() {
        // 顺序消息
        for(int i = 0; i < 100; i++) {
            rocketMQTemplate.syncSendOrderly(prop.getTopic().get("order"), "我是order-"+"-"+i, "商品ID");
        }
        return "ok";
    }

    @RequestMapping("/send/transaction")
    public String sendTransaction() {
        // 事务发送
        Message<String> message = MessageBuilder.withPayload("我是事务消息")
                .setHeader(RocketMQHeaders.KEYS, 1)
                .setHeader("orderID", 10)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, 100).build();
        rocketMQTemplate.sendMessageInTransaction(prop.getTopic().get("transaction"), message, null);
        return "ok";
    }

    @RequestMapping("/send/Delay")
    public String sendDelay() {
        rocketMQTemplate.syncSend(prop.getTopic().get("delay"),
                new GenericMessage<>("我是延迟消息" ),
                3000,
                3);
        return "ok";
    }

    @RequestMapping("/send/batch")
    public String sendBatch() {
        List<Message<String>> messages = new ArrayList<>();
        // 构建消息1
        Message<String> message1 = MessageBuilder.withPayload("Message 1").build();
        messages.add(message1);
        // 构建消息2
        Message<String> message2 = MessageBuilder.withPayload("Message 2").build();
        messages.add(message2);
        // 构建消息3
        Message<String> message3 = MessageBuilder.withPayload("Message 3").build();
        messages.add(message3);
        // 批量发送消息
        rocketMQTemplate.syncSend(prop.getTopic().get("batch"), messages);
        return "ok";
    }



}
