package top.gaogle.framework.commons.exception;
/**
 * 运行时异常
 *
 * @author gaogle
 */
public class LogAspectException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LogAspectException(String message) {
        super(message);
    }
}
