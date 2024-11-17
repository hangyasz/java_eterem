// src/megjelenites/m_menu.java
package megjelenites;

import menu.menu;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class m_menu extends JFrame {

    public m_menu(List<menu> menus) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Menü");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel("Menü"));
        add(topPanel, BorderLayout.NORTH);

        JTable table = new JTable(new menu_Tabla(menus));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton button = new JButton("Vissza");
        button.addActionListener(e -> dispose());
        bottomPanel.add(button);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}