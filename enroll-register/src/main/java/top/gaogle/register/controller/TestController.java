package top.gaogle.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.security.annotation.Logical;
import top.gaogle.framework.security.annotation.RequiresPermissions;
import top.gaogle.pojo.enums.security.AuthorityEnumConst;
import top.gaogle.register.service.TestService;

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

    @RequiresPermissions(value = {AuthorityEnumConst.USER_VIEW_ADMIN, AuthorityEnumConst.USER_PUT_ADMIN}, logical = Logical.OR)
    @GetMapping
    public I18nResult<String> insert() {
        return testService.test();
    }

    @GetMapping("/tcc")
    public I18nResult<String> tcc() {
        return testService.tcc();
    }

    @GetMapping("/testNacos")
    public I18nResult<String> testNacos() {
        return testService.testNacos();
    }

}
