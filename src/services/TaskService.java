package services;

import java.util.List;

import exception.DuplicateTaskException;
import exception.TaskFileException;
import model.Task;
import repository.Repository;
import repository.TaskLoadResult;

/**
 * Business operations on tasks. The GUI calls this class instead of touching
 * the file layer, and domain failures are declared as checked exceptions so
 * they cannot be ignored by the caller.
 */
public final class TaskService {

    private TaskService() {
    }

    public static TaskLoadResult loadTasks() throws TaskFileException {
        return Repository.loadTasks();
    }

    public static void addTask(Task task)
            throws DuplicateTaskException, TaskFileException {
        List<Task> existing = Repository.loadTasks().getTasks();
        for (Task current : existing) {
            if (sameIdentity(current, task)) {
                throw new DuplicateTaskException("A task named \"" + task.getTaskName()
                        + "\" for " + task.getCourse() + " on "
                        + Task.formatDate(task.getDeadline()) + " already exists.");
            }
        }
        Repository.appendTask(task);
    }

    public static void saveAll(List<Task> tasks) throws TaskFileException {
        Repository.saveAll(tasks);
    }

    private static boolean sameIdentity(Task a, Task b) {
        return a.getTaskName().equalsIgnoreCase(b.getTaskName())
                && a.getCourse().equalsIgnoreCase(b.getCourse())
                && a.getDeadline().equals(b.getDeadline());
    }
}
