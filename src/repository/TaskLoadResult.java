package repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Task;

/**
 * The outcome of a load operation: the tasks that could be read and the
 * warnings describing rows that had to be skipped. Keeping both together lets
 * the caller show one honest message instead of silently losing data.
 */
public class TaskLoadResult {

    private final List<Task> tasks;
    private final List<String> warnings;

    public TaskLoadResult(List<Task> tasks, List<String> warnings) {
        this.tasks = new ArrayList<>(tasks);
        this.warnings = new ArrayList<>(warnings);
    }

    public static TaskLoadResult empty(String warning) {
        List<String> warnings = new ArrayList<>();
        if (warning != null && !warning.isBlank()) {
            warnings.add(warning);
        }
        return new TaskLoadResult(new ArrayList<>(), warnings);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
}
