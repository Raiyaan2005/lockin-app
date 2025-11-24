package view;

import entity.Task;
import interface_adapter.Dashboard.DashboardViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TasksPanel extends JPanel {

    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JButton addTaskBtn;
    private List<Task> allTasks = new ArrayList<>();

    private final DashboardViewModel dashboardViewModel;

    public static interface_adapter.sync_task.SyncTaskToCalendarController syncController;

    public TasksPanel(DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;

        Color panelDark = Color.decode("#020F28");
        Color tableBackground = Color.decode("#001F3F");
        Color textLight = Color.decode("#E6E6E6");

        setLayout(new BorderLayout());
        setBackground(panelDark);

        JLabel title = new JLabel("Task Manager", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setForeground(textLight);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

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

        taskTable.setBackground(tableBackground);
        taskTable.setForeground(textLight);
        taskTable.setFont(new Font("Georgia", Font.PLAIN, 14));
        taskTable.setGridColor(panelDark);

        taskTable.getTableHeader().setFont(new Font("Georgia", Font.BOLD, 15));
        taskTable.getTableHeader().setBackground(panelDark);
        taskTable.getTableHeader().setForeground(textLight);
        taskTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.getViewport().setBackground(panelDark);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        addTaskBtn = new JButton("Add Task");
        addTaskBtn.setFont(new Font("Georgia", Font.BOLD, 16));
        addTaskBtn.setForeground(tableBackground);
        addTaskBtn.setBackground(new Color(0x003366));
        addTaskBtn.setFocusPainted(false);

        JButton sortByDateBtn = new JButton("Sort by Date");
        sortByDateBtn.setFont(new Font("Georgia", Font.BOLD, 16));
        sortByDateBtn.setForeground(tableBackground);
        sortByDateBtn.setBackground(new Color(0x003366));

        JButton sortByCourseBtn = new JButton("Sort by Course");
        sortByCourseBtn.setFont(new Font("Georgia", Font.BOLD, 16));
        sortByCourseBtn.setForeground(tableBackground);
        sortByCourseBtn.setBackground(new Color(0x003366));

        JPanel btnWrapper = new JPanel();
        btnWrapper.setBackground(panelDark);
        btnWrapper.add(addTaskBtn);
        btnWrapper.add(sortByDateBtn);
        btnWrapper.add(sortByCourseBtn);

        add(btnWrapper, BorderLayout.SOUTH);

        addTaskBtn.addActionListener(e -> openAddTaskPopup());

        sortByDateBtn.addActionListener(e -> {
            allTasks.sort(Comparator.comparing(Task::getDate));
            refreshTable();
        });

        sortByCourseBtn.addActionListener(e -> {
            allTasks.sort(Comparator.comparing(
                    Task::getCourse,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
            ));
            refreshTable();
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Task t : allTasks) {
            tableModel.addRow(new Object[]{
                    t.getTitle(),
                    t.getCourse(),
                    t.getDate(),
                    t.isCompleted() ? "Done" : "Not started"
            });
        }
    }

    private void openAddTaskPopup() {
        JDialog popup = new JDialog((Frame) null, "Add New Task", true);
        popup.setSize(420, 450);
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

        JLabel titleLabel = new JLabel("Task Name:");
        titleLabel.setForeground(textLight);
        titleLabel.setFont(labelFont);
        JTextField titleField = new JTextField();
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        titleField.setBackground(fieldDark);
        titleField.setForeground(textLight);
        titleField.setFont(fieldFont);

        JLabel courseLabel = new JLabel("Course:");
        courseLabel.setForeground(textLight);
        courseLabel.setFont(labelFont);
        JTextField courseField = new JTextField();
        courseField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        courseField.setBackground(fieldDark);
        courseField.setForeground(textLight);
        courseField.setFont(fieldFont);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setForeground(textLight);
        descLabel.setFont(labelFont);
        JTextArea descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(fieldDark);
        descArea.setForeground(textLight);
        descArea.setFont(fieldFont);
        JScrollPane descScroll = new JScrollPane(descArea);

        JLabel dateLabel = new JLabel("Due Date (YYYY-MM-DD):");
        dateLabel.setForeground(textLight);
        dateLabel.setFont(labelFont);
        JTextField dateField = new JTextField();
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        dateField.setBackground(fieldDark);
        dateField.setForeground(textLight);
        dateField.setFont(fieldFont);

        JCheckBox completedCheck = new JCheckBox("Completed?");
        completedCheck.setFont(labelFont);
        completedCheck.setForeground(textLight);
        completedCheck.setBackground(panelDark);

        form.add(titleLabel); form.add(titleField); form.add(Box.createVerticalStrut(10));
        form.add(courseLabel); form.add(courseField); form.add(Box.createVerticalStrut(10));
        form.add(descLabel); form.add(descScroll); form.add(Box.createVerticalStrut(10));
        form.add(dateLabel); form.add(dateField); form.add(Box.createVerticalStrut(10));
        form.add(completedCheck);

        popup.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setBackground(panelDark);
        JButton saveBtn = new JButton("Save Task");
        JButton cancelBtn = new JButton("Cancel");
        saveBtn.setFont(new Font("Georgia", Font.BOLD, 14));
        cancelBtn.setFont(new Font("Georgia", Font.BOLD, 14));

        saveBtn.addActionListener(e -> {
            try {
                Task newTask = new Task(
                        (int)(Math.random() * 1_000_000),
                        titleField.getText(),
                        descArea.getText(),
                        LocalDate.parse(dateField.getText()), courseField.getText()
                );

                if (completedCheck.isSelected()) {
                    newTask.markCompleted();
                }

                allTasks.add(newTask);

                if (dashboardViewModel != null) {
                    dashboardViewModel.updateDueSoonTasks(allTasks);
                }

                if (syncController != null) {
                    syncController.sync(newTask);
                }

                refreshTable();
                popup.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(popup, "Invalid input. Please check fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> popup.dispose());
        buttons.add(saveBtn);
        buttons.add(cancelBtn);
        popup.add(buttons, BorderLayout.SOUTH);
        popup.setVisible(true);
    }
}
