package top.gaogle.framework.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.springframework.stereotype.Component;
import top.gaogle.framework.commons.enums.HttpStatusEnum;
import top.gaogle.framework.commons.service.SuperService;
import top.gaogle.framework.commons.util.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义限流异常处理
 *
 * @author gaogle
 * @since 1.0.0
 */
@Component
public class CustomBlockExceptionHandler extends SuperService implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        String message = "系统开小差了，请稍后再试";
        if (e instanceof FlowException) {
            message = "当前系统繁忙，请稍后再试";
        } else if (e instanceof DegradeException) {
            message = "服务暂时不可用，请稍后重试哦";
        } else if (e instanceof ParamFlowException) {
            message = "请求过于频繁，请稍等片刻再试";
        } else if (e instanceof SystemBlockException) {
            message = "系统正在保护中，请稍后再试";
        } else if (e instanceof AuthorityException) {
            message = "您暂无权限访问该功能，请联系管理员";
        }
        ServletUtil.webResponseWriter(httpServletResponse, HttpStatusEnum.INTERNAL_SERVER_ERROR, message);

    }
}
