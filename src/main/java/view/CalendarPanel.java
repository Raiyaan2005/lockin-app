package view;

import entity.Event;
import interface_adapter.calendar.CalendarController;
import interface_adapter.calendar.CalendarViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.*;
import java.util.List;

public class CalendarPanel extends JPanel implements PropertyChangeListener {

    // These are set from Main before the DashboardView is created
    public static CalendarViewModel sharedViewModel;
    public static CalendarController sharedCalendarController;

    private final CalendarController controller;
    private final CalendarViewModel viewModel;

    private final JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
    private final JPanel grid = new JPanel(new GridLayout(7, 7, 1, 1));
    private YearMonth current = YearMonth.now();

    public CalendarPanel() {
        this(sharedCalendarController, sharedViewModel);
    }

    public CalendarPanel(CalendarController controller, CalendarViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        if (this.viewModel != null) {
            this.viewModel.addPropertyChangeListener(this);
        }

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(16, 16, 16, 16));

        JButton addEventBtn = new JButton("Add Event");
        addEventBtn.addActionListener(this::openAddDialog);

        JButton prev = new JButton("<");
        JButton next = new JButton(">");

        prev.addActionListener(e -> {
            current = current.minusMonths(1);
            refresh();
        });

        next.addActionListener(e -> {
            current = current.plusMonths(1);
            refresh();
        });

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.add(addEventBtn);

        monthLabel.setFont(monthLabel.getFont().deriveFont(Font.BOLD, 18f));

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.add(monthLabel);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.add(prev);
        right.add(next);

        JPanel top = new JPanel(new BorderLayout());
        top.add(left, BorderLayout.WEST);
        top.add(center, BorderLayout.CENTER);
        top.add(right, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);

        controller.viewCalendar();

        refresh();
    }

    private void openAddDialog(ActionEvent e) {
        if (controller == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Calendar is not wired correctly (controller is null). " +
                            "Make sure Main creates a real CalendarController and assigns\n" +
                            "CalendarPanel.sharedCalendarController before creating DashboardView.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Add Event", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        dlg.add(new JLabel("Event name:"), c);
        c.gridx = 1;
        JTextField nameField = new JTextField(16);
        dlg.add(nameField, c);

        c.gridx = 0;
        c.gridy++;
        dlg.add(new JLabel("Date (YYYY-MM-DD):"), c);
        c.gridx = 1;
        JTextField dateField = new JTextField(current.atDay(1).toString(), 16);
        dlg.add(dateField, c);

        c.gridx = 0;
        c.gridy++;
        dlg.add(new JLabel("Colour:"), c);

        c.gridx = 1;
        // Use your existing ColorSwatchPicker class
        ColorSwatchPicker colorPicker = new ColorSwatchPicker(Color.BLUE);
        dlg.add(colorPicker, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton cancel = new JButton("Cancel");
        JButton add = new JButton("Add");
        btnRow.add(cancel);
        btnRow.add(add);
        dlg.add(btnRow, c);

        cancel.addActionListener(ev -> dlg.dispose());

        add.addActionListener(ev -> {
            try {
                String name = nameField.getText().trim();
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                Color color = colorPicker.getSelectedColor();
                controller.addEvent(name, date, color);
                dlg.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dlg,
                        "Invalid input: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void refresh() {
        String monthName = current.getMonth().toString();
        monthName = monthName.substring(0, 1) + monthName.substring(1).toLowerCase();
        monthLabel.setText(monthName + " " + current.getYear());

        grid.removeAll();

        DayOfWeek[] order = {
                DayOfWeek.SUNDAY,
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
        };

        for (DayOfWeek d : order) {
            JLabel l = new JLabel(d.name().substring(0, 3), SwingConstants.CENTER);
            l.setOpaque(true);
            l.setBackground(new Color(245, 245, 245));
            l.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            grid.add(l);
        }

        LocalDate first = current.atDay(1);
        int startCol = first.getDayOfWeek().getValue() % 7;
        int days = current.lengthOfMonth();

        for (int i = 0; i < 42; i++) {
            if (i < startCol || i >= startCol + days) {
                grid.add(dayCell("", null));
            } else {
                int d = i - startCol + 1;
                LocalDate date = current.atDay(d);

                List<Event> events = null;
                if (viewModel != null) {
                    events = viewModel.eventsOn(date);
                }

                grid.add(dayCell(String.valueOf(d), events));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel dayCell(String dayText, List<Event> events) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel day = new JLabel(dayText);
        day.setBorder(new EmptyBorder(4, 6, 0, 0));
        p.add(day, BorderLayout.NORTH);

        if (events != null && !events.isEmpty()) {
            JPanel list = new JPanel();
            list.setOpaque(false);
            list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
            for (Event e : events) {
                JLabel l = new JLabel("• " + e.getName());
                l.setForeground(e.getColor());
                l.setFont(l.getFont().deriveFont(15f));
                list.add(l);
            }
            p.add(list, BorderLayout.CENTER);
        }
        return p;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (CalendarViewModel.EVENTS_CHANGED.equals(evt.getPropertyName())) {
            refresh();
        }
    }
}
