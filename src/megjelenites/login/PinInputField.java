package megjelenites.login;

import role.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * A PIN bevitel mezőt megvalósító osztály
 */

public class PinInputField extends JPanel {
    private JTextField pinField;
    private StringBuilder enteredPin = new StringBuilder();
    private List<User> users;
    private User authenticatedUser = null;


    /**
     * Konstruktor a PIN bevitel mező létrehozásához
     * @param users a felhasználók listája
     * beállítja a felhasználókat és a mezőket itt lehet beírni a pin kódot
     */
    public PinInputField(List<User> users) {
        this.users = users;
        setLayout(new BorderLayout());

        // PIN mező
        pinField = new JPasswordField(4);
        pinField.setFont(new Font("Arial", Font.BOLD, 24));
        pinField.setEditable(false);
        add(pinField, BorderLayout.NORTH);

        // PIN bevitel billentyűzettel
        pinField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // ha számot ütünk be és a pin hossza nem éri el a 4 karaktert akkor hozzáadja a pinhez
                if (Character.isDigit(e.getKeyChar()) && enteredPin.length() < 4) {
                    enteredPin.append(e.getKeyChar());
                    updatePinDisplay();
                    // ha backspace-t ütünk be és a pin hossza nagyobb mint 0 akkor törli az utolsó karaktert
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && enteredPin.length() > 0) {
                    enteredPin.setLength(enteredPin.length() - 1);
                    updatePinDisplay();
                    // ha enter-t ütünk be akkor belépteti a felhasználót
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

    /**
     * Szám gomb hozzáadása
     * @param panel a panel amire a gombot hozzáadja
     * @param number a gomb szövege
     */
    private void addNumberButton(JPanel panel, String number) {
        JButton button = new JButton(number);
        // gomb szövegének megelenése formátuma
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.addActionListener(e -> {
            if (enteredPin.length() < 4) {
                enteredPin.append(number);
                updatePinDisplay(); // pinField frissítése a beírt számok erlejtése
            }
        });
        panel.add(button);
    }

    /**
     * Vezérlő gomb hozzáadása (Clear, Enter, Visza) gombok muveletei álja be
     * @param panel a panel amire a gombot hozzáadja
     * @param text a gomb szövege
     */
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

    /**
     * A pinField frissítése a beírja a csillagokat a beírt számok helyére
     */
    private void updatePinDisplay() {
        pinField.setText("*".repeat(enteredPin.length()));
    }

    /**
     * Felhasználó azonosítása a beírt PIN alapján  hibba esetén hibaüzenet ha helyes akkor belépteti a felhasználót és bezárja az ablakot
     */
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

    /**
     * Az azonosított felhasználó lekérdezése
     * @return az azonosított felhasználó
     */
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}
