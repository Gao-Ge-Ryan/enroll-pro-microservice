package top.gaogle.framework.security.enums;

/**
 * 限流类型
 *
 * @author gaogle
 * @since 1.0.0
 */
public enum LimitTypeEnum {
    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据请求者IP进行限流
     */
    IP
}
