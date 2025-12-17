package top.gaogle.framework.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.gaogle.framework.redis.pojo.RedisMq;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "redis.stream.mq")
public class RedisStreamMQProperties {
    private List<RedisMq> configs = new ArrayList<>();

    public List<RedisMq> getConfigs() {
        return configs;
    }

    public void setConfigs(List<RedisMq> configs) {
        this.configs = configs;
    }
}
