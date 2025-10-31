package top.gaogle.enums.register;

import top.gaogle.IndexedEnum;

public enum InterviewTicketTemplateFlagEnum implements IndexedEnum<Integer> {

    FIRST(1, "模板一"),

    SECOND(2, "模板二"),


    THIRD (3, "模板三");

    private final Integer value;
    private final String title;

    InterviewTicketTemplateFlagEnum(Integer value, String title) {
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
