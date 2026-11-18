package gui;

import services.*;

import gui.themes.Colors;
import repository.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.*;

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

        // -- DATA TABLE SETUP --
        // These are the column headers. The initial row count is 0 because the rows
        // come from the CSV file instead of being typed in here.
        String[] columnName = { "Task", "Course", "Deadline", "Difficulty", "Type", "Priority" };
        DefaultTableModel tableModel = new DefaultTableModel(columnName, 0);

        // Load saved tasks when the app opens. loadTasks() returns an empty list if
        // the CSV does not exist yet, so nothing special is needed on first launch.
        for (String[] row : StudentRepository.loadTasks()) {
            tableModel.addRow(row);
        }

        // The JTable is the part that actually displays the model. Without this line
        // the scroll pane would be empty and no tasks would be visible.
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(4, 124, 992, 370);

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

        // -- CRUD BUTTONS --
        JButton addBtn = new JButton("Add Task");
        addBtn.setBounds(20, 10, 100, 100);
        JButton editBtn = new JButton("Edit");
        editBtn.setBounds(30, 10, 100, 100);
        editBtn.setFont(inputFont);
        // When a task is added: show it in the table AND save it to the CSV.
        // appendTask() returns false if the save failed; no exception reaches this code.
        addBtn.addActionListener(e -> TaskFormDialog.showAdd(frame, row -> {
            tableModel.addRow(row);
            if (!StudentRepository.appendTask(row)) {
                JOptionPane.showMessageDialog(frame, "Could not save task.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }));

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

        // add header label to topPanel
        topPanel.add(header);

        // add panels to frame
        frame.add(topPanel);
        frame.add(subPanel1);
        frame.add(subPanel2);
        frame.add(scroll);

        frame.setVisible(true);
    }
}