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
    protected final m_Etterem parent;

    public TeremPanel(List<asztal> asztalok, double x_term, double y_term, m_Etterem parent) {
        this.asztalok = asztalok;
        this.x_term = x_term;
        this.y_term = y_term;
        this.parent = parent;

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = getSize();
                zoomFactorX = size.width / (x_term * 10);
                zoomFactorY = size.height / (y_term * 10);
                revalidate();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (asztal asztal : asztalok) {
                    if (e.getX() >= asztal.getX() * zoomFactorX && e.getX() <= (asztal.getX() + 50) * zoomFactorX &&
                            e.getY() >= asztal.getY() * zoomFactorY && e.getY() <= (asztal.getY() + 50) * zoomFactorY) {
                        if (parent != null) {
                            parent.showOrders(asztal);
                        }
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomFactorX, zoomFactorY);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, (int) (x_term * 10), (int) (y_term * 10));
        for (asztal asztal : asztalok) {
            if (asztal.getRendelesek().isEmpty()) {
                g2d.setColor(Color.GREEN);
            } else {
                g2d.setColor(Color.RED);
            }
            g2d.fillRect(asztal.getX(), asztal.getY(), 50, 50);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(asztal.getX(), asztal.getY(), 50, 50);
            g2d.drawString(asztal.getNev(), asztal.getX() + 5, asztal.getY() + 25);
        }
    }
}