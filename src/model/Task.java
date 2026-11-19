package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import exception.TaskValidationException;
import model.enums.Difficulty;
import model.enums.TaskStatus;
import model.enums.TaskType;
import services.IdGenerator;

/**
 * A single task. All state is private and every write goes through a mutator
 * that rejects invalid values, so no other class can put the object into a bad
 * state. Priority is derived from the type and difficulty, never stored.
 */
public final class Task {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MM/dd/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final int MAX_TEXT_LENGTH = 60;

    private final String id;
    private String taskName;
    private String course;
    private LocalDate deadline;
    private Difficulty difficulty;
    private TaskType taskType;
    private TaskStatus status;

    /** Creates a new task with a generated identifier. */
    public Task(String taskName, String course, LocalDate deadline,
                Difficulty difficulty, TaskType taskType, TaskStatus status) {
        this(IdGenerator.next(), taskName, course, deadline,
                difficulty, taskType, status);
    }

    /** Creates a task with a known identifier, used when reloading saved data. */
    public Task(String id, String taskName, String course, LocalDate deadline,
                Difficulty difficulty, TaskType taskType, TaskStatus status) {
        if (id == null || id.isBlank()) {
            throw new TaskValidationException("Task identifier cannot be empty.");
        }
        this.id = id.trim();
        setTaskName(taskName);
        setCourse(course);
        setDeadline(deadline);
        setDifficulty(difficulty);
        setTaskType(taskType);
        setStatus(status);
    }

    /** Priority is recalculated on demand from the current type and difficulty. */
    public double computePriority() {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), deadline);
        if (daysLeft < 1) {
            daysLeft = 1;
        }
        double urgency = 1.0 / daysLeft;
        double rawPriority = taskType.getWeight() * difficulty.getWeight() * urgency * 2;
        return Math.round(rawPriority * 100.0) / 100.0;
    }

    public boolean isDueSoon() {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), deadline);
        return daysLeft >= 0 && daysLeft <= 1 && !status.isDone();
    }

    public boolean isOverDue() {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), deadline);
        return daysLeft < 0 && !status.isDone();
    }

    public String getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getCourse() {
        return course;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setTaskName(String taskName) {
        String value = requireText(taskName, "Task name");
        if (value.length() > MAX_TEXT_LENGTH) {
            throw new TaskValidationException(
                    "Task name cannot be longer than " + MAX_TEXT_LENGTH + " characters.");
        }
        this.taskName = value;
    }

    public void setCourse(String course) {
        String value = requireText(course, "Course");
        if (value.length() > MAX_TEXT_LENGTH) {
            throw new TaskValidationException(
                    "Course cannot be longer than " + MAX_TEXT_LENGTH + " characters.");
        }
        this.course = value;
    }

    public void setDeadline(LocalDate deadline) {
        if (deadline == null) {
            throw new TaskValidationException("Deadline is required.");
        }
        this.deadline = deadline;
    }

    public void setDifficulty(Difficulty difficulty) {
        if (difficulty == null) {
            throw new TaskValidationException("Difficulty is required.");
        }
        this.difficulty = difficulty;
    }

    public void setTaskType(TaskType taskType) {
        if (taskType == null) {
            throw new TaskValidationException("Task type is required.");
        }
        this.taskType = taskType;
    }

    public void setStatus(TaskStatus status) {
        if (status == null) {
            throw new TaskValidationException("Status is required.");
        }
        this.status = status;
    }

    /** One place formats a date for both the table and the CSV file. */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            throw new TaskValidationException("Deadline is required.");
        }
        return date.format(DATE_FORMAT);
    }

    /** Strict parser: impossible dates such as 02/30/2026 are rejected. */
    public static LocalDate parseDate(String text) {
        String value = requireText(text, "Deadline");
        try {
            return LocalDate.parse(value, DATE_FORMAT);
        } catch (RuntimeException e) {
            throw new TaskValidationException(
                    "Deadline must be a real date in MM/DD/YYYY format: " + value, e);
        }
    }

    private static String requireText(String text, String fieldName) {
        if (text == null || text.isBlank()) {
            throw new TaskValidationException(fieldName + " is required.");
        }
        return text.trim();
    }

    /**
     * Row layout used by both the table model and the CSV file: seven display
     * columns followed by the identifier. Older seven-column rows are accepted
     * and given a generated identifier.
     */
    public Object[] toCsvRow() {
        return new Object[] {
                taskName,
                course,
                formatDate(deadline),
                difficulty.getLabel(),
                taskType.getLabel(),
                String.valueOf(computePriority()),
                status.toString(),
                id
        };
    }

    public static Task fromCsvRow(String[] row) {
        if (row == null || row.length < 7) {
            throw new TaskValidationException(
                    "Malformed task row: expected at least 7 columns.");
        }

        String id = row.length > 7 && !row[7].isBlank() ? row[7].trim() : IdGenerator.next();

        return new Task(id,
                row[0],
                row[1],
                parseDate(row[2]),
                Difficulty.fromTextStrict(row[3]),
                TaskType.fromTextStrict(row[4]),
                TaskStatus.fromTextStrict(row[6]));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Task)) {
            return false;
        }
        Task that = (Task) other;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return taskName + " (" + course + ")";
    }
}
