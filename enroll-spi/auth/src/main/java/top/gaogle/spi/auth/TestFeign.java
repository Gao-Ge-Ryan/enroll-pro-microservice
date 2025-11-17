package top.gaogle.spi.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import top.gaogle.framework.commons.i18n.I18nResult;

@FeignClient(contextId = "enroll-auth", value = "enroll-auth")
public interface TestFeign {

    @GetMapping("/test")
    I18nResult<String> insert();

    @GetMapping("/test/tcc")
    I18nResult<String> tcc();

}
