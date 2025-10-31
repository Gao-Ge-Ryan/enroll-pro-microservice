package top.gaogle.enums.register;


import top.gaogle.IndexedEnum;

public enum DynamicOptionsStatusEnum implements IndexedEnum<Integer> {

    NOT_ENABLED(0, "未启用"),
    ENABLE(1, "启用"),

    ;

    private final Integer value;
    private final String title;

    DynamicOptionsStatusEnum(Integer value, String title) {
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
