package megjelenites.login;

import role.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

        // PIN bevitel billentyűzettel
        pinField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (Character.isDigit(e.getKeyChar()) && enteredPin.length() < 4) {
                    enteredPin.append(e.getKeyChar());
                    updatePinDisplay();
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && enteredPin.length() > 0) {
                    enteredPin.setLength(enteredPin.length() - 1);
                    updatePinDisplay();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    authenticateUser();
                }
            }
        });

        // Gombok panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 10, 10));

        // 1-9 gombok
        for (int i = 1; i <= 9; i++) {
            addNumberButton(buttonPanel, String.valueOf(i));
        }
        // 0 gomb
        addNumberButton(buttonPanel, "0");

        // Törlés, Enter, Mégse gombok
        addControlButton(buttonPanel, "Clear");
        addControlButton(buttonPanel, "Enter");
        addControlButton(buttonPanel, "Visza");

        add(buttonPanel, BorderLayout.CENTER);

        // beltja hogy alapbol a pinField legyen a kiválszva (azonal lehesen gépleni)
        SwingUtilities.invokeLater(() -> pinField.requestFocusInWindow());
    }

    private void addNumberButton(JPanel panel, String number) {
        JButton button = new JButton(number);
        // gomb szövegének megelenése
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.addActionListener(e -> {
            if (enteredPin.length() < 4) {
                enteredPin.append(number);
                updatePinDisplay(); // pinField frissítése a beírt számok erlejtése
            }
        });
        panel.add(button);
    }

    // vezérlő gombok hozzáadása
    private void addControlButton(JPanel panel, String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.addActionListener(e -> {
            switch (text) {
                case "Clear": // törlés
                    enteredPin.setLength(0);
                    break;
                case "Enter":
                    authenticateUser(); // belépés
                    break;
                case "Visza": // bezárás
                    SwingUtilities.getWindowAncestor(this).dispose();
                    break;
            }
            updatePinDisplay();
        });
        panel.add(button);
    }

    // pinField frissítése csilagokal
    private void updatePinDisplay() {
        pinField.setText("*".repeat(enteredPin.length()));
    }

    // felhasználó azonosítása
    private void authenticateUser() {
        String pin = enteredPin.toString();
        for (User user : users) {
            if (user.authenticate(pin)) {
                authenticatedUser = user;
                break;
            }
        }
        // hibás pin esetén hibaüzenet
        if (authenticatedUser == null) {
            JOptionPane.showMessageDialog(this, "Hibbás Pinn", "Hiba", JOptionPane.ERROR_MESSAGE);
            enteredPin.setLength(0);
        } else {
            //belépés esetén ablak bezárása
            SwingUtilities.getWindowAncestor(this).dispose();
        }
        updatePinDisplay();
    }

    // visszaadja a bejelentkezett felhasználót
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}
