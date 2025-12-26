package top.gaogle.framework.rocketmq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQConfigProperties {

    private String nameServer;
    private Map<String, String> topic;
    private Consumer consumer;

    public static class Consumer {
        private Map<String, String> group;

        public Map<String, String> getGroup() {
            return group;
        }

        public void setGroup(Map<String, String> group) {
            this.group = group;
        }


    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public Map<String, String> getTopic() {
        return topic;
    }

    public void setTopic(HashMap<String, String> topic) {
        this.topic = topic;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}
