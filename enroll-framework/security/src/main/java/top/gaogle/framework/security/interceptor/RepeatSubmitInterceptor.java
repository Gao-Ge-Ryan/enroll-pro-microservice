package top.gaogle.framework.security.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.gaogle.framework.commons.enums.HttpStatusEnum;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.commons.util.JsonUtil;
import top.gaogle.framework.security.annotation.RepeatSubmit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 防止重复提交拦截器
 *
 * @author gaogle
 */
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request, annotation)) {
                    response.setStatus(HttpStatusEnum.BAD_REQUEST.getValue());
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    I18nResult<String> result = I18nResult.newInstance();
                    response.getWriter().print(JsonUtil.object2JsonByJackson(result.failedBadRequest().setMessage(annotation.message())));
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param request    请求信息
     * @param annotation 防重复注解参数
     * @return 结果
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation);
}
