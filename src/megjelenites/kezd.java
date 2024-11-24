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
import terem.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


/**
 * Kezdő panel
 */
public class kezd extends JFrame {

    /**
     * Konstruktor
     * @param raktars a raktárak listája
     * @param menu a menük listája
     * @param asztals az asztalok listája
     * @param terem a terem adati
     * @param users a felhasználók listája
     * megjeleníti a kezdő panelt és lehetőséget ad az étterem, raktár, menü, térkép és admin felületek megnyitására gombnyomásra
     */
    public kezd(List<raktar> raktars, List<menu> menu, List<asztal> asztals, terem terem, List<User> users) {
        //válaszopanel megjelenitése
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Étteremi rendszer");
        setLocationRelativeTo(null);
        setVisible(true);
        JPanel panel = new JPanel();
        // 2 sor 3 oszlop
        panel.setLayout(new GridLayout(2, 3, 10, 10));
        //gombok létrehozása
        JButton eteremButton = new JButton("Étterem");
        JButton raktarButton = new JButton("Raktár");
        JButton menuButton = new JButton("Menü");
        JButton terkepButton = new JButton("Térkép");
        JButton adminButton = new JButton("Admin");
        JButton close = new JButton("Bezárás");

        //gombokra kattintás és azonositás báliásia
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
                    JOptionPane.showMessageDialog(kezd.this, "Nincs hozzáférése a raktárhoz", "Hitelesités", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        eteremButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new m_Etterem(asztals,terem, menu, users);
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
                    JOptionPane.showMessageDialog(kezd.this, "Nincs hozzáférése", "Hitelesités", JOptionPane.ERROR_MESSAGE);
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
                    new m_TeremTerkep(asztals, terem);
                } else {
                    JOptionPane.showMessageDialog(kezd.this, "Nincs hozzáférése", "Hitelesités", JOptionPane.ERROR_MESSAGE);
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
                    new m_admin(users,authenticatedUser);
                } else {
                    JOptionPane.showMessageDialog(kezd.this, "Nincs hozzáférése", "Hitelesités", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //gombok hozzáadása a panelhez
        panel.add(eteremButton);
        panel.add(raktarButton);
        panel.add(menuButton);
        panel.add(terkepButton);
        panel.add(adminButton);
        panel.add(close);
        add(panel);
        pack();

    }
}