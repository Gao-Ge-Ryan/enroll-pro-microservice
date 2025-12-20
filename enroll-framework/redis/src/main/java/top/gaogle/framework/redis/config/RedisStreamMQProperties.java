package top.gaogle.framework.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.gaogle.framework.redis.pojo.RedisMq;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * redis stream mq 配置
 *
 * @author gaogle
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "redis.stream.mq")
public class RedisStreamMQProperties {
    private Boolean enable ;
    private Map<String,RedisMq> configs = new LinkedHashMap<>();

    public Map<String, RedisMq> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, RedisMq> configs) {
        this.configs = configs;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

}
