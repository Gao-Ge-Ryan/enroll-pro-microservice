

package top.gaogle.framework.commons.util;

/**
 * <p>
 *     用于确定对象字段的值。
 *     若要进行复制操作的源或目标对象中具有需要特别处理的字段（或无法使用常规方式复制），可以实现该接口来进行特别处理。
 *     该接口 {@link #resolve(Object, String, Object)} 方法接收 源对象、源字段名、源字段值，并由具体实现决定最终返回何值以复制给目标对象。
 * </p>
 *
 * @author gaogle
 * @since 1.0.0
 * @see FieldTypeValueResolver
 * @see FieldNameValueResolver
 */
public interface FieldValueResolver {

    Object resolve(Object source, String fieldName, Object fieldValue);

}
