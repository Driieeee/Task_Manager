// Handles the reading and writing of .csv files containing the data
package repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Task;

public class StudentRepository {
    // Initialized the pseudo database
    private static final String FILE_NAME = "data.csv";

    private static void SaveTask(Task task) throws IOException {
        FileWriter fw = new FileWriter(FILE_NAME, true);
        fw.write(task.toString());
        fw.close();
    }
}