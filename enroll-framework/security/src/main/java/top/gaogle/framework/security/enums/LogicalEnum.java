package top.gaogle.framework.security.enums;

/**
 * 权限注解的验证模式
 *
 * @author gaogle
 */
public enum LogicalEnum {
    /**
     * 必须具有所有的元素
     */
    AND,

    /**
     * 只需具有其中一个元素
     */
    OR
}
