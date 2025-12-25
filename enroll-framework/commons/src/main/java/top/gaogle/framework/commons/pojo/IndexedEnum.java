package top.gaogle.framework.commons.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.gaogle.framework.commons.serializer.IndexedEnumSerializer;

import java.io.Serializable;

/**
 * 索引枚举接口
 *
 * @author gaogle
 * @since 1.0.0
 */
@JsonSerialize(using = IndexedEnumSerializer.class)
public interface IndexedEnum<V extends Serializable> extends Serializable {
    String name();

    V value();

    String title();
}
