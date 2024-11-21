package megjelenites.Eterem;

import asztal.asztal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TeremPanel extends JPanel {
    protected double zoomFactorX = 1.0;
    protected double zoomFactorY = 1.0;
    protected final List<asztal> asztalok;
    protected final double x_term;
    protected final double y_term;


    public TeremPanel(List<asztal> asztalok, double x_term, double y_term, JFrame parent) {
        this.asztalok = asztalok;
        this.x_term = x_term;
        this.y_term = y_term;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomFactorX, zoomFactorY);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, (int) (x_term*zoomFactorX), (int) (y_term * zoomFactorY));
        for (asztal asztal : asztalok) {
            if (asztal.getRendelesek().isEmpty()) {
                g2d.setColor(Color.GREEN);
            } else {
                g2d.setColor(Color.RED);
            }
            g2d.fillRect(asztal.getX(), asztal.getY(), 1, 1);
            g2d.setColor(Color.BLACK);
            g2d.drawString(asztal.getNev(), asztal.getX(), asztal.getY());
        }
    }
}