package gui;

import services.*;

import gui.themes.Colors;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainFrame {

    public static void main(String[] args) {

        // fonts
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font inputFont = new Font("Arial", Font.PLAIN, 14);

        // create JFrame
        JFrame frame = new JFrame();

        frame.setSize(1000, 630); 
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
        String[] courses = { "Discrete Structures", "Linear Algebra" };
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

        // -- DATA TABLE SETUP (Moved up so it's accessible by buttons) --
        String[] columnName = { "Task", "Course", "Deadline", "Difficulty", "Type", "Priority"};
        
        // Load initial data from repository (falls back safely if file doesn't exist)
        String[][] data = {
                  { "Okay test", "ADET", "10/20/22", "5", "Exam", "90" },
                  { "Not So Okay Test", "DS", "10/20/22", "9", "Exam", "99" },
                  { "", "", "", "", "", "" } // FIX: maintained 6 columns to prevent painting errors
       };

        DefaultTableModel tableModel = new DefaultTableModel(data, columnName);
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(4, 154, 992, 342);
        frame.add(scroll);

        // -- CRUD BUTTONS --
        JButton addBtn = new JButton("Add Task");
        addBtn.setBounds(20, 10, 100, 100);
        addBtn.addActionListener(e -> {
            TaskFormDialog.show(frame, () -> {
            });
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

        // add header label to topPanel
        topPanel.add(header);
        
        // add panels to frame
        frame.add(topPanel);
        frame.add(subPanel1);
        frame.add(subPanel2);
        
        // add buttons to subPanel1
        subPanel1.add(addBtn);
        subPanel1.add(editBtn);
        subPanel1.add(deleteBtn);
        subPanel1.add(doneBtn);
        
        // add comboBoxes to subPanel1
        subPanel1.add(courseBox);
        subPanel1.add(typeTaskBox);

        frame.setVisible(true);
    }
}