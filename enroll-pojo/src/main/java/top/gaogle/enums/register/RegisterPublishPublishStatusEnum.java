package top.gaogle.enums.register;

import top.gaogle.IndexedEnum;

public enum RegisterPublishPublishStatusEnum implements IndexedEnum<Integer> {

    PUBLISHED(1, "已发布"),

    UNPUBLISHED(2, "取消发布"),
    ;

    private final Integer value;
    private final String title;

    RegisterPublishPublishStatusEnum(Integer value, String title) {
        this.value = value;
        this.title = title;
    }


    @Override
    public Integer value() {
        return value;
    }

    @Override
    public String title() {
        return title;
    }

}
