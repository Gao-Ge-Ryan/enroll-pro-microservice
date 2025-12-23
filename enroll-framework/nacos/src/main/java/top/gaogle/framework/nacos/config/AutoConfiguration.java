package top.gaogle.framework.nacos.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.gaogle.framework.nacos.controller.ShutdownController;
import top.gaogle.framework.nacos.service.ShutdownService;


@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication // 只有 Web 应用才生效
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ShutdownService shutdownService() {
        return new ShutdownService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ShutdownController shutdownServiceController(ShutdownService shutdownService) {
        return new ShutdownController(shutdownService);
    }

}
