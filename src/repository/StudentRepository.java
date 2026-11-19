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

/**
 * A tiny "pseudo database": it reads and writes one CSV file instead of using a real database.
 * The file is created automatically, so you do not have to make it by hand.
 *
 * Important: both public methods handle their own errors. They never throw checked
 * exceptions, so the GUI code can call them without try/catch everywhere.
 */
public class StudentRepository {

    // This path is relative to the folder you run the app from (the project root).
    // ../Task_Manager/src/data/tasks.csv appears here as src/data/tasks.csv.
    private static final String FILE_NAME = "src/data/tasks.csv";

    // The first line of the CSV. It names the columns, and is written when the file is new.
    private static final String HEADER = "Task,Course,Deadline,Difficulty,Type,Priority,Status";

    /**
     * Reads every task from the CSV file.
     * Returns an empty list if the file does not exist yet or cannot be read.
     */
    public static List<String[]> loadTasks() {
        List<String[]> tasks = new ArrayList<>();
        Path path = Path.of(FILE_NAME);

        // No file yet = no saved tasks. This is normal on the first launch.
        if (!Files.exists(path)) {
            return tasks;
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue; // skip empty lines
                }
                if (firstLine) {
                    firstLine = false;
                    if (line.equalsIgnoreCase(HEADER)) {
                        continue; // the header describes columns, it is not a task
                    }
                }
                tasks.add(parseLine(line)); // convert "a,b,c" into String[] { "a", "b", "c" }
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            // If reading fails, keep whatever rows were read and carry on with an empty table.
            tasks.clear();
        }
        return tasks;
    }

    /**
     * Adds one task as a new line in the CSV file.
     * Creates the folder and the file (with its header) if they do not exist.
     * Returns true when saved, false if something went wrong.
     */
    public static boolean appendTask(Object[] row) {
        Path path = Path.of(FILE_NAME);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent()); // make sure data/ exists
            }

            boolean needsHeader = !Files.exists(path) || Files.size(path) == 0;

            // CREATE + APPEND means we add to the end of the file instead of overwriting it.
            try (BufferedWriter writer = Files.newBufferedWriter(
                    path,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                if (needsHeader) {
                    writer.write(HEADER);
                    writer.newLine();
                }
                writer.write(toCsvLine(row));
                writer.newLine();
            }
            return true;
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            // The GUI can show a message when this returns false.
            return false;
        }
    }

    /** Joins the 6 values of a row into one CSV line: value,value,value,... */
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

    /** Quotes a value if it contains a comma, quote, or line break, so the CSV stays valid. */
    private static String escape(String value) {
        if (value.contains(",") || value.contains("\"")
                || value.contains("\n") || value.contains("\r")) {
            return '"' + value.replace("\"", "\"\"") + '"';
        }
        return value;
    }

    /** Splits a CSV line back into separate values (the opposite of toCsvLine). */
    private static String[] parseLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"'); // "" means one real quote
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

    // overwrites the .csv file 
    public static boolean saveAll(List<Object[]> rows) {
        Path path = Path.of(FILE_NAME);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                writer.write(HEADER);
                writer.newLine();;

                for(Object[] row : rows) {
                    writer.write(toCsvLine(row));
                    writer.newLine();
                }
            }
            return true;
        }
        catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }
}
