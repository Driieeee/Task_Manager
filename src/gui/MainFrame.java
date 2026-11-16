package gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;


public class MainFrame {
    
    public static void main(String[] args) {
        //create JFrame
        JFrame frame = new JFrame();
        frame.setSize(600, 600);
        frame.setBounds(100, 600, 600, 600);
        frame.setTitle("DueSmart");
        frame. setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create topPanel
        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, 600, 100);
        topPanel.setBackground(Color.BLUE);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        topPanel.setLayout(null);

        //create subPanels
        JPanel subPanel1 = new JPanel();
        subPanel1.setBounds(0, 100, 600, 50);
        subPanel1.setBackground(Color.YELLOW);
        subPanel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel subPanel2 = new JPanel();
        subPanel2.setBounds(0, 150, 600, 50);
        subPanel2.setBackground(Color.GREEN);
        subPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //create label for panel
        JLabel header = new JLabel("Student Task Manager");
        header.setBounds(30, 0, 300, 100);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(Color.WHITE);
        
        //--CRUD BUTTONS--
        JButton addBtn = new JButton("Add Task");
        addBtn.setBounds(10, 0, 100, 100);
        
        addBtn.addActionListener(e -> {
            String taskName = JOptionPane.showInputDialog(addBtn, "Enter task");
            
            if(taskName != null && !taskName.trim().isEmpty()){
                JOptionPane.showMessageDialog(addBtn, "Successfully Added: " + taskName);
            }
        });
        

        JButton editBtn = new JButton("Edit");
        editBtn.setBounds(20, 0, 100, 100);
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(30, 0, 100, 100);
        JButton doneBtn = new JButton("Mark as Done");
        doneBtn.setBounds(40, 0, 100, 100);

        // Data table
        String[] columnName = {"Course", "Deadline", "Difficulty", "Type", "Priority"};
        String [][] data = {
            {"ADET", "10/20/22", "5", "Exam", "90"},
            {"", "", "", "", ""}
        };
        JTable table = new JTable(data, columnName);
        JScrollPane scroll = new JScrollPane(table);

        scroll.setBounds(0, 220, 585, 360);
        frame.add(scroll);

        frame.add(scroll);

        //add header label to topPanel
        topPanel.add(header);
        //add panels to frame
        frame.add(topPanel);
        frame.add(subPanel1);
        frame.add(subPanel2);

        subPanel1.add(addBtn);
        subPanel1.add(editBtn);
        subPanel1.add(deleteBtn);
        subPanel1.add(doneBtn);

        frame.setVisible(true);

    }

}
