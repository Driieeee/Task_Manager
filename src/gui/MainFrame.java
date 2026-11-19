package gui;

import services.*;

import exception.DuplicateTaskException;
import exception.TaskFileException;
import gui.themes.Colors;
import gui.themes.Fonts;
import repository.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.table.*;

import model.Task;
import model.enums.TaskType;

public class MainFrame {

    public static void main(String[] args) {

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
        courseBox.setFont(Fonts.inputFont);

        JComboBox<TaskType> typeTaskBox = new JComboBox<>(TaskType.values());
        typeTaskBox.setFont(Fonts.inputFont);

        // create label for panel
        JLabel header = new JLabel("Student Task Manager");
        header.setBounds(50, 0, 1000, 75);
        header.setFont(Fonts.headerFont);
        header.setForeground(Color.WHITE);

        // -- DATA TABLE SETUP --
        // These are the column headers. The initial row count is 0 because the rows
        // come from the CSV file instead of being typed in here.
        String[] columnName = { "Task", "Course", "Deadline", "Difficulty", "Type",
                                "Priority", "Status", "ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columnName, 0);

        // Load saved tasks. Warnings (missing file, skipped lines) are shown to the
        // user; a read failure is reported here so the app can still open empty.
        try {
            repository.TaskLoadResult loaded = TaskService.loadTasks();
            for (Task task : loaded.getTasks()) {
                tableModel.addRow(task.toCsvRow());
            }
            if (loaded.hasWarnings()) {
                JOptionPane.showMessageDialog(frame, String.join("\n", loaded.getWarnings()),
                        "Data Notice", JOptionPane.WARNING_MESSAGE);
            }
        } catch (TaskFileException e) {
            JOptionPane.showMessageDialog(frame,
                    "Could not load saved tasks (" + e.getMessage() + "). Starting empty.",
                    "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        // The JTable is the part that actually displays the model. Without this line
        // the scroll pane would be empty and no tasks would be visible.
        JTable table = new JTable(tableModel) {
            @Override 
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(4, 124, 992, 370);

        // customize table
        table.setFont(Fonts.inputFont);
        table.setRowHeight(28);
        table.getTableHeader().setFont(Fonts.labelFont);
        table.setAutoCreateRowSorter(true); // sort by priority

        // center numeric columns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(3).setCellRenderer(center);

        // Identity travels with each row but is not shown in the table.
        // The model keeps it so edit/delete operate on the same stored task.
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(7)); // ID

        // click to deselect
        MouseAdapter clearOnCLick = new MouseAdapter() {
            @Override 
            public void mousePressed(MouseEvent e){
                table.clearSelection();
                frame.getContentPane().requestFocusInWindow();
            }
        };

        scroll.getViewport().addMouseListener(clearOnCLick);
        frame.getContentPane().setFocusable(true);
        frame.getContentPane().addMouseListener(clearOnCLick);

        // -- CRUD BUTTONS --
        JButton addBtn = new JButton("Add Task");
        addBtn.setBounds(20, 10, 100, 100);
        addBtn.setFont(Fonts.inputFont);

        // Save first, then show the row, so the table and the file never disagree.
        // Checked exceptions from the service are handled here at the GUI boundary.
        addBtn.addActionListener(e -> TaskFormDialog.showAdd(frame, task -> {
            try {
                TaskService.addTask(task);
                tableModel.addRow(task.toCsvRow());
                if (task.isDueSoon()) {
                    JOptionPane.showMessageDialog(frame,
                            "\"" + task.getTaskName() + "\" is due within 1 day!",
                            "Deadline Warning", JOptionPane.WARNING_MESSAGE);
                }
                return true;
            } catch (DuplicateTaskException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(),
                        "Duplicate Task", JOptionPane.WARNING_MESSAGE);
            } catch (TaskFileException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }));

        JButton editBtn = new JButton("Edit");
        editBtn.setBounds(30, 10, 100, 100);
        editBtn.setFont(Fonts.inputFont);

        editBtn.addActionListener(e -> TaskFormDialog.showEdit(frame, table));

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(40, 10, 100, 100);
        deleteBtn.setFont(Fonts.inputFont);

        TaskFormDialog.showDelete(frame, table, deleteBtn);

        JButton doneBtn = new JButton("Mark as Done");
        doneBtn.setBounds(50, 10, 100, 100);
        doneBtn.setFont(Fonts.inputFont);
        doneBtn.setContentAreaFilled(false);
        doneBtn.setOpaque(true);
        doneBtn.setBackground(Colors.YELLOW);
        doneBtn.setBorderPainted(false);

        TaskFormDialog.showDone(frame, table, doneBtn);

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

        addBtn.setFocusable(false);
        editBtn.setFocusable(false);
        deleteBtn.setFocusable(false);
        doneBtn.setFocusable(false);

        frame.setVisible(true);
    }
}