package view;

import interfaceadapter.login.LoginController;
import interfaceadapter.login.LoginState;
import interfaceadapter.login.LoginViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private final JTextField usernameInputField = new JTextField(20);
    private final JLabel usernameErrorField = new JLabel(" ");
    private final JPasswordField passwordInputField = new JPasswordField(20);

    private final JButton logIn;
    private final JButton cancel;
    private final JButton toSignup;

    private LoginController loginController = null;

    private static final Color LEFT_TOP    = new Color(0x1E40AF);
    private static final Color LEFT_BOT    = new Color(0x312E81);
    private static final Color LEFT_CIRCLE = new Color(255, 255, 255, 22);
    private static final Color SURFACE     = Color.WHITE;
    private static final Color FIELD       = new Color(0xF8FAFC);
    private static final Color ACCENT      = new Color(0x3B82F6);
    private static final Color ACCENT_HI   = new Color(0x2563EB);
    private static final Color TEXT        = new Color(0x1C1917);
    private static final Color LABEL_FG    = new Color(0x374151);
    private static final Color MUTED       = new Color(0x6B7280);
    private static final Color DANGER      = new Color(0xDC2626);
    private static final Color BORDER      = new Color(0xE2E8F0);
    private static final Color BLUE_LIGHT  = new Color(0xBFDBFE);
    private static final Color BLUE_XLIGHT = new Color(0xEFF6FF);
    private static final Color HEADING_FG  = new Color(0x1E3A8A);

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        logIn    = new JButton("Log In");
        toSignup = new JButton("Create an account →");
        cancel   = new JButton("Exit");

        setLayout(new BorderLayout());
        setBackground(LEFT_TOP);

        add(buildBrandPanel(), BorderLayout.WEST);
        add(buildFormPanel(),  BorderLayout.CENTER);

        wireListeners();
    }

    private JPanel buildBrandPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, LEFT_TOP, 0, getHeight(), LEFT_BOT));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(LEFT_CIRCLE);
                g2.fillOval(-70, -70, 280, 280);
                g2.fillOval(getWidth() - 120, getHeight() - 160, 240, 240);
                g2.fillOval(getWidth() / 2 - 50, getHeight() / 2 + 40, 120, 120);
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(LEFT_TOP);
        p.setPreferredSize(new Dimension(360, 540));

        p.add(Box.createVerticalGlue());

        JLabel logo = new JLabel("LockIn");
        logo.setFont(new Font("Copperplate", Font.BOLD, 52));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(logo);

        p.add(Box.createVerticalStrut(12));

        for (String line : new String[]{"Stay focused,", "get more done."}) {
            JLabel lbl = new JLabel(line);
            lbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
            lbl.setForeground(BLUE_LIGHT);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            p.add(lbl);
            p.add(Box.createVerticalStrut(3));
        }

        p.add(Box.createVerticalStrut(44));

        for (String feat : new String[]{"✓  Task management", "✓  Calendar view", "✓  Goal tracking"}) {
            JLabel lbl = new JLabel(feat);
            lbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
            lbl.setForeground(BLUE_XLIGHT);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            p.add(lbl);
            p.add(Box.createVerticalStrut(14));
        }

        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(SURFACE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(SURFACE);
        form.setBorder(BorderFactory.createEmptyBorder(0, 52, 0, 52));

        JLabel heading = new JLabel("Welcome back");
        heading.setFont(new Font("Copperplate", Font.BOLD, 28));
        heading.setForeground(HEADING_FG);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Sign in to your account");
        sub.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
        sub.setForeground(MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        usernameLabel.setForeground(LABEL_FG);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleField(usernameInputField);

        usernameErrorField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        usernameErrorField.setForeground(DANGER);
        usernameErrorField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        passwordLabel.setForeground(LABEL_FG);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleField(passwordInputField);

        styleAccentButton(logIn);
        styleGhostButton(toSignup, MUTED);
        styleGhostButton(cancel, new Color(0x9CA3AF));

        form.add(Box.createVerticalGlue());
        form.add(heading);
        form.add(Box.createVerticalStrut(6));
        form.add(sub);
        form.add(Box.createVerticalStrut(36));
        form.add(usernameLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(usernameInputField);
        form.add(Box.createVerticalStrut(4));
        form.add(usernameErrorField);
        form.add(Box.createVerticalStrut(18));
        form.add(passwordLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(passwordInputField);
        form.add(Box.createVerticalStrut(30));
        form.add(logIn);
        form.add(Box.createVerticalStrut(14));
        form.add(toSignup);
        form.add(Box.createVerticalStrut(6));
        form.add(cancel);
        form.add(Box.createVerticalGlue());

        wrapper.add(form, BorderLayout.CENTER);
        return wrapper;
    }

    private void styleField(JTextField field) {
        field.setBackground(FIELD);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleAccentButton(JButton btn) {
        btn.setBackground(ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(ACCENT_HI); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(ACCENT); }
        });
    }

    private void styleGhostButton(JButton btn, Color fg) {
        btn.setForeground(fg);
        btn.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(ACCENT); }
            public void mouseExited(MouseEvent e)  { btn.setForeground(fg); }
        });
    }

    private void wireListeners() {
        logIn.addActionListener(evt -> {
            if (loginController != null) {
                final LoginState s = loginViewModel.getState();
                loginController.execute(s.getUsername(), s.getPassword());
            }
        });

        cancel.addActionListener(this);

        toSignup.addActionListener(evt -> {
            if (loginController != null) loginController.switchToSignupView();
        });

        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void sync() {
                loginViewModel.getState().setUsername(usernameInputField.getText());
                loginViewModel.setState(loginViewModel.getState());
            }
            public void insertUpdate(DocumentEvent e)  { sync(); }
            public void removeUpdate(DocumentEvent e)  { sync(); }
            public void changedUpdate(DocumentEvent e) { sync(); }
        });

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void sync() {
                loginViewModel.getState().setPassword(new String(passwordInputField.getPassword()));
                loginViewModel.setState(loginViewModel.getState());
            }
            public void insertUpdate(DocumentEvent e)  { sync(); }
            public void removeUpdate(DocumentEvent e)  { sync(); }
            public void changedUpdate(DocumentEvent e) { sync(); }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == cancel) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            int result = JOptionPane.showConfirmDialog(frame,
                    "You are leaving the program now.", "Confirm Exit",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION) System.exit(0);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();
        usernameInputField.setText(state.getUsername());
        passwordInputField.setText(state.getPassword());
        String err = state.getLoginError();
        usernameErrorField.setText(err != null ? err : " ");
    }

    public String getViewName() { return viewName; }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
