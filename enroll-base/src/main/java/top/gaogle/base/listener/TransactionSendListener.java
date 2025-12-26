package top.gaogle.base.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
public class TransactionSendListener implements RocketMQLocalTransactionListener {
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("本地事务成功");
        return RocketMQLocalTransactionState.COMMIT;
    }
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("事务检查成功");
        return RocketMQLocalTransactionState.COMMIT;
    }
}
