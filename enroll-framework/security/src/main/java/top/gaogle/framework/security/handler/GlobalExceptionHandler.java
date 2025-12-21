package top.gaogle.framework.security.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.gaogle.framework.commons.enums.HttpStatusEnum;
import top.gaogle.framework.commons.exception.RateLimiterException;
import top.gaogle.framework.commons.exception.ServiceException;
import top.gaogle.framework.commons.exception.auth.NotPermissionException;
import top.gaogle.framework.commons.exception.auth.NotRoleException;
import top.gaogle.framework.commons.i18n.I18nResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author gaogle
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public I18nResult<String> handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        I18nResult<String> result = I18nResult.newInstance();
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限码校验失败'{}'", requestURI, e.getMessage());
        return result.failed().setStatus(HttpStatusEnum.FORBIDDEN).setMessage("没有访问权限，请联系管理员授权");
    }

    /**
     * 角色权限异常
     */
    @ExceptionHandler(NotRoleException.class)
    public I18nResult<String> handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        I18nResult<String> result = I18nResult.newInstance();
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',角色权限校验失败'{}'", requestURI, e.getMessage());
        return result.failed().setStatus(HttpStatusEnum.FORBIDDEN).setMessage("没有访问权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public I18nResult<String> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        I18nResult<String> result = I18nResult.newInstance();
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return result.failed().setMessage(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public I18nResult<String> handleServiceException(ServiceException e, HttpServletRequest request) {
        I18nResult<String> result = I18nResult.newInstance();
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return result.failed().setMessage(e.getMessage());
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public I18nResult<String> handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        I18nResult<String> result = I18nResult.newInstance();
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI, e);
        return result.failed().setMessage(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public I18nResult<String> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        I18nResult<String> result = I18nResult.newInstance();
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return result.failed().setMessage(e.getMessage());
    }

    /**
     * 限流异常拦截
     */
    @ExceptionHandler({RateLimiterException.class})
    @ResponseBody
    public ResponseEntity<I18nResult<String>> handleRateLimiterException(Exception ex) {
        I18nResult<String> result = I18nResult.newInstance();
        result.failedBadRequest().setMessage(ex.getMessage());
        return result.toResponseEntity();
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public I18nResult<String> handleException(Exception e, HttpServletRequest request) {
        I18nResult<String> result = I18nResult.newInstance();
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return result.failed().setMessage(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public I18nResult<String> handleBindException(BindException e) {
        I18nResult<String> result = I18nResult.newInstance();
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return result.failed().setMessage(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public I18nResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        I18nResult<String> result = I18nResult.newInstance();
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return result.failed().setMessage(message);
    }


}
