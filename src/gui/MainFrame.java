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

        String[] typeTask = {"Assignment", "Exam", "Laboratory Exercise", "Long Quiz", "Project", "Quiz", "Summative", "Other" };
        JComboBox<String> typeTaskBox = new JComboBox<>(typeTask);
        typeTaskBox.setFont(inputFont);

        // create label for panel
        JLabel header = new JLabel("Student Task Manager");
        header.setBounds(50, 0, 1000, 75);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(Color.WHITE);

        // -- DATA TABLE SETUP (Using hardcoded values, loadTasks removed) --
        String[] columnName = { "Task", "Course", "Deadline", "Difficulty", "Type", "Priority"};
        
        String[][] data = {
            { "Okay test", "ADET", "10/20/22", "5", "Exam", "90" },
            { "Not So Okay Test", "DS", "10/20/22", "9", "Exam", "99" },
            { "", "", "", "", "", "" }
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columnName);
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(4, 154, 992, 342);
        frame.add(scroll);

        // -- CRUD BUTTONS --
        JButton addBtn = new JButton("Add Task");
        addBtn.setBounds(20, 10, 100, 100);
        addBtn.addActionListener(e -> TaskFormDialog.showAdd(frame, row -> tableModel.addRow(row)));

        JButton editBtn = new JButton("Edit");
        editBtn.setBounds(30, 10, 100, 100);
        editBtn.setFont(inputFont);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(40, 10, 100, 100);
        deleteBtn.setFont(inputFont);

        TaskFormDialog.showDelete(frame, table, deleteBtn);

        JButton doneBtn = new JButton("Mark as Done");
        doneBtn.setBounds(50, 10, 100, 100);
        doneBtn.setFont(inputFont);
        doneBtn.setContentAreaFilled(false);
        doneBtn.setOpaque(true);
        doneBtn.setBackground(Colors.YELLOW);
        doneBtn.setBorderPainted(false);

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