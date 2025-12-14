package top.gaogle.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.auth.service.TestService;
import top.gaogle.framework.security.annotation.InnerAuth;

/**
 * @author gaogle
 * @since 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }
    @InnerAuth
    @GetMapping
    public I18nResult<String> insert() {
        return testService.test();
    }

    @GetMapping("/tcc")
    public I18nResult<String> tcc() {
        return testService.tcc();
    }

}
