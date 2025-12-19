//package top.gaogle.framework.redis.config;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//
//@Configuration
//@AutoConfigureBefore({
//        RedisAutoConfiguration.class
//})
//public class RedisConfig {
//    private static final RedisSerializer<Object> SERIALIZER = createSerializer();
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//        // 创建 RedisTemplate 对象
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        // 设置 RedisConnection 工厂。😈 它就是实现多种 Java Redis 客户端接入的秘密工厂。感兴趣的胖友，可以自己去撸下。
//        template.setConnectionFactory(factory);
//        // 使用 String 序列化方式，序列化 KEY 。
//        template.setKeySerializer(RedisSerializer.string());
//        template.setHashKeySerializer(RedisSerializer.string());
//        // 使用 JSON 序列化方式（库是 Jackson ），序列化 VALUE 。
//        template.setValueSerializer(SERIALIZER);
//        template.setHashValueSerializer(SERIALIZER);
//        return template;
//    }
//
//    private static RedisSerializer<Object> createSerializer() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModules(new JavaTimeModule());
//        // 此项必须配置，否则会报java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
//        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
//        return new GenericJackson2JsonRedisSerializer(mapper);
//    }
//
//}
