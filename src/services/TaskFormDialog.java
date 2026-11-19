package services;

import gui.themes.Fonts;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.table.DefaultTableModel;

import repository.Repository;

import java.util.function.Consumer;
import java.util.*;

import model.Task;
import model.enums.Difficulty;
import model.enums.TaskStatus;
import model.enums.TaskType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskFormDialog {

    public static void showAdd(JFrame parentFrame, Consumer<Task> onTaskAdded) {
        showForm(parentFrame, "Add Task", null, onTaskAdded);
    }

    public static void showEdit(JFrame parentFrame, JTable taskTable) {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parentFrame, "Please select a task to edit.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
        int modelRow = taskTable.convertRowIndexToModel(selectedRow);
        Task current = Task.fromCsvRow(readRow(model, modelRow));

        showForm(parentFrame, "Edit Task", current, task -> {
            Object[] updated = task.toCsvRow();
            for (int i = 0; i < updated.length; i++) {
                model.setValueAt(updated[i], modelRow, i);
            }
            if (Repository.saveAll(readAllTasks(model))) {
                JOptionPane.showMessageDialog(parentFrame, "Task successfully updated!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Could not save changes to the CSV.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void showForm(JFrame parentFrame, String title, Task initial,
                                 Consumer<Task> onSave) {
        boolean isEdit = initial != null;

        JFrame frame = new JFrame(title);
        frame.setSize(600, 400);
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

        JLabel deadlineLbl = new JLabel("Deadline (MM/DD/YY):");
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

        if (isEdit) {
            taskField.setText(initial.getTaskName());
            courseField.setText(initial.getCourse());
            deadlineField.setText(initial.getDeadline()
                    .format(DateTimeFormatter.ofPattern("MM/dd/yy")));
            diffBox.setSelectedItem(initial.getDifficulty());
            typeBox.setSelectedItem(initial.getTaskType());
        }

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(Fonts.inputFont);
        clearButton.addActionListener(e -> {
            taskField.setText("");
            courseField.setText("");
            deadlineField.setText("");
            diffBox.setSelectedItem(Difficulty.DEFAULT);
            typeBox.setSelectedItem(TaskType.defaultType());
        });

        JButton completeButton = new JButton(isEdit ? "Save" : "Done");
        completeButton.setFont(Fonts.inputFont);
        completeButton.addActionListener(e -> {
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
                JOptionPane.showMessageDialog(frame, "Please enter a complete deadline in MM/DD/YY format.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                deadlineField.requestFocus();
                return;
            }

            LocalDate date;
            try {
                date = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("MM/dd/yy"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a real date in MM/DD/YY format.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                deadlineField.requestFocus();
                return;
            }

            Difficulty difficulty = (Difficulty) diffBox.getSelectedItem();
            TaskType type = (TaskType) typeBox.getSelectedItem();
            TaskStatus status = isEdit ? initial.getStatus() : TaskStatus.NOT_STARTED;

            Task task = new Task(taskName, course, date, difficulty, type, status);
            onSave.accept(task);
            frame.dispose();
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

            if (confirm == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
                int modelRow = taskTable.convertRowIndexToModel(selectedRow);

                List<Task> tasks = readAllTasks(model);
                tasks.remove(modelRow);
                model.removeRow(modelRow);

                if (Repository.saveAll(tasks)) {
                    JOptionPane.showMessageDialog(parentFrame, "Task successfully deleted!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        parentFrame.add(deleteButton);
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

            Task task = Task.fromCsvRow(readRow(model, modelRow));
            if (task.getStatus().isDone()) {
                JOptionPane.showMessageDialog(parentFrame, "This task is already completed.",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            task.setStatus(TaskStatus.DONE);
            model.setValueAt(task.getStatus().toString(), modelRow, model.findColumn("Status"));
            Repository.saveAll(readAllTasks(model));

            JOptionPane.showMessageDialog(parentFrame, "Task Done!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private static JFormattedTextField createDeadlineField() {
        try {
            MaskFormatter mask = new MaskFormatter("##/##/##");
            mask.setPlaceholderCharacter('_');
            JFormattedTextField field = new JFormattedTextField(mask);
            field.setColumns(8);
            return field;
        } catch (java.text.ParseException e) {
            return new JFormattedTextField();
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
