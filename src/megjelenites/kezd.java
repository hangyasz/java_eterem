// kezd.java
package megjelenites;

import menu.menu;
import raktar.raktar;
import asztal.asztal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class kezd extends JFrame {
    public kezd(List<raktar> raktars, List<menu> menu, List<asztal> asztals, double x, double y) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Étteremi rendszer");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10)); // 2x2 grid with gaps

        JButton eteremButton = new JButton("Étterem");
        JButton raktarButton = new JButton("Raktár");
        JButton menuButton = new JButton("Menü");
        JButton terkepButton = new JButton("Térkép");

        raktarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new m_raktar(raktars, menu);
            }
        });
        eteremButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new m_Etterem(asztals, x, y);
            }
        });
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new m_menu(menu, raktars);
            }
        });
        terkepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new m_TeremTerkep(asztals, x, y);
            }
        });

        panel.add(eteremButton);
        panel.add(raktarButton);
        panel.add(menuButton);
        panel.add(terkepButton);
        add(panel);

        pack(); // Adjust the frame size to fit the preferred sizes of its components
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }
}