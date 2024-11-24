// m_TeremTerkep.java
package megjelenites.teremterkep;

import asztal.asztal;
import terem.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


/**
 * Terem megjelenítését végző osztály
 */
public class m_TeremTerkep extends JFrame {

    /**
     * Konstruktor
     * @param asztalok Az asztalok listája
     * @param terem A terem adati
     * megjeleníti a termet és lehetőséget ad az asztalok mozgatására és az asztalok törlését, átnevezését és mozgatását
     */
    public m_TeremTerkep(List<asztal> asztalok,terem terem) {
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
        InteractableTeremPanel panel = new InteractableTeremPanel(asztalok, terem );
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

}