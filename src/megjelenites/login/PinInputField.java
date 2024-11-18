package megjelenites.login;

import role.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PinInputField extends JPanel {
    private JTextField pinField;
    private StringBuilder enteredPin = new StringBuilder();
    private List<User> users;
    private User authenticatedUser;

    public PinInputField(List<User> users) {
        this.users = users;
        setLayout(new BorderLayout());

        pinField = new JPasswordField(4);
        pinField.setFont(new Font("Arial", Font.BOLD, 24));
        pinField.setEditable(false);
        add(pinField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 10, 10));

        // Number buttons
        for (int i = 1; i <= 9; i++) {
            addNumberButton(buttonPanel, String.valueOf(i));
        }
        addNumberButton(buttonPanel, "0");

        // Clear, Enter, and Cancel buttons
        addControlButton(buttonPanel, "Clear");
        addControlButton(buttonPanel, "Enter");
        addControlButton(buttonPanel, "Cancel");

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void addNumberButton(JPanel panel, String number) {
        JButton button = new JButton(number);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.addActionListener(e -> {
            if (enteredPin.length() < 4) {
                enteredPin.append(number);
                updatePinDisplay();
            }
        });
        panel.add(button);
    }

    private void addControlButton(JPanel panel, String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.addActionListener(e -> {
            if (text.equals("Clear")) {
                enteredPin.setLength(0);
            } else if (text.equals("Enter")) {
                authenticateUser();
            } else if (text.equals("Cancel")) {
                SwingUtilities.getWindowAncestor(this).dispose();
            }
            updatePinDisplay();
        });
        panel.add(button);
    }

    private void updatePinDisplay() {
        pinField.setText("*".repeat(enteredPin.length()));
    }

    private void authenticateUser() {
        String pin = enteredPin.toString();
        for (User user : users) {
            if (user.authenticate(pin)) {
                authenticatedUser = user;
                break;
            }
        }
        if (authenticatedUser == null) {
            JOptionPane.showMessageDialog(this, "Invalid PIN", "Error", JOptionPane.ERROR_MESSAGE);
            enteredPin.setLength(0);
        } else {
            SwingUtilities.getWindowAncestor(this).dispose();
        }
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}