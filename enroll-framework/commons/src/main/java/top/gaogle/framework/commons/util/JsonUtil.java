package top.gaogle.framework.commons.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gaogle.framework.commons.common.CommonsConst;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Jackson常用方式封装
 *
 * @author gaogle
 * @since 1.0.0
 */
public class JsonUtil {
    private final static Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private JsonUtil() {
        throw new IllegalStateException(CommonsConst.PROHIBIT_INSTANTIATION);
    }


    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Gson gson = new Gson();

    /**
     * 创建ArrayNode新实例
     *
     * @return ArrayNode
     */
    public static ArrayNode createArrayNode() {
        JsonNodeFactory.instance.arrayNode();
        return objectMapper.createArrayNode();
    }

    /**
     * 创建ObjectNode新实例
     *
     * @return ObjectNode
     */
    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    /**
     * 将字符串转成ArrayNode对象
     *
     * @param jsonString 输入字符串
     * @return ArrayNode
     */
    public static ArrayNode parseArrayNode(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, ArrayNode.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectMapper.createArrayNode();
    }

    /**
     * 将字符串转成ObjectNode对象
     *
     * @param jsonString 字符串
     * @return ObjectNode
     */
    public static ObjectNode parseObjectNode(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, ObjectNode.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectMapper.createObjectNode();
    }

    /**
     * 将对象转成ArrayNode
     * 输入对象 fromValue值建议为map,list,set等，
     * 不建议使用字符串,传入字符串会转成TextNode而导致强转失败,字符串转化可以使用{@link JsonUtil#parseArrayNode(String)}
     *
     * @param fromValue 输入对象
     * @return ArrayNode
     */
    public static ArrayNode convertValueToArrayNode(Object fromValue) {
        return objectMapper.convertValue(fromValue, ArrayNode.class);
    }

    /**
     * 将对象转成JsonNode
     * 输入对象 fromValue值建议为map,list,set等，
     * 不建议使用字符串,传入字符串会转成TextNode而导致强转失败,
     * 字符串转化可以使用{@link JsonUtil#parseArrayNode(String)}
     *
     * @param fromValue 输入对象
     * @return JsonNode
     */
    public static JsonNode valueToTree(Object fromValue) {
        return objectMapper.valueToTree(fromValue);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return objectMapper.convertValue(fromValue, toValueTypeRef);
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValue) {
        return objectMapper.convertValue(fromValue, toValue);
    }


    public static String object2JsonByJackson(Object fromValue) {
        String jsonDate = "";
        try {
            jsonDate = objectMapper.writeValueAsString(fromValue);
        } catch (Exception e) {

        }
        return jsonDate;
    }

    /**
     * 将对象转为 JSON 字符串，并排除指定的字段
     */
    public static String object2JsonByJackson(Object fromValue, String[] excludeParamNames) {
        if (fromValue == null) {
            return "";
        }
        try {
            // 先转成 JsonNode（树模型）
            JsonNode jsonNode = objectMapper.valueToTree(fromValue);
            // 如果是对象类型，才进行字段排除
            if (jsonNode.isObject()) {
                ObjectNode objectNode = (ObjectNode) jsonNode;
                Set<String> excludes = excludeParamNames != null
                        ? new HashSet<>(Arrays.asList(excludeParamNames))
                        : new HashSet<>();
                // 删除敏感字段
                excludes.forEach(objectNode::remove);
            }
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            return "";
        }
    }

    public static String object2JsonByGson(Object fromValue) {
        return gson.toJson(fromValue);
    }

    public static <T> T json2ObjectByJackson(String json, Class<T> clazz) {
        if (json == null || clazz == null) {
            throw new IllegalArgumentException("JSON string and class type must not be null");
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            // 记录日志以便调试
            log.error("Failed to deserialize JSON string to object. JSON: {}", json, e);
            return null; // 或者抛出异常，根据实际需求选择
        }
    }

    public static <T> T json2ObjectByGson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
