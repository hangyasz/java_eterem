// m_TeremTerkep.java
package megjelenites.teremterkep;

import asztal.asztal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class m_TeremTerkep extends JFrame {
    private final List<asztal> asztalok;
    private double x_term = 50;
    private double y_term = 50;

    public m_TeremTerkep(List<asztal> asztalok, double x_term, double y_term) {
        this.asztalok = asztalok;
        this.x_term = x_term;
        this.y_term = y_term;
        setTitle("Terem Térkép");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER));
        up.add(new JLabel("Terem Térkép"));
        add(up, BorderLayout.NORTH);
        JPanel done = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton vissza = new JButton("Vissza");
        vissza.addActionListener(e -> dispose());
        done.add(vissza);
        add(done, BorderLayout.SOUTH);
        InteractableTeremPanel panel = new InteractableTeremPanel(asztalok, x_term, y_term);
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

}