package services;

import java.awt.Font;
import java.awt.GridLayout;
import java.swing.*;

public class TaskFormDialog {

    public static void show(JFrame parentFrame, Runnable onTaskAdded) {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font inputFont = new Font("Arial", Font.PLAIN, 14);

            JFrame addTaskFrame = new JFrame("Add Task");
            addTaskFrame.setSize(600, 400); // FIX: was 300, too short for 6 rows
            addTaskFrame.setLocationRelativeTo(frame);
            addTaskFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel gridPanel = new JPanel(new GridLayout(0, 2, 10, 15));
            gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel taskLbl = new JLabel("Tasks:");
            taskLbl.setFont(labelFont);
            JTextField taskField = new JTextField();
            taskField.setFont(inputFont);

            JLabel courseLbl = new JLabel("Course:");
            courseLbl.setFont(labelFont);
            JTextField courseField = new JTextField();
            courseField.setFont(inputFont);

            JLabel deadlineLbl = new JLabel("Deadline (MM/DD/YY):");
            deadlineLbl.setFont(labelFont);
            JTextField deadlineField = new JTextField();
            deadlineField.setFont(inputFont);

            JLabel diffLbl = new JLabel("Difficulty (1-10):");
            diffLbl.setFont(labelFont);
            String[] difficulties = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
            JComboBox<String> diffBox = new JComboBox<>(difficulties);
            diffBox.setFont(inputFont);

            JLabel typeLbl = new JLabel("Type:");
            typeLbl.setFont(labelFont);
            String[] types = { "Exam", "Assignment", "Project", "Quiz" };
            JComboBox<String> typeBox = new JComboBox<>(types);
            typeBox.setFont(inputFont);

            JButton clearButton = new JButton("Clear");
            clearButton.setFont(inputFont);
            clearButton.addActionListener(e -> {
                taskField.setText("");
                courseField.setText("");
                deadlineField.setText("");
            });

            JButton compleButton = new JButton("Done");
            compleButton.setFont(inputFont);

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
            gridPanel.add(compleButton);

            addTaskFrame.add(gridPanel);
            addTaskFrame.setVisible(true);
    }
}