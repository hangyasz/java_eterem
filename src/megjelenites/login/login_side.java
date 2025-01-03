package megjelenites.login;

import role.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A login ablakot megjelenítő osztály
 */
public class login_side {
    private List<User> users;

    /**
     * Konstruktor a login ablak megjelenítéséhez
     * @param users a felhasználók listája
     * beállítja a felhasználókat
     */
    public login_side(List<User> users) {
        this.users = users;
    }
    /**
     * Megjeleníti a login ablakot
     * @param parent a szülő ablak
     * @return a bejelentkezett felhasználó
     */
    public User showLoginDialog(Frame parent) {
        PinInputField pinInputField = new PinInputField(users);
        //megjeleníti a felubó
        JDialog dialog = new JDialog(parent, "Login", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 600);
        dialog.setLayout(new BorderLayout());
        //megjelnit a jelkod mezot
        dialog.add(pinInputField, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        //viszaadja a bejelentkezett felhasználót
        return pinInputField.getAuthenticatedUser();
    }
}