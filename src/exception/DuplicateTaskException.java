package exception;

/**
 * Checked exception raised when a task with the same name, course and deadline
 * is already stored. Domain failures of this kind are not field errors, so they
 * are reported with a checked exception that callers must handle explicitly.
 */
public class DuplicateTaskException extends Exception {

    private static final long serialVersionUID = 1L;

    public DuplicateTaskException(String message) {
        super(message);
    }

    public DuplicateTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
