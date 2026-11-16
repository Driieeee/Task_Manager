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
        header.setBounds(10, 10, 300, 100);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(Color.WHITE);
        //add header label to topPanel
        topPanel.add(header);
        //add panels to frame
        frame.add(topPanel);
        frame.add(subPanel1);
        frame.add(subPanel2);

        frame.setVisible(true);
        

    }

}
