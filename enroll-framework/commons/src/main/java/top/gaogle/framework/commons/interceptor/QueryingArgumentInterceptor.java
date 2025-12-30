

package top.gaogle.framework.commons.interceptor;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import top.gaogle.framework.commons.pojo.QueryingArgument;
import top.gaogle.framework.commons.pojo.SuperQuerying;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * SuperQuerying通过代理取值
 *
 * @author gaogle
 * @since 2.0.0
 */
public class QueryingArgumentInterceptor implements MethodInterceptor {

    private static final Set<Method> QUERYING_METHOD = new HashSet<>();

    static {
        QUERYING_METHOD.addAll(Arrays.asList(SuperQuerying.class.getMethods()));
    }

    private final QueryingArgument querying;

    public QueryingArgumentInterceptor(QueryingArgument querying) {
        this.querying = querying;
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result;
        if (QUERYING_METHOD.contains(method)) {
            result = method.invoke(querying, args);
        } else {
            result = methodProxy.invokeSuper(target, args);
        }
        return result;
    }
}
