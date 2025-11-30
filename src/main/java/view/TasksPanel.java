package view;

import dataaccess.InMemoryTasksDataAccess;
import entity.Task;
import interfaceadapter.dashboard.DashboardViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class TasksPanel extends JPanel {

    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JButton addTaskBtn, deleteTaskBtn;

    private final InMemoryTasksDataAccess tasksDataAccess;
    private List<Task> allTasks;

    private final DashboardViewModel dashboardViewModel;
    public static interfaceadapter.sync_task.SyncTaskToCalendarController syncController;

    // ⭐ UPDATED — now requires repository so UI and use case share data
    public TasksPanel(DashboardViewModel dashboardViewModel,
                      InMemoryTasksDataAccess tasksDataAccess) {

        this.dashboardViewModel = dashboardViewModel;
        this.tasksDataAccess = tasksDataAccess;

        // ⭐ Use shared task list (NOT new ArrayList)
        this.allTasks = tasksDataAccess.getAllTasks();

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

        deleteTaskBtn = new JButton("Delete Task");
        deleteTaskBtn.setFont(new Font("Georgia", Font.BOLD, 16));
        deleteTaskBtn.setForeground(tableBackground);
        deleteTaskBtn.setBackground(new Color(0x660000));

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
        btnWrapper.add(deleteTaskBtn);
        btnWrapper.add(sortByDateBtn);
        btnWrapper.add(sortByCourseBtn);

        add(btnWrapper, BorderLayout.SOUTH);

        // ACTIONS
        addTaskBtn.addActionListener(e -> openTaskPopup(null));
        deleteTaskBtn.addActionListener(e -> deleteSelectedTask());

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

        taskTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && taskTable.getSelectedRow() != -1) {
                    openTaskPopup(allTasks.get(taskTable.getSelectedRow()));
                }
            }
        });

        refreshTable();
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

    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task first.");
            return;
        }

        Task task = allTasks.get(selectedRow);
        tasksDataAccess.removeTask(task);

        refreshTable();
    }

    private void openTaskPopup(Task taskToEdit) {
        boolean editing = (taskToEdit != null);

        JDialog popup = new JDialog((Frame) null, editing ? "Edit Task" : "Add New Task", true);
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
        completedCheck.setForeground(textLight);
        completedCheck.setBackground(panelDark);
        completedCheck.setFont(labelFont);

        if (editing) {
            titleField.setText(taskToEdit.getTitle());
            courseField.setText(taskToEdit.getCourse());
            descArea.setText(taskToEdit.getDescription());
            dateField.setText(taskToEdit.getDate().toString());
            completedCheck.setSelected(taskToEdit.isCompleted());
        }

        form.add(titleLabel); form.add(titleField); form.add(Box.createVerticalStrut(10));
        form.add(courseLabel); form.add(courseField); form.add(Box.createVerticalStrut(10));
        form.add(descLabel); form.add(descScroll); form.add(Box.createVerticalStrut(10));
        form.add(dateLabel); form.add(dateField); form.add(Box.createVerticalStrut(10));
        form.add(completedCheck);

        popup.add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton(editing ? "Save Changes" : "Save Task");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.setFont(new Font("Georgia", Font.BOLD, 14));
        cancelBtn.setFont(new Font("Georgia", Font.BOLD, 14));

        JPanel buttons = new JPanel();
        buttons.setBackground(panelDark);
        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        popup.add(buttons, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            try {
                if (editing) {
                    // ---- EDIT EXISTING TASK ----
                    taskToEdit.setTitle(titleField.getText());
                    taskToEdit.setCourse(courseField.getText());
                    taskToEdit.setDescription(descArea.getText());
                    taskToEdit.setDate(LocalDate.parse(dateField.getText()));
                    taskToEdit.setCompleted(completedCheck.isSelected());

                    tasksDataAccess.updateTask(taskToEdit);

                } else {
                    // ---- CREATE NEW TASK ----
                    Task newTask = new Task(
                            (int)(Math.random() * 1_000_000),
                            titleField.getText(),
                            descArea.getText(),
                            LocalDate.parse(dateField.getText()),
                            courseField.getText()
                    );

                    newTask.setCompleted(completedCheck.isSelected());
                    tasksDataAccess.addTask(newTask);
                }

                // Refresh visible list after edit/add
                allTasks = tasksDataAccess.getAllTasks();
                refreshTable();

                popup.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        popup,
                        "Invalid input — please check fields.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        cancelBtn.addActionListener(e -> popup.dispose());
        popup.setVisible(true);
    }

}
