package top.gaogle.framework.security.interceptor;


import org.apache.commons.lang3.StringUtils;
import top.gaogle.framework.commons.common.CommonsConst;
import top.gaogle.framework.commons.common.SecurityConstants;
import top.gaogle.framework.commons.util.HttpHelper;
import top.gaogle.framework.commons.util.JsonUtil;
import top.gaogle.framework.commons.util.SpringUtil;
import top.gaogle.framework.redis.service.StringRedisService;
import top.gaogle.framework.security.annotation.RepeatSubmit;
import top.gaogle.framework.security.wrapper.RepeatedlyRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 判断请求url和数据是否和上一次相同，
 * 如果和上次相同，则是重复提交表单。 有效时间为10秒内。
 *
 * @author gaogle
 * @since 1.0.0
 */
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {

    public static final String REPEAT_PARAMS = "repeatParams";

    public static final String REPEAT_TIME = "repeatTime";


    private final StringRedisService cacheUtil = SpringUtil.getBean(StringRedisService.class);


    @SuppressWarnings("unchecked")
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) {
        String nowParams = "";
        if (request instanceof RepeatedlyRequestWrapper) {
            RepeatedlyRequestWrapper repeatedlyRequest = (RepeatedlyRequestWrapper) request;
            nowParams = HttpHelper.getBodyString(repeatedlyRequest);
        }

        // body参数为空，获取Parameter的数据
        if (StringUtils.isEmpty(nowParams)) {
            nowParams = JsonUtil.object2JsonByJackson(request.getParameterMap());
        }
        Map<String, Object> nowDataMap = new HashMap<>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();

        // 唯一值（没有消息头则使用请求地址）
        String submitKey = StringUtils.trimToEmpty(request.getHeader(SecurityConstants.AUTHORIZATION_HEADER));

        // 唯一标识（指定key + url + 消息头）
        String cacheRepeatKey = CommonsConst.REPEAT_SUBMIT_KEY + url + submitKey;
        // todo gaoge 非原子操作 存在极限问题，后续考虑优化
        String sessionStr = cacheUtil.getCacheObject(cacheRepeatKey);
        if (StringUtils.isNotEmpty(sessionStr)) {
            Map<String, Object> sessionMap = JsonUtil.json2ObjectByJackson(sessionStr, Map.class);
            if (sessionMap != null && (sessionMap.containsKey(url))) {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
                if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, annotation.interval())) {
                    return true;
                }
            }
        }

        Map<String, Object> cacheMap = new HashMap<>();
        cacheMap.put(url, nowDataMap);
        cacheUtil.setCacheObject(cacheRepeatKey, JsonUtil.object2JsonByJackson(cacheMap), (long) annotation.interval(), TimeUnit.MILLISECONDS);
        return false;
    }

    /**
     * 判断参数是否相同
     */
    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        return (time1 - time2) < interval;
    }
}
