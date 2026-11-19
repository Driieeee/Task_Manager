package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import exception.TaskFileException;
import exception.TaskValidationException;
import model.Task;

/**
 * File layer for tasks. It owns CSV parsing/writing and reports unusable data
 * instead of silently hiding it. Business rules live in the service layer.
 */
public class Repository {

    private static final String FILE_NAME = "src/data/tasks.csv";
    private static final String HEADER =
            "Task,Course,Deadline,Difficulty,Type,Priority,Status,ID,Hours";
    private static final String LEGACY_HEADER =
            "Task,Course,Deadline,Difficulty,Type,Priority,Status";

    /**
     * Loads every readable row. A missing file is an empty result with a
     * warning; a corrupt row is skipped and counted; an unreadable file raises
     * a checked exception for the caller to report.
     */
    public static TaskLoadResult loadTasks() throws TaskFileException {
        List<Task> tasks = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        Path path = Path.of(FILE_NAME);

        if (!Files.exists(path)) {
            return TaskLoadResult.empty("Data file not found (" + FILE_NAME
                    + "); a new one will be created on first save.");
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                if (firstLine) {
                    firstLine = false;
                    if (line.equalsIgnoreCase(HEADER)
                            || line.equalsIgnoreCase(LEGACY_HEADER)) {
                        continue;
                    }
                }

                try {
                    tasks.add(Task.fromCsvRow(parseLine(line)));
                } catch (TaskValidationException e) {
                    warnings.add("Skipped line " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new TaskFileException("Could not read " + FILE_NAME + ".", e);
        }

        return new TaskLoadResult(tasks, warnings);
    }

    public static void appendTask(Task task) throws TaskFileException {
        Path path = Path.of(FILE_NAME);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            boolean needsHeader = !Files.exists(path) || Files.size(path) == 0;

            try (BufferedWriter writer = Files.newBufferedWriter(
                    path,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                if (needsHeader) {
                    writer.write(HEADER);
                    writer.newLine();
                }
                writer.write(toCsvLine(task.toCsvRow()));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new TaskFileException("Could not save task to " + FILE_NAME + ".", e);
        }
    }

    public static void saveAll(List<Task> tasks) throws TaskFileException {
        Path path = Path.of(FILE_NAME);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(
                    path,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)) {

                writer.write(HEADER);
                writer.newLine();

                for (Task task : tasks) {
                    writer.write(toCsvLine(task.toCsvRow()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new TaskFileException("Could not write tasks to " + FILE_NAME + ".", e);
        }
    }

    private static String toCsvLine(Object[] row) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(escape(row[i] == null ? "" : row[i].toString()));
        }
        return sb.toString();
    }

    private static String escape(String value) {
        if (value.contains(",") || value.contains("\"")
                || value.contains("\n") || value.contains("\r")) {
            return '"' + value.replace("\"", "\"\"") + '"';
        }
        return value;
    }

    private static String[] parseLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else if (c == '"') {
                inQuotes = true;
            } else if (c == ',') {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        values.add(current.toString());
        return values.toArray(new String[0]);
    }
}
