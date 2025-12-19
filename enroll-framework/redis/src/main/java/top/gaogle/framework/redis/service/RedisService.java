//package top.gaogle.framework.redis.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Range;
//import org.springframework.data.redis.connection.RedisZSetCommands;
//import org.springframework.data.redis.connection.stream.MapRecord;
//import org.springframework.data.redis.connection.stream.PendingMessagesSummary;
//import org.springframework.data.redis.connection.stream.StreamInfo;
//import org.springframework.data.redis.connection.stream.StreamOffset;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StreamOperations;
//import org.springframework.stereotype.Component;
//import top.gaogle.framework.commons.util.StringUtil;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * spring redis 工具类
// *
// * @author gaogle
// * @since 1.0.0
// */
//@Component
//public class RedisService {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    @Autowired
//    public RedisService(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
////
////    /**
////     * 判断key是否存在
////     *
////     * @param key stream名称
////     * @return Boolean
////     */
////    public Boolean hasKey(String key) {
////        return redisTemplate.hasKey(key);
////    }
//
////    /**
////     * 新增消息
////     *
////     * @param stream stream名称
////     * @param value  map数据类型
////     *               return            返回 消息id
////     */
////    public String addStreamMsg(String stream, Map<String, Object> value) {
////        return Objects.requireNonNull(redisTemplate.opsForStream().add(stream, value)).getValue();
////    }
//
//
//}
