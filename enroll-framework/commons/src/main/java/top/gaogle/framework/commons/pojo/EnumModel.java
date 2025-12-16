package top.gaogle.framework.commons.pojo;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class EnumModel<T extends Serializable> {

    private IndexedEnum<T> enumName;
    private T value;
    private String title;

    public EnumModel() {
    }

    public EnumModel(IndexedEnum<T> enumName) {
        this.enumName = enumName;
    }

    public EnumModel(IndexedEnum<T> enumName, T value, String title) {
        this.enumName = enumName;
        this.value = value;
        this.title = title;
    }


    public String name() {
        return enumName.name();
    }


    public T value() {
        return value;
    }


    public String title() {
        return title;
    }

    public IndexedEnum<T> getEnumName() {
        return enumName;
    }

    public void setEnumName(IndexedEnum<T> enumName) {
        this.enumName = enumName;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnumModel enumModel = (EnumModel) o;
        return Objects.equals(enumName, enumModel.enumName) && Objects.equals(value, enumModel.value) && Objects.equals(title, enumModel.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enumName, value, title);
    }

    @Override
    public String toString() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("enumName", enumName);
        map.put("value", value);
        map.put("title", title);
        return map.toString();
    }
}
