package top.gaogle.pojo.enums.security;

import top.gaogle.framework.commons.common.CommonsConst;

/**
 * 权限枚举常量
 *
 * @author goge
 * @since 1.0.0
 */
public class AuthorityEnumConst {

    private AuthorityEnumConst() {
        throw new IllegalStateException(CommonsConst.PROHIBIT_INSTANTIATION);
    }

    /**
     * 用户管理
     */
    public static final AuthorityEnum USER = AuthorityEnum.USER;
    public static final String USER_VIEW_ADMIN = "USER_VIEW_ADMIN";
    public static final String USER_PUT_ADMIN = "USER_PUT_ADMIN";

}
