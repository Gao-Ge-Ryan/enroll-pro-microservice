package top.gaogle.framework.log.aspect;


import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import top.gaogle.framework.commons.util.*;
import top.gaogle.framework.log.annotation.Log;
import top.gaogle.framework.log.enums.BusinessStatusEnum;
import top.gaogle.framework.log.pojo.OperateLog;
import top.gaogle.framework.redis.service.RedisService;
import top.gaogle.framework.security.util.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 操作日志记录处理
 *
 * @author gaogle
 * @since 1.0.0
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    private final RedisService redisService;


    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    /**
     * 参数最大长度限制
     */
    private static final int PARAM_MAX_LENGTH = 15000;

    /**
     * 异常最大长度限制
     */
    private static final int EXCEPTION_MAX_LENGTH = 5000;
    public static final String OPERATE_LOG_KEY = "OPERATE_LOG";

    @Autowired
    public LogAspect(RedisService redisService) {
        this.redisService = redisService;
    }


    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void doBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(DateUtil.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        OperateLog operLog = new OperateLog();
        try {
            // *========数据库日志=========*//
            operLog.setStatus(BusinessStatusEnum.SUCCESS);
            // 请求的地址
            String ip = IpUtil.getIpAddr();
            operLog.setOperateIp(ip);
            operLog.setOperateUrl(StringUtil.substring(Objects.requireNonNull(ServletUtil.getRequest()).getRequestURI(), 0, 1000));
            String username = SecurityUtil.getUsername();
            String userId = SecurityUtil.getUserId();
            if (StringUtil.isNotBlank(username)) {
                operLog.setOperator(username);
                operLog.setOperatorUserId(userId);
            }

            if (e != null) {
                operLog.setStatus(BusinessStatusEnum.FAIL);
                operLog.setErrorMsg(StringUtil.substring(e.getMessage(), 0, EXCEPTION_MAX_LENGTH));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(ServletUtil.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            // 设置消耗时间
            operLog.setCostTime(DateUtil.currentTimeMillis() - TIME_THREADLOCAL.get());
            operLog.setCreateAt(DateUtil.currentTimeMillis());
            // 保存(redis生产-消费模式)
            Map<String, Object> map = new HashMap<>();
            map.put(OPERATE_LOG_KEY, JsonUtil.object2Json(operLog));
            redisService.addStreamMsg(StringUtil.joinWithColon(OPERATE_LOG_KEY), map);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("LogAspect异常信息operLog:{},joinPoint:{}", JsonUtil.object2Json(operLog), JsonUtil.object2Json(joinPoint), exp);
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, OperateLog operLog, Object jsonResult) {
        // 设置action动作
        operLog.setBusinessType(log.businessType());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置操作人类别
        operLog.setOperateType(log.operatorType());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtil.isNotNull(jsonResult)) {
            operLog.setJsonResult(StringUtil.substring(JsonUtil.object2Json(jsonResult), 0, PARAM_MAX_LENGTH));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     */
    private void setRequestValue(JoinPoint joinPoint, OperateLog operLog, String[] excludeParamNames) {
        String requestMethod = operLog.getRequestMethod();
        Map<?, ?> paramsMap = ServletUtil.getParamMap(ServletUtil.getRequest());
        if (StringUtil.isEmpty(paramsMap) && StringUtil.equalsAny(requestMethod, HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name())) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperateParam(StringUtil.substring(params, 0, PARAM_MAX_LENGTH));
        } else {
            operLog.setOperateParam(StringUtil.substring(JsonUtil.object2Json(paramsMap, excludeParamNames), 0, PARAM_MAX_LENGTH));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object o : paramsArray) {
                if (StringUtil.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JsonUtil.object2Json(o, excludeParamNames);
                        params.append(jsonObj).append(" ");
                    } catch (Exception e) {
                        log.error("请求参数拼装异常 msg:{}, 参数:{}", e.getMessage(), paramsArray, e);
                    }
                }
            }
        }
        return params.toString();
    }


    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
