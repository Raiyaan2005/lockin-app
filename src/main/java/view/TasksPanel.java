package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import entity.Task;
import java.time.LocalDate;

public class TasksPanel extends JPanel {

    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JButton addTaskBtn;

    public static interface_adapter.sync_task.SyncTaskToCalendarController syncController;

    public TasksPanel() {

        // Colours
        Color panelDark = Color.decode("#020F28");     // navy background
        Color tableBackground = Color.decode("#001F3F");
        Color textLight = Color.decode("#E6E6E6");

        setLayout(new BorderLayout());
        setBackground(panelDark);

        // Title
        JLabel title = new JLabel("Task Manager", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setForeground(textLight);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Task", "Course", "Due Date", "Status"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setFillsViewportHeight(true);
        taskTable.setRowHeight(30);

        // table styling
        taskTable.setBackground(tableBackground);
        taskTable.setForeground(textLight);
        taskTable.setFont(new Font("Georgia", Font.PLAIN, 14));
        taskTable.setGridColor(panelDark);

        // table header
        taskTable.getTableHeader().setFont(new Font("Georgia", Font.BOLD, 15));
        taskTable.getTableHeader().setBackground(panelDark);
        taskTable.getTableHeader().setForeground(textLight);
        taskTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.getViewport().setBackground(panelDark);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        // Add Task button
        addTaskBtn = new JButton("Add Task");
        addTaskBtn.setPreferredSize(new Dimension(120, 45));
        addTaskBtn.setFont(new Font("Georgia", Font.BOLD, 16));
        addTaskBtn.setForeground(tableBackground);
        addTaskBtn.setBackground(new Color(0x003366));
        addTaskBtn.setFocusPainted(false);

        JPanel btnWrapper = new JPanel();
        btnWrapper.setBackground(panelDark);
        btnWrapper.add(addTaskBtn);

        add(btnWrapper, BorderLayout.SOUTH);

        // Popup panel to add information
        addTaskBtn.addActionListener(e -> openAddTaskPopup());
    }

    private void openAddTaskPopup() {
        JDialog popup = new JDialog((Frame) null, "Add New Task", true);
        popup.setSize(420, 420);
        popup.setLocationRelativeTo(null);
        popup.setLayout(new BorderLayout());

        Color panelDark = Color.decode("#020F28");
        Color fieldDark = Color.decode("#001F3F");
        Color textLight = Color.decode("#E6E6E6");

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(panelDark);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Georgia", Font.PLAIN, 16);
        Font fieldFont = new Font("Georgia", Font.PLAIN, 14);

        // Title
        JLabel titleLabel = new JLabel("Task Name:");
        titleLabel.setForeground(textLight);
        titleLabel.setFont(labelFont);

        JTextField titleField = new JTextField();
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        titleField.setBackground(fieldDark);
        titleField.setForeground(textLight);
        titleField.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        titleField.setFont(fieldFont);

        // Description
        JLabel descLabel = new JLabel("Description:");
        descLabel.setForeground(textLight);
        descLabel.setFont(labelFont);

        JTextArea descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(fieldDark);
        descArea.setForeground(textLight);
        descArea.setFont(fieldFont);
        descArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane descScroll = new JScrollPane(descArea);

        // ---- DATE ----
        JLabel dateLabel = new JLabel("Due Date (YYYY-MM-DD):");
        dateLabel.setForeground(textLight);
        dateLabel.setFont(labelFont);

        JTextField dateField = new JTextField();
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        dateField.setBackground(fieldDark);
        dateField.setForeground(textLight);
        dateField.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        dateField.setFont(fieldFont);

        // ---- TYPE ----
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setForeground(panelDark);
        typeLabel.setFont(labelFont);

        String[] types = {"Assignment", "Test", "Event", "Reminder", "Other"};
        JComboBox<String> typeDropdown = new JComboBox<>(types);
        typeDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        typeDropdown.setFont(fieldFont);
        typeDropdown.setBackground(fieldDark);
        typeDropdown.setForeground(textLight);

        // ---- COMPLETED ----
        JCheckBox completedCheck = new JCheckBox("Completed?");
        completedCheck.setFont(labelFont);
        completedCheck.setForeground(textLight);
        completedCheck.setBackground(panelDark);

        // Add all fields in order
        form.add(titleLabel);
        form.add(titleField);
        form.add(Box.createVerticalStrut(10));
        form.add(descLabel);
        form.add(descScroll);
        form.add(Box.createVerticalStrut(10));
        form.add(dateLabel);
        form.add(dateField);
        form.add(Box.createVerticalStrut(10));
        form.add(typeLabel);
        form.add(typeDropdown);
        form.add(Box.createVerticalStrut(10));
        form.add(completedCheck);

        popup.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel();
        buttons.setBackground(panelDark);

        JButton saveBtn = new JButton("Save Task");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.setFont(new Font("Georgia", Font.BOLD, 14));
        cancelBtn.setFont(new Font("Georgia", Font.BOLD, 14));

        saveBtn.addActionListener(e -> {
            try {
                // Create Task object using your entity
                Task newTask = new Task(
                        (int) (Math.random() * 1000000),          // temporary ID
                        titleField.getText(),
                        descArea.getText(),
                        LocalDate.parse(dateField.getText()),
                        (String) typeDropdown.getSelectedItem()
                );

                if (completedCheck.isSelected()) {
                    newTask.markCompleted();
                }

                if (syncController != null) {
                    syncController.sync(newTask);
                }


                // Add row to table
                tableModel.addRow(new Object[]{
                        newTask.getTitle(),
                        newTask.getType(),
                        newTask.getDate(),
                        newTask.isCompleted() ? "Done" : "Not started"
                });

                popup.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        popup,
                        "Invalid date format or missing fields.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelBtn.addActionListener(e -> popup.dispose());

        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        popup.add(buttons, BorderLayout.SOUTH);
        popup.setVisible(true);
    }

}
