package view;

import data_access.InMemoryTasksDataAccess;
import interface_adapter.Dashboard.DashboardViewModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logout.LogoutController;
import interface_adapter.tasks.TasksController;
import interface_adapter.tasks.TasksPresenter;
import interface_adapter.tasks.TasksViewModel;
import use_case.tasks.TasksInteractor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardView extends JPanel {

    private static final String HOME_CARD = "Dashboard";
    private static final String TASK_MANAGER_CARD = "Task Manager";
    private static final String CALENDAR_CARD = "Calendar";

    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private final JLabel mainHeaderLabel;

    private JButton currentSelectedButton = null;

    private final HomePanel homePanel;
    private final TasksPanel tasksPanel;
    private final CalendarPanel calendarPanel;

    private final DashboardViewModel dashboardViewModel;

    private ChangePasswordController changePasswordController = null;
    private LogoutController logoutController = null;
    private TasksController tasksController;


    private final Color BG_BLACK = Color.decode("#000000");
    private final Color PANEL_DARK = Color.decode("#020F28");
    private final Color ACCENT_HOVER = Color.decode("#0047A3");
    private final Color TEXT_LIGHT = Color.decode("#E6E6E6");
    private final Color BUTTON_BASE = PANEL_DARK;

    /**
     * Updated constructor to accept the DashboardViewModel.
     * @param frame The main application JFrame.
     * @param dashboardViewModel The ViewModel containing 'due soon' task data.
     */

    public DashboardView(JFrame frame, DashboardViewModel dashboardViewModel) {
        this.frame = frame;
        this.dashboardViewModel = dashboardViewModel;
        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(cardLayout);

        // ---------- CLEAN ARCHITECTURE WIRING FOR TASKS ----------
        InMemoryTasksDataAccess tasksRepo = new InMemoryTasksDataAccess();
        TasksViewModel tasksViewModel = new TasksViewModel();
        TasksPresenter tasksPresenter = new TasksPresenter(tasksViewModel);
        TasksInteractor interactor = new TasksInteractor(tasksRepo, tasksPresenter);
        this.tasksController = new TasksController(interactor);

        // ---------- PANELS (keep your existing types) ----------
        this.homePanel = new HomePanel(this.dashboardViewModel);
        this.tasksPanel = new TasksPanel(this.dashboardViewModel, tasksRepo);
        this.calendarPanel = new CalendarPanel();

        this.mainHeaderLabel = new JLabel("LockIn Dashboard", SwingConstants.CENTER);

        initUI();
    }



    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        mainHeaderLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        mainHeaderLabel.setOpaque(true);
        mainHeaderLabel.setBackground(PANEL_DARK);
        mainHeaderLabel.setForeground(TEXT_LIGHT);
        mainHeaderLabel.setPreferredSize(new Dimension(0, 80));
        add(mainHeaderLabel, BorderLayout.NORTH);

        cardPanel.setBackground(BG_BLACK);

        cardPanel.add(homePanel, HOME_CARD);
        cardPanel.add(tasksPanel, TASK_MANAGER_CARD);
        cardPanel.add(calendarPanel, CALENDAR_CARD);

        add(cardPanel, BorderLayout.CENTER);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(4, 1, 0, 0));
        sidebar.setBackground(BG_BLACK);
        sidebar.setPreferredSize(new Dimension(220, 0));

        String[] buttons = {HOME_CARD, CALENDAR_CARD, TASK_MANAGER_CARD, "Logout"};

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Georgia", Font.PLAIN, 18));
            btn.setForeground(TEXT_LIGHT);
            btn.setBackground(BUTTON_BASE);
            btn.setOpaque(true);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            if (text.equals(HOME_CARD)) {
                btn.setBackground(ACCENT_HOVER);
                currentSelectedButton = btn;
            }

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (btn != currentSelectedButton) {
                        btn.setBackground(ACCENT_HOVER.brighter());
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (btn != currentSelectedButton) {
                        btn.setBackground(BUTTON_BASE);
                    }
                }
            });

            btn.addActionListener(e -> {
                if (currentSelectedButton != null) {
                    currentSelectedButton.setBackground(BUTTON_BASE);
                }
                currentSelectedButton = btn;
                btn.setBackground(ACCENT_HOVER);

                switch (text) {
                    case HOME_CARD:
                        cardLayout.show(cardPanel, HOME_CARD);
                        mainHeaderLabel.setText("LockIn Dashboard");
                        break;
                    case TASK_MANAGER_CARD:
                        if (tasksController != null) {
                            tasksController.loadTasks();   // ← this now calls TasksInteractor
                        }
                        cardLayout.show(cardPanel, TASK_MANAGER_CARD);
                        mainHeaderLabel.setText(TASK_MANAGER_CARD);
                        break;
                    case CALENDAR_CARD:
                        cardLayout.show(cardPanel, CALENDAR_CARD);
                        mainHeaderLabel.setText(CALENDAR_CARD);
                        break;
                    case "Logout":
                        if (logoutController != null) {
                            logoutController.execute();
                        } else {
                            // Reset selection if logout fails
                            currentSelectedButton.setBackground(BUTTON_BASE);
                            currentSelectedButton = null;
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "LogoutController not set.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                        break;
                }
            });

            JPanel buttonWrapper = new JPanel(new BorderLayout());
            buttonWrapper.setOpaque(false);
            buttonWrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            buttonWrapper.add(btn, BorderLayout.CENTER);
            sidebar.add(buttonWrapper);
        }

        add(sidebar, BorderLayout.WEST);

        cardLayout.show(cardPanel, HOME_CARD);
        this.setBackground(BG_BLACK);
    }


    /**
     * Called by LoggedInView to update the welcome message on the Home Panel.
     * @param username The username of the currently logged-in user.
     */
    public void updateUsername(String username) {
        this.homePanel.setUsername(username);
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public void setTasksController(TasksController tasksController) {
        this.tasksController = tasksController;
    }

}
