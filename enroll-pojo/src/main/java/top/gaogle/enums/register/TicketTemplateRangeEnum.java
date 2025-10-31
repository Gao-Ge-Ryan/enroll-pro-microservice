package top.gaogle.enums.register;

import top.gaogle.IndexedEnum;

public enum TicketTemplateRangeEnum implements IndexedEnum<Integer> {

    /**
     * 新申请
     */
    ALL_VIEW(0, "全部可见"),

    /**
     * 企业可见
     */
    ENTERPRISE_VIEW(1, "企业可见"),

    ;

    private final Integer value;
    private final String title;

    TicketTemplateRangeEnum(Integer value, String title) {
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
