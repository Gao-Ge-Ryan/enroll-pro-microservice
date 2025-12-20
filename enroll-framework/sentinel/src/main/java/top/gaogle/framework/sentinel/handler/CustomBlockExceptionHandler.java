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
        String message = null;
        if (e instanceof FlowException) {
            message = "接口被限流了";
        } else if (e instanceof DegradeException) {
            message = "接口被降级了";
        } else if (e instanceof ParamFlowException) {
            message = "接口热点参数限流了";
        } else if (e instanceof SystemBlockException) {
            message = "接口触发系统保护规则了";
        } else if (e instanceof AuthorityException) {
            message = "授权接口不通过";
        } else {
            message = e.getMessage();
        }
        ServletUtil.webResponseWriter(httpServletResponse, HttpStatusEnum.INTERNAL_SERVER_ERROR, message);

    }
}
