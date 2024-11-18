// kezd.java
package megjelenites;

import megjelenites.Eterem.m_Etterem;
import megjelenites.menu.m_menu;
import megjelenites.raktar.m_raktar;
import megjelenites.teremterkep.m_TeremTerkep;
import megjelenites.admin.m_admin;
import menu.menu;
import raktar.raktar;
import asztal.asztal;
import role.*;
import megjelenites.login.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class kezd extends JFrame {
    private List<User> users;

    public kezd(List<raktar> raktars, List<menu> menu, List<asztal> asztals, double x, double y, List<User> users) {
        this.users = users;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Étteremi rendszer");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3, 10, 10)); // 2x3 grid with gaps

        JButton eteremButton = new JButton("Étterem");
        JButton raktarButton = new JButton("Raktár");
        JButton menuButton = new JButton("Menü");
        JButton terkepButton = new JButton("Térkép");
        JButton adminButton = new JButton("Admin");

        raktarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login_side login = new login_side(users);
                User authenticatedUser = login.showLoginDialog(kezd.this);
                if (authenticatedUser == null) {
                    return;
                }
                if (exes.ratar_menuex(authenticatedUser)) {
                    new m_raktar(raktars, menu);
                } else {
                    JOptionPane.showMessageDialog(kezd.this, "Nincs hozzáférése", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        eteremButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new m_Etterem(asztals, x, y, menu, users);
            }
        });

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login_side login = new login_side(users);
                User authenticatedUser = login.showLoginDialog(kezd.this);
                if (authenticatedUser == null) {
                    return;
                }
                if (exes.menuex(authenticatedUser)) {
                    new m_menu(menu, raktars, users, authenticatedUser);
                } else {
                    JOptionPane.showMessageDialog(kezd.this, "Nincs hozzáférése", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        terkepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login_side login = new login_side(users);
                User authenticatedUser = login.showLoginDialog(kezd.this);
                if (authenticatedUser == null) {
                    return;
                }
                if (exes.ratar_menuex(authenticatedUser)) {
                    new m_TeremTerkep(asztals, x, y);
                } else {
                    JOptionPane.showMessageDialog(kezd.this, "Nincs hozzáférése", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login_side login = new login_side(users);
                User authenticatedUser = login.showLoginDialog(kezd.this);
                if (authenticatedUser == null) {
                    return;
                }
                if (exes.allex(authenticatedUser)) {
                    new m_admin(users);
                } else {
                    JOptionPane.showMessageDialog(kezd.this, "Nincs hozzáférése", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(eteremButton);
        panel.add(raktarButton);
        panel.add(menuButton);
        panel.add(terkepButton);
        panel.add(adminButton);
        add(panel);

        pack(); // Adjust the frame size to fit the preferred sizes of its components
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }
}