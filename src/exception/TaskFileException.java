package exception;

/**
 * Checked exception raised when the task data file cannot be read or written.
 * It keeps file failures separate from input-validation failures and forces the
 * GUI layer to decide how the failure is reported to the user.
 */
public class TaskFileException extends Exception {

    private static final long serialVersionUID = 1L;

    public TaskFileException(String message) {
        super(message);
    }

    public TaskFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
