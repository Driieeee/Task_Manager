package exception;

public class TaskValidationException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public TaskValidationException(String message) {
        super(message);
    }

    public TaskValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}