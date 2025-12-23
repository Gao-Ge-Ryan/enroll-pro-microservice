package top.gaogle.framework.commons.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class SpringUtil implements BeanFactoryPostProcessor, ApplicationContextAware {

    private static ConfigurableListableBeanFactory beanFactory;

    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtil.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 获取对象
     *
     * @return Object 一个以所给名字注册的bean的实例
     */
    public static <T> T getBean(String name) throws BeansException {
        return CastUtils.cast(beanFactory.getBean(name));
    }

    /**
     * 获取类型为requiredType的对象
     */
    public static <T> T getBean(Class<T> clz) throws BeansException {
        return CastUtils.cast(beanFactory.getBean(clz));
    }

    /**
     * 通过类型获取其全部的实现对象
     *
     * @param requiredType 类型
     * @return bean实例
     */
    public static <T> Collection<T> getBeans(Class<T> requiredType) {
        try {
            Map<String, T> beansMap = applicationContext.getBeansOfType(requiredType);
            return beansMap.values();
        } catch (Exception e) {
            return null;
        }
    }
}
