package view;

import interface_adapter.tasks.dto.TaskDTO;
import interface_adapter.Dashboard.DashboardViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomePanel extends JPanel implements PropertyChangeListener {

    private final JLabel welcomeLabel;
    private final JPanel mainContentPanel;
    private final DashboardViewModel dashboardViewModel;
    private final List<JPanel> taskInfoPanels;
    private final JPanel infoContainerPanel;

    private final Color BG_BLACK = Color.decode("#000000");
    private final Color PANEL_DARK = Color.decode("#020F28");
    private final Color TEXT_LIGHT = Color.decode("#E6E6E6");
    private final Color ACCENT_COLOR = Color.decode("#007bff");

    public HomePanel(DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
        this.dashboardViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBackground(BG_BLACK);

        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(PANEL_DARK.brighter());
        welcomeLabel = new JLabel("Welcome Home! (Loading username...)");
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        welcomeLabel.setForeground(TEXT_LIGHT);
        welcomePanel.add(welcomeLabel);
        welcomePanel.setPreferredSize(new Dimension(0, 120));

        mainContentPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        mainContentPanel.setBackground(BG_BLACK);

        this.add(welcomePanel, BorderLayout.NORTH);
        this.add(mainContentPanel, BorderLayout.CENTER);

        this.infoContainerPanel = createDueSoonPanel();
        JPanel section2Wrapper = createSection2Wrapper(this.infoContainerPanel);
        JPanel section3 = createPlaceholderPanel();

        mainContentPanel.add(section2Wrapper);
        mainContentPanel.add(createSection3Wrapper(section3));

        this.taskInfoPanels = Arrays.stream(this.infoContainerPanel.getComponents())
                .filter(c -> c instanceof JPanel)
                .map(c -> (JPanel) c)
                .collect(Collectors.toList());

        updateTaskPanels(dashboardViewModel.getDueSoonTasks());
    }

    private JPanel createDueSoonPanel() {
        JPanel container = new JPanel();
        container.setBackground(PANEL_DARK);
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel infoContainer = new JPanel(new GridLayout(1, 3, 15, 0));
        infoContainer.setOpaque(false);
        infoContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        for (int i = 0; i < 3; i++) {
            JPanel infoPanel = createEmptyTaskPanel("Empty Slot");
            infoContainer.add(infoPanel);
        }

        container.add(infoContainer);
        container.add(Box.createHorizontalGlue());
        return infoContainer;
    }

    private JPanel createSection2Wrapper(JPanel section2) {
        JPanel wrapper2 = new JPanel(new BorderLayout());
        wrapper2.setBackground(BG_BLACK);
        wrapper2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel mainSection2 = new JPanel(new BorderLayout());
        mainSection2.setOpaque(false);

        JLabel titleLabel = new JLabel("Due Soon", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_LIGHT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleLabel.setBackground(PANEL_DARK);
        titleLabel.setOpaque(true);

        mainSection2.add(titleLabel, BorderLayout.NORTH);

        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(PANEL_DARK);
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentArea.add(section2, BorderLayout.CENTER);

        mainSection2.add(contentArea, BorderLayout.CENTER);
        wrapper2.add(mainSection2, BorderLayout.CENTER);

        return wrapper2;
    }

    private JPanel createSection3Wrapper(JPanel section3) {
        JPanel wrapper3 = new JPanel(new BorderLayout());
        wrapper3.setBackground(BG_BLACK);
        wrapper3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        wrapper3.add(section3, BorderLayout.CENTER);
        return wrapper3;
    }

    private JPanel createEmptyTaskPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(PANEL_DARK.brighter().brighter());
        panel.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2, true));

        JLabel title = new JLabel(labelText, SwingConstants.CENTER);
        title.setForeground(TEXT_LIGHT);
        title.setFont(new Font("Georgia", Font.BOLD, 16));

        panel.add(title, BorderLayout.NORTH);
        return panel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("dueSoonTasks".equals(evt.getPropertyName())) {
            @SuppressWarnings("unchecked")
            List<TaskDTO> tasks = (List<TaskDTO>) evt.getNewValue();
            updateTaskPanels(tasks);
        }
    }

    private void updateTaskPanels(List<TaskDTO> tasks) {
        if (tasks == null) tasks = List.of();

        for (int i = 0; i < taskInfoPanels.size(); i++) {
            JPanel panel = taskInfoPanels.get(i);
            panel.removeAll();

            if (i < tasks.size()) {
                TaskDTO task = tasks.get(i);

                panel.setLayout(new GridBagLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                panel.setBackground(PANEL_DARK.brighter());

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(4, 6, 4, 6);
                gbc.anchor = GridBagConstraints.WEST;

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JLabel dueTopLabel = new JLabel(task.getDueInText(), SwingConstants.CENTER);
                dueTopLabel.setFont(new Font("Georgia", Font.BOLD, 24));
                dueTopLabel.setForeground(ACCENT_COLOR);
                dueTopLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(dueTopLabel, gbc);

                gbc.gridy++;
                gbc.gridx = 0;
                gbc.gridwidth = 1;
                gbc.fill = GridBagConstraints.NONE;

                JLabel nameLabel = new JLabel("Task Name:");
                nameLabel.setForeground(TEXT_LIGHT);
                nameLabel.setFont(new Font("Georgia", Font.BOLD, 16));
                panel.add(nameLabel, gbc);

                gbc.gridy++;
                JLabel courseLabel = new JLabel("Course:");
                courseLabel.setForeground(TEXT_LIGHT);
                courseLabel.setFont(new Font("Georgia", Font.BOLD, 16));
                panel.add(courseLabel, gbc);

                gbc.gridy++;
                JLabel descLabel = new JLabel("Description:");
                descLabel.setForeground(TEXT_LIGHT);
                descLabel.setFont(new Font("Georgia", Font.BOLD, 16));
                panel.add(descLabel, gbc);

                gbc.gridy++;
                JLabel dueDateLabel = new JLabel("Due Date:");
                dueDateLabel.setForeground(TEXT_LIGHT);
                dueDateLabel.setFont(new Font("Georgia", Font.BOLD, 16));
                panel.add(dueDateLabel, gbc);

                gbc.gridx = 1;
                gbc.gridy = 1;
                JLabel nameValue = new JLabel(task.getTaskName());
                nameValue.setForeground(TEXT_LIGHT);
                nameValue.setFont(new Font("Georgia", Font.PLAIN, 16));
                panel.add(nameValue, gbc);

                gbc.gridy++;
                JLabel courseValue = new JLabel(task.getCourse());
                courseValue.setForeground(ACCENT_COLOR);
                courseValue.setFont(new Font("Georgia", Font.PLAIN, 16));
                panel.add(courseValue, gbc);

                gbc.gridy++;
                JLabel descValue = new JLabel(task.getDescription() != null ? task.getDescription() : "-");
                descValue.setForeground(TEXT_LIGHT);
                descValue.setFont(new Font("Georgia", Font.PLAIN, 16));
                panel.add(descValue, gbc);

                gbc.gridy++;
                JLabel dueDateValue = new JLabel(task.getFormattedDueDate());
                dueDateValue.setForeground(TEXT_LIGHT);
                dueDateValue.setFont(new Font("Georgia", Font.PLAIN, 16));
                panel.add(dueDateValue, gbc);

            } else {
                panel.setLayout(new GridBagLayout());
                panel.setBorder(null);
                panel.setBackground(PANEL_DARK);

                JLabel emptyLabel = new JLabel("No more tasks due soon", SwingConstants.CENTER);
                emptyLabel.setForeground(TEXT_LIGHT.darker());
                emptyLabel.setFont(new Font("Georgia", Font.PLAIN, 16));
                panel.add(emptyLabel);
            }

            panel.revalidate();
            panel.repaint();
        }
    }

    public void setUsername(String username) {
        this.welcomeLabel.setText("Welcome, " + username + "!");
    }

    private JPanel createPlaceholderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_DARK);

        JLabel placeholderLabel = new JLabel("Stopwatch", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        placeholderLabel.setForeground(TEXT_LIGHT);
        panel.add(placeholderLabel, BorderLayout.NORTH);

        JLabel stopwatchLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        stopwatchLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        stopwatchLabel.setForeground(ACCENT_COLOR);
        panel.add(stopwatchLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PANEL_DARK);
        JButton startBtn = new JButton("Start");
        JButton stopBtn = new JButton("Stop");
        JButton resetBtn = new JButton("Reset");

        buttonPanel.add(startBtn);
        buttonPanel.add(stopBtn);
        buttonPanel.add(resetBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        final int[] elapsedSeconds = {0};
        javax.swing.Timer stopwatchTimer = new javax.swing.Timer(1000, e -> {
            elapsedSeconds[0]++;
            int hours = elapsedSeconds[0] / 3600;
            int minutes = (elapsedSeconds[0] % 3600) / 60;
            int seconds = elapsedSeconds[0] % 60;
            stopwatchLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        });

        startBtn.addActionListener(e -> stopwatchTimer.start());
        stopBtn.addActionListener(e -> stopwatchTimer.stop());
        resetBtn.addActionListener(e -> {
            stopwatchTimer.stop();
            elapsedSeconds[0] = 0;
            stopwatchLabel.setText("00:00:00");
        });

        return panel;
    }
}