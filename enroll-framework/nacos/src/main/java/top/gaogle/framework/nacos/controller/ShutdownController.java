package top.gaogle.framework.nacos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.gaogle.framework.commons.i18n.I18nResult;
import top.gaogle.framework.nacos.service.ShutdownService;


/**
 * 优雅停机
 *
 * @author gaogle
 * @since 1.0.0
 */
@RestController
@RequestMapping("/shutdown")
public class ShutdownController {

    private final ShutdownService shutdownService;

    @Autowired
    public ShutdownController(ShutdownService shutdownService) {
        this.shutdownService = shutdownService;
    }

    @GetMapping
    public I18nResult<Object> shutdown() {
        return shutdownService.shutdown();
    }

}
