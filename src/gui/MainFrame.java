package gui;

import gui.themes.Colors;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;

public class MainFrame {

    public static void main(String[] args) {

        // fonts
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font inputFont = new Font("Arial", Font.PLAIN, 14);

        // create JFrame
        JFrame frame = new JFrame();

        frame.setSize(1000, 630); // FIX: was 600, the title bar cut off the bottom panel
        frame.setTitle("DueSmart");
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, 1000, 100);
        topPanel.setBackground(Colors.TOP_PANEL);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        topPanel.setLayout(null);

        // create subPanels
        JPanel subPanel1 = new JPanel();
        subPanel1.setBounds(0, 100, 1000, 50);
        subPanel1.setBackground(Colors.SUB_PANEL1);
        subPanel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        subPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel subPanel2 = new JPanel();
        subPanel2.setBounds(0, 500, 1000, 105);
        subPanel2.setBackground(Colors.SUB_PANEL2);
        subPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // create comboBoxes
        String[] courses = { "Discrete Structures", "Lineat Algebra" };
        JComboBox<String> courseBox = new JComboBox<>(courses);
        courseBox.setFont(inputFont);

        String[] typeTask = { "Exam", "Assignment", "Project", "Quiz" };
        JComboBox<String> typeTaskBox = new JComboBox<>(typeTask);
        typeTaskBox.setFont(inputFont);

        // create label for panel
        JLabel header = new JLabel("Student Task Manager");
        header.setBounds(30, 0, 1000, 100);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(Color.WHITE);

        // --CRUD BUTTONS--
        JButton addBtn = new JButton("Add Task");
        addBtn.setBounds(20, 10, 100, 100);

        addBtn.addActionListener(e -> {
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
        });

        JButton editBtn = new JButton("Edit");
        editBtn.setBounds(30, 10, 100, 100);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(40, 10, 100, 100);

        deleteBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this task?",
                    "Confirm Action", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame, "Task Deleted");
            }
        });

        JButton doneBtn = new JButton("Mark as Done");
        doneBtn.setBounds(50, 10, 100, 100);

        // Data table
        String[] columnName = { "Task", "Course", "Deadline", "Difficulty", "Type", "Priority" };
        String[][] data = {
                { "Okay test", "ADET", "10/20/22", "5", "Exam", "90" },
                { "Not So Okay Test", "DS", "10/20/22", "9", "Exam", "99" },
                { "", "", "", "", "", "" } // FIX: had 5 values for 6 columns, which throws an error while painting
        };
        JTable table = new JTable(data, columnName);
        JScrollPane scroll = new JScrollPane(table);

        scroll.setBounds(4, 154, 992, 342);
        frame.add(scroll);


        // add header label to topPanel
        topPanel.add(header);
        // add panels to frame
        frame.add(topPanel);
        frame.add(subPanel1);
        frame.add(subPanel2);
        //add buttons to subPanel
        subPanel1.add(addBtn);
        subPanel1.add(editBtn);
        subPanel1.add(deleteBtn);
        subPanel1.add(doneBtn);
        //add comboBoxes to subPanel1
        subPanel1.add(courseBox);
        subPanel1.add(typeTaskBox);

        frame.setVisible(true);
    }
}