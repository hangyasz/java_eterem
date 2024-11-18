// m_Etterem.java
package megjelenites;

import asztal.asztal;
import menu.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class m_Etterem extends JFrame {
    private final List<asztal> asztalok;
    private final JPanel ordersPanel;
    private double x_term = 50;
    private double y_term = 50;

    public m_Etterem(List<asztal> asztalok, double x_term, double y_term) {
        this.asztalok = asztalok;
        this.x_term = x_term;
        this.y_term = y_term;

        setTitle("Étterem");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER));
        up.add(new JLabel("Étterem"));
        add(up, BorderLayout.NORTH);

        TeremPanel panel = new TeremPanel();
        add(panel, BorderLayout.CENTER);

        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setVisible(false); // Initially invisible
        add(ordersPanel, BorderLayout.EAST);

        setVisible(true);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        Dimension size = getSize();
        ordersPanel.setPreferredSize(new Dimension(size.width / 3, size.height));
        ordersPanel.revalidate();
    }

    private class TeremPanel extends JPanel {
        private double zoomFactorX = 1.0;
        private double zoomFactorY = 1.0;

        public TeremPanel() {
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
                            showOrders(asztal);
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
            g2d.setColor(Color.BLUE);
            g2d.fillRect(0, 0, (int) (x_term * 10), (int) (y_term * 10));
            g2d.setColor(Color.BLACK);
            for (asztal asztal : asztalok) {
                g2d.drawRect(asztal.getX(), asztal.getY(), 50, 50);
                g2d.drawString(asztal.getNev(), asztal.getX() + 5, asztal.getY() + 25);
            }
        }
    }

    private void showOrders(asztal asztal) {
        ordersPanel.removeAll();
        ordersPanel.setLayout(new BorderLayout());
        // Add title and close button
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel(asztal.getNev() + " asztalnál:");
        JButton closeButton = new JButton("Bezár");
        closeButton.addActionListener(e -> ordersPanel.setVisible(false));
        titlePanel.add(titleLabel);
        titlePanel.add(closeButton);
        ordersPanel.add(titlePanel, BorderLayout.NORTH);

        // Add orders
        for (menu menu : asztal.getRendelesek()) {
            ordersPanel.add(new JLabel(menu.getNev() + " - " + menu.getAr() + " Ft"));
        }

        // Add total amount, pay button, and add new order button
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        JLabel totalLabel = new JLabel("Összeg: " + asztal.getEretke() + " Ft");
        JButton payButton = new JButton("Fizet");
        JButton addOrderButton = new JButton("Új rendelés hozzáadása");
        bottomPanel.add(addOrderButton);
        bottomPanel.add(totalLabel);
        bottomPanel.add(payButton);
        ordersPanel.add(bottomPanel, BorderLayout.SOUTH);

        ordersPanel.setVisible(true); // Make the panel visible
        ordersPanel.revalidate();
        ordersPanel.repaint();
    }
}