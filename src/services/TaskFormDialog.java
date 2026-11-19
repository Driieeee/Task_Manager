package services;

import gui.themes.Fonts;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import repository.StudentRepository;

import java.util.function.Consumer;
import java.util.*;

public class TaskFormDialog {

    public static void showAdd(JFrame parentFrame, Consumer<Object[]> onTaskAdded) {

        JFrame addTaskFrame = new JFrame("Add Task");
        addTaskFrame.setSize(600, 400);
        addTaskFrame.setLocationRelativeTo(parentFrame);
        addTaskFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
        JTextField deadlineField = new JTextField();
        deadlineField.setFont(Fonts.inputFont);

        JLabel diffLbl = new JLabel("Difficulty (1-10):");
        diffLbl.setFont(Fonts.labelFont);
        String[] difficulties = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        JComboBox<String> diffBox = new JComboBox<>(difficulties);
        diffBox.setFont(Fonts.inputFont);

        JLabel typeLbl = new JLabel("Type:");
        typeLbl.setFont(Fonts.labelFont);
        String[] types = { "Exam", "Assignment", "Project", "Quiz" };
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setFont(Fonts.inputFont);

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(Fonts.inputFont);
        clearButton.addActionListener(e -> {
            taskField.setText("");
            courseField.setText("");
            deadlineField.setText("");
        });

        JButton completeButton = new JButton("Done");
        completeButton.setFont(Fonts.inputFont);
        completeButton.addActionListener(e -> {
            String taskName = taskField.getText().trim();
            String course = courseField.getText().trim();
            String deadline = deadlineField.getText().trim();
            String difficulty = (String) diffBox.getSelectedItem();
            String type = (String) typeBox.getSelectedItem();

            if (taskName.isEmpty() || course.isEmpty() || deadline.isEmpty()) {
                JOptionPane.showMessageDialog(addTaskFrame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int level = Integer.parseInt(difficulty);
            String priority = level >= 8 ? "High" : level >= 5 ? "Medium" : "Low";
            String status = "Not Yet Started";

            onTaskAdded.accept(new Object[] { taskName, course, deadline, difficulty, type, priority, status });
            IdGenerator createKey = new IdGenerator();
            
            addTaskFrame.dispose();
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

        addTaskFrame.add(gridPanel);
        addTaskFrame.setVisible(true);
    }

    public static void showDelete(JFrame parentFrame, JTable taskTable, JButton deleteButton) {

        deleteButton.addActionListener(e -> {

            int selectedRow = taskTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    parentFrame,
                    "Please select a task to delete.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                parentFrame,
                "Are you sure you want to delete this task?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {

                DefaultTableModel model = (DefaultTableModel) taskTable.getModel();

                int modelRow = taskTable.convertRowIndexToModel(selectedRow);
                model.removeRow(modelRow);
                
                if (StudentRepository.saveAll(readAllRows(model))) {
                    JOptionPane.showMessageDialog(
                    parentFrame,
                    "Task successfully deleted!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                }
            }
        });

        parentFrame.add(deleteButton);
    }

    public static void showDone(JFrame parentFrame, JTable taskTable, JButton doneButton) {

        doneButton.addActionListener(e -> {

            int selectedRow = taskTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    parentFrame,
                    "Please select a task to mark as done.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            DefaultTableModel model = (DefaultTableModel) taskTable.getModel();

            int modelRow = taskTable.convertRowIndexToModel(selectedRow);

            String status = String.valueOf(model.getValueAt(modelRow, 6));

            if ("Done".equalsIgnoreCase(status.trim())) {
                JOptionPane.showMessageDialog(
                    parentFrame,
                    "This task is already completed.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            model.setValueAt("Done", modelRow, 6);

            JOptionPane.showMessageDialog(
                parentFrame,
                "Task Done!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    private static List<Object[]> readAllRows(DefaultTableModel model) {
        List<Object[]> rows = new ArrayList<>();
        for (int r = 0; r < model.getRowCount(); r++) {
            Object[] row = new Object[model.getColumnCount()];
            for (int c = 0; c < model.getColumnCount(); c++) {
                row[c] = model.getValueAt(r, c);
            }
            rows.add(row);
        }
        return rows;
    }
}