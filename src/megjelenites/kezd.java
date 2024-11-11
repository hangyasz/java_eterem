package megjelenites;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class kezd extends JFrame {
    public kezd(List<raktar.raktar> raktars) {
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
             new m_raktar(raktars);
            }
        });
        eteremButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Étterembe lépés");
            }
        });
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Menübe lépés");
            }
        });
        terkepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Térképbe lépés");
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
