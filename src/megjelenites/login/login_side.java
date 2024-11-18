package megjelenites.login;

import role.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class login_side {
    private List<User> users;

    public login_side(List<User> users) {
        this.users = users;
    }

    public User showLoginDialog(Frame parent) {
        PinInputField pinInputField = new PinInputField(users);
        JDialog dialog = new JDialog(parent, "Login", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 600);
        dialog.setLayout(new BorderLayout());
        dialog.add(pinInputField, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return pinInputField.getAuthenticatedUser();
    }
}