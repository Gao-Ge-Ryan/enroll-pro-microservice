package top.gaogle.framework.commons.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import top.gaogle.framework.commons.common.KeyValue;
import top.gaogle.framework.commons.pojo.IndexedEnum;

import java.io.IOException;

/**
 * 索引枚举序列化器
 *
 * @author gaogle
 * @since 1.0.0
 */
public class IndexedEnumSerializer extends StdSerializer<IndexedEnum> {

    private static final long serialVersionUID = -4076094908139895557L;

    protected IndexedEnumSerializer() {
        super(IndexedEnum.class);
    }

    @Override
    public void serialize(IndexedEnum aEnum, JsonGenerator gen, SerializerProvider provider) throws IOException {

        // 将 IndexedEnum 枚举类型字段序列化为 name 和 title 两个字段
        if (gen.getOutputContext().inArray()) {
            gen.writeObject(KeyValue.create().entry("name", getEnumName(aEnum)).entry("title", getEnumTitle(aEnum)));
        } else {
            gen.writeObject(getEnumName(aEnum));
            String title = gen.getOutputContext().getCurrentName() + "Title";
            gen.writeObjectField(title, getEnumTitle(aEnum));
        }

    }

    public String getEnumName(IndexedEnum<?> indexedEnum) {
        return indexedEnum.name();
    }

    public String getEnumTitle(IndexedEnum<?> indexedEnum) {
        return indexedEnum.title();
    }

}
