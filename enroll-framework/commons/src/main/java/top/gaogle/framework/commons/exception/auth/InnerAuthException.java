package top.gaogle.framework.commons.exception.auth;

/**
 * 内部认证异常
 *
 * @author gaogle
 */
public class InnerAuthException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InnerAuthException(String message) {
        super(message);
    }
}
