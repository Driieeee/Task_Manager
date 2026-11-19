package services;

import gui.themes.Fonts;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.table.DefaultTableModel;

import exception.TaskFileException;
import exception.TaskValidationException;

import java.util.function.Function;
import java.util.*;

import model.Task;
import model.enums.Difficulty;
import model.enums.TaskStatus;
import model.enums.TaskType;
import java.time.LocalDate;

/**
 * One shared form used for both creating and editing a task. The form only
 * collects values; validation happens in the model and business errors are
 * reported by the callback. Returning true means the save succeeded.
 */
public class TaskFormDialog {

    public static void showAdd(JFrame parentFrame, Function<Task, Boolean> onTaskAdded) {
        showForm(parentFrame, "Add Task", null, onTaskAdded);
    }

    public static void showEdit(JFrame parentFrame, JTable taskTable) {
        int selectedRow = taskTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Please select a task to edit.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
        int modelRow = taskTable.convertRowIndexToModel(selectedRow);

        Task current;
        try {
            current = Task.fromCsvRow(readRow(model, modelRow));
        } catch (TaskValidationException e) {
            JOptionPane.showMessageDialog(parentFrame, "Stored task data is invalid: " + e.getMessage(),
                    "Data Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (current.getStatus().isDone()) {
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Completed tasks cannot be edited.",
                    "Edit Restricted",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        showForm(parentFrame, "Edit Task", current, task -> {
            List<Task> tasks = safeReadAll(parentFrame, model);
            if (tasks == null) {
                return false;
            }
            // Keep the identifier from the stored row: editing must not create a
            // new identity.
            Task edited = new Task(current.getId(), task.getTaskName(), task.getCourse(),
                    task.getDeadline(), task.getDifficulty(), task.getTaskType(),
                    task.getStatus(), task.getEstimatedHours());
            tasks.set(modelRow, edited);

            try {
                TaskService.saveAll(tasks);
            } catch (TaskFileException e) {
                JOptionPane.showMessageDialog(parentFrame, e.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Object[] updated = edited.toCsvRow();
            for (int i = 0; i < updated.length; i++) {
                model.setValueAt(updated[i], modelRow, i);
            }
            JOptionPane.showMessageDialog(parentFrame, "Task successfully updated!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        });
    }

    private static void showForm(JFrame parentFrame, String title, Task initial,
                                  Function<Task, Boolean> onSave) {
        boolean isEdit = initial != null;

        JFrame frame = new JFrame(title);
        frame.setSize(600, 440);
        frame.setLocationRelativeTo(parentFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel taskLbl = new JLabel("Tasks:");
        taskLbl.setFont(Fonts.labelFont);
        JTextField taskField = new JTextField();
        taskField.setFont(Fonts.inputFont);

        JLabel courseLbl = new JLabel("Course:");
        courseLbl.setFont(Fonts.labelFont);
        JTextField courseField = new JTextField();
        courseField.setFont(Fonts.inputFont);

        JLabel deadlineLbl = new JLabel("Deadline (MM/DD/YYYY):");
        deadlineLbl.setFont(Fonts.labelFont);
        final JFormattedTextField deadlineField = createDeadlineField();
        deadlineField.setFont(Fonts.inputFont);

        JLabel diffLbl = new JLabel("Difficulty (1-5):");
        diffLbl.setFont(Fonts.labelFont);
        JComboBox<Difficulty> diffBox = new JComboBox<>(Difficulty.values());
        diffBox.setFont(Fonts.inputFont);

        JLabel typeLbl = new JLabel("Type:");
        typeLbl.setFont(Fonts.labelFont);
        JComboBox<TaskType> typeBox = new JComboBox<>(TaskType.values());
        typeBox.setFont(Fonts.inputFont);

        JLabel hoursLbl = new JLabel("Estimated Hours (0.5-12):");
        hoursLbl.setFont(Fonts.labelFont);
        JTextField hoursField = new JTextField();
        hoursField.setFont(Fonts.inputFont);

        if (isEdit) {
            taskField.setText(initial.getTaskName());
            courseField.setText(initial.getCourse());
            deadlineField.setText(Task.formatDate(initial.getDeadline()));
            diffBox.setSelectedItem(initial.getDifficulty());
            typeBox.setSelectedItem(initial.getTaskType());
            hoursField.setText(String.valueOf(initial.getEstimatedHours()));
        } else {
            hoursField.setText(String.valueOf(Task.DEFAULT_HOURS));
        }

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(Fonts.inputFont);
        clearButton.addActionListener(e -> {
            taskField.setText("");
            courseField.setText("");
            deadlineField.setText("");
            diffBox.setSelectedItem(Difficulty.DEFAULT);
            typeBox.setSelectedItem(TaskType.defaultType());
            hoursField.setText(String.valueOf(Task.DEFAULT_HOURS));
        });

        JButton completeButton = new JButton(isEdit ? "Save" : "Done");
        completeButton.setFont(Fonts.inputFont);
        completeButton.addActionListener(e -> {
            completeButton.setEnabled(false);
            try {
                String taskName = taskField.getText().trim();
                String course = courseField.getText().trim();
                String deadline = deadlineField.getText().trim();

                if (taskName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a task name.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    taskField.requestFocus();
                    return;
                }
                if (course.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a course.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    courseField.requestFocus();
                    return;
                }
                if (deadline.isEmpty() || deadline.contains("_")) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a complete deadline in MM/DD/YYYY format.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    deadlineField.requestFocus();
                    return;
                }

                LocalDate date = Task.parseDate(deadline);

                double hours;
                try {
                    hours = Double.parseDouble(hoursField.getText().trim());
                } catch (NumberFormatException ex) {
                    throw new TaskValidationException("Estimated hours must be a number.");
                }

                Difficulty difficulty = (Difficulty) diffBox.getSelectedItem();
                TaskType type = (TaskType) typeBox.getSelectedItem();
                TaskStatus status = isEdit ? initial.getStatus() : TaskStatus.NOT_STARTED;

                Task task = new Task(taskName, course, date, difficulty, type, status, hours);
                if (Boolean.TRUE.equals(onSave.apply(task))) {
                    frame.dispose();
                }
            } catch (TaskValidationException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(),
                        "Invalid Task", JOptionPane.ERROR_MESSAGE);
                taskField.requestFocus();
            } finally {
                completeButton.setEnabled(true);
            }
        });

        gridPanel.add(taskLbl);
        gridPanel.add(taskField);
        gridPanel.add(courseLbl);
        gridPanel.add(courseField);
        gridPanel.add(deadlineLbl);
        gridPanel.add(deadlineField);
        gridPanel.add(diffLbl);
        gridPanel.add(diffBox);
        gridPanel.add(typeLbl);
        gridPanel.add(typeBox);
        gridPanel.add(hoursLbl);
        gridPanel.add(hoursField);
        gridPanel.add(clearButton);
        gridPanel.add(completeButton);

        frame.add(gridPanel);
        frame.setVisible(true);
    }

    public static void showDelete(JFrame parentFrame, JTable taskTable, JButton deleteButton) {
        deleteButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Please select a task to delete.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(parentFrame,
                    "Are you sure you want to delete this task?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
            int modelRow = taskTable.convertRowIndexToModel(selectedRow);

            List<Task> tasks = safeReadAll(parentFrame, model);
            if (tasks == null) {
                return;
            }
            tasks.remove(modelRow);

            deleteButton.setEnabled(false);
            try {
                TaskService.saveAll(tasks);
                model.removeRow(modelRow);
                JOptionPane.showMessageDialog(parentFrame, "Task successfully deleted!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (TaskFileException ex) {
                JOptionPane.showMessageDialog(parentFrame, ex.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                deleteButton.setEnabled(true);
            }
        });
    }

    public static void showDone(JFrame parentFrame, JTable taskTable, JButton doneButton) {
        doneButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Please select a task to mark as done.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
            int modelRow = taskTable.convertRowIndexToModel(selectedRow);

            Task task;
            try {
                task = Task.fromCsvRow(readRow(model, modelRow));
            } catch (TaskValidationException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Stored task data is invalid: " + ex.getMessage(),
                        "Data Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (task.getStatus().isDone()) {
                JOptionPane.showMessageDialog(parentFrame, "This task is already completed.",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            task.setStatus(TaskStatus.DONE);
            List<Task> tasks = safeReadAll(parentFrame, model);
            if (tasks == null) {
                return;
            }
            tasks.set(modelRow, task);

            doneButton.setEnabled(false);
            try {
                TaskService.saveAll(tasks);
                model.setValueAt(task.getStatus().toString(), modelRow, model.findColumn("Status"));
                JOptionPane.showMessageDialog(parentFrame, "Task Done!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (TaskFileException ex) {
                JOptionPane.showMessageDialog(parentFrame, ex.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                doneButton.setEnabled(true);
            }
        });
    }

    private static JFormattedTextField createDeadlineField() {
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            JFormattedTextField field = new JFormattedTextField(mask);
            field.setColumns(8);
            return field;
        } catch (java.text.ParseException e) {
            return new JFormattedTextField();
        }
    }

    private static List<Task> safeReadAll(JFrame parent, DefaultTableModel model) {
        try {
            return readAllTasks(model);
        } catch (TaskValidationException e) {
            JOptionPane.showMessageDialog(parent, "Stored task data is invalid: " + e.getMessage(),
                    "Data Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private static String[] readRow(DefaultTableModel model, int modelRow) {
        String[] row = new String[model.getColumnCount()];
        for (int c = 0; c < row.length; c++) {
            row[c] = String.valueOf(model.getValueAt(modelRow, c));
        }
        return row;
    }

    private static List<Task> readAllTasks(DefaultTableModel model) {
        List<Task> tasks = new ArrayList<>();
        for (int r = 0; r < model.getRowCount(); r++) {
            tasks.add(Task.fromCsvRow(readRow(model, r)));
        }
        return tasks;
    }
}
