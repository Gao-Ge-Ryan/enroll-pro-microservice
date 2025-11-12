package top.gaogle;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import top.gaogle.i18n.I18nResult;

@FeignClient(contextId = "enroll-auth", value = "enroll-auth")
public interface TestFeign {

    @GetMapping("/test")
    I18nResult<String> insert();

}
