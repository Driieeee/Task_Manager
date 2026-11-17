package gui;

import gui.themes.Colors;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.table.*;
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
        topPanel.setBounds(0, 0, 1000, 70);
        topPanel.setBackground(Colors.DARK_BLUE);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        topPanel.setLayout(null);

        // create subPanels
        JPanel subPanel1 = new JPanel();
        subPanel1.setBounds(0, 70, 1000, 50);
        subPanel1.setBackground(Colors.LIGHT_BLUE);
        subPanel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        subPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel subPanel2 = new JPanel();
        subPanel2.setBounds(0, 500, 1000, 105);
        subPanel2.setBackground(Colors.MED_DARK_BLUE);
        subPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // create comboBoxes
        String[] courses = { "Discrete Structures", "Linear Algebra" };
        JComboBox<String> courseBox = new JComboBox<>(courses);
        courseBox.setFont(inputFont);

        String[] typeTask = { "Exam", "Assignment", "Project", "Quiz" };
        JComboBox<String> typeTaskBox = new JComboBox<>(typeTask);
        typeTaskBox.setFont(inputFont);

        // create label for panel
        JLabel header = new JLabel("Student Task Manager");
        header.setBounds(50, 0, 1000, 75);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(Color.WHITE);

        // --CRUD BUTTONS--
        JButton addBtn = new JButton("Add Task");
        addBtn.setBounds(20, 10, 100, 100);
        addBtn.setFont(inputFont);

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
            String[] difficulties = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
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
        editBtn.setFont(inputFont);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(40, 10, 100, 100);
        deleteBtn.setFont(inputFont);

        deleteBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this task?",
                    "Confirm Action", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame, "Task Deleted");
            }
        });

        JButton doneBtn = new JButton("Mark as Done");
        doneBtn.setBounds(50, 10, 100, 100);
        doneBtn.setFont(inputFont);
        doneBtn.setContentAreaFilled(false);
        doneBtn.setOpaque(true);
        doneBtn.setBackground(Colors.YELLOW);
        doneBtn.setBorderPainted(false);

        //position buttons in subPanel1
        subPanel1.setLayout(new BorderLayout());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        left.setOpaque(false);
        left.add(addBtn);
        left.add(editBtn);
        left.add(deleteBtn);
        left.add(doneBtn);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        right.setOpaque(false);
        right.add(courseBox);
        right.add(typeTaskBox);

        subPanel1.add(left, BorderLayout.WEST);
        subPanel1.add(right, BorderLayout.EAST);

        // Data table
        String[] columnName = { "Task", "Course", "Deadline", "Difficulty", "Type", "Priority" };
        String[][] data = {
                { "Okay test", "ADET", "10/20/22", "5", "Exam", "90" },
                { "Not So Okay Test", "DS", "10/20/22", "9", "Exam", "99" },
                { "", "", "", "", "", "" } // FIX: had 5 values for 6 columns, which throws an error while painting
        };
        JTable table = new JTable(data, columnName);
        JScrollPane scroll = new JScrollPane(table);

        // customize table
        table.setFont(inputFont);
        table.setRowHeight(28);
        table.getTableHeader().setFont(labelFont);
        table.setAutoCreateRowSorter(true); // sort by priority

        // center numeric columns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(3).setCellRenderer(center);
        table.getColumnModel().getColumn(5).setCellRenderer(center);

        scroll.setBounds(4, 124, 992, 370);
        frame.add(scroll);

        // add header label to topPanel
        topPanel.add(header);
        // add panels to frame
        frame.add(topPanel);
        frame.add(subPanel1);
        frame.add(subPanel2);

        frame.setVisible(true);
    }
}