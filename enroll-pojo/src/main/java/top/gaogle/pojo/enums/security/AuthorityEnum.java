package top.gaogle.pojo.enums.security;


import top.gaogle.framework.commons.pojo.IndexedEnum;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限枚举
 * <p>
 * shift取值范围：0~62，最高位符号位
 * MODULE所有权限二进制值：0111111111111111111111111111111111111111111111111111111111111111
 * @author goge
 * @since 1.0.0
 */
public enum AuthorityEnum implements IndexedEnum<Long> {

    /**
     * 用户管理
     */
    USER("user", 0L, "用户管理", 0L, Type.MODULE),
    USER_VIEW_ADMIN(USER, 0L, "管理端用户查看", 1L, Type.UNIT),
    USER_PUT_ADMIN(USER, 0L, "管理端用户修改", 2L, Type.UNIT),
    USER_DELETE_ADMIN(USER, 0L, "管理端用户删除", 3L, Type.UNIT),
    USER_PUT_ROLE_ADMIN(USER, 0L, "管理端设置角色", 4L, Type.UNIT),
    USER_ADMIN_SIMULATION_LOGIN_ADMIN(USER, 0L, "管理端模拟登录", 5L, Type.UNIT),


    ;

    private final String module;
    private final AuthorityEnum parent;
    /**
     * 序号值
     * 注：范围0-62，63为符号位不可用
     */
    private final Long value;
    private final String title;
    /**
     * 权限值
     * 注：范围0-62，0作为功能模块Type.MODULE使用，1-62作为功能单元Type.UNIT使用，63为符号位不可用
     */
    private final Long shift;
    private final Type type;
    private final List<AuthorityEnum> children;


    AuthorityEnum(String module, Long value, String title, Long shift, Type type) {
        this(module, null, value, title, shift, type);
    }

    AuthorityEnum(AuthorityEnum parent, Long value, String title, Long shift, Type type) {
        this(parent.module, parent, value, title, shift, type);
    }

    /**
     * 注：shift，value 用于逻辑位移的操作数
     */
    AuthorityEnum(String module, AuthorityEnum parent, Long value, String title, Long shift, Type type) {
        this.module = module;
        this.parent = parent;
        if (value == null) {
            this.value = 0L;
        } else {
            this.value = 1L << value;// 逻辑位移 序号值
        }
        this.title = title;
        if (shift == null) {
            this.shift = 0L;
        } else {
            this.shift = 1L << shift; // 逻辑位移 权限值
        }
        this.type = type;
        this.children = new ArrayList<>();
        if (this.parent != null) {
            this.parent.children.add(this);
        }
    }

    public enum Type {
        MODULE, // 功能模块
        UNIT, // 功能单元
    }

    @Override
    public Long value() {
        return value;
    }

    @Override
    public String title() {
        return title;
    }

    public Type type() {
        return type;
    }

    public List<AuthorityEnum> children() {
        return children;
    }

    public String module() {
        return module;
    }

    public Long shift() {
        return shift;
    }

    public AuthorityEnum parent() {
        return parent;
    }


    // 访问父类枚举
    public static List<AuthorityEnum> getAllParentEnum() {
        return Arrays.stream(AuthorityEnum.values())
                .filter(value -> Type.MODULE.equals(value.type))
                .collect(Collectors.toList());
    }

    // 访问父类枚举
    public static List<AuthorityEnumModel> getAllParentEnumModel() {
        return getAllParentEnum().stream()
                .map(AuthorityEnumModel::new)
                .collect(Collectors.toList());
    }

}
