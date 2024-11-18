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
        TeremPanel panel = new TeremPanel();
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private class TeremPanel extends JPanel {
        private asztal draggedAsztal = null;
        private int offsetX, offsetY;
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
                    boolean tableClicked = false;
                    for (asztal asztal : asztalok) {
                        if (e.getX() >= asztal.getX() * zoomFactorX && e.getX() <= (asztal.getX() + 50) * zoomFactorX &&
                                e.getY() >= asztal.getY() * zoomFactorY && e.getY() <= (asztal.getY() + 50) * zoomFactorY) {
                            tableClicked = true;
                            if (SwingUtilities.isRightMouseButton(e)) {
                                showContextMenu(e, asztal);
                            } else {
                                draggedAsztal = asztal;
                                offsetX = (int) (e.getX() - asztal.getX() * zoomFactorX);
                                offsetY = (int) (e.getY() - asztal.getY() * zoomFactorY);
                            }
                            break;
                        }
                    }
                    if (!tableClicked && SwingUtilities.isRightMouseButton(e)) {
                        showContextMenu(e, null);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    draggedAsztal = null;
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (draggedAsztal != null) {
                        int newX = (int) ((e.getX() - offsetX) / zoomFactorX);
                        int newY = (int) ((e.getY() - offsetY) / zoomFactorY);

                        if (newX < 0) newX = 0;
                        if (newY < 0) newY = 0;
                        if (newX + 50 > x_term * 10) newX = (int) (x_term * 10 - 50);
                        if (newY + 50 > y_term * 10) newY = (int) (y_term * 10 - 50);

                        draggedAsztal.setX(newX);
                        draggedAsztal.setY(newY);
                        repaint();
                    }
                }
            });

            addMouseWheelListener(e -> {
                if (e.getWheelRotation() < 0) {
                    zoomFactorX *= 1.1;
                    zoomFactorY *= 1.1;
                } else {
                    zoomFactorX /= 1.1;
                    zoomFactorY /= 1.1;
                }
                repaint();
            });
        }

        private void showContextMenu(MouseEvent e, asztal asztal) {
            JPopupMenu contextMenu = new JPopupMenu();
            JMenuItem renameItem = new JMenuItem("Átnevezés");
            JMenuItem deleteItem = new JMenuItem("Törlés");

            if (asztal != null) {
                renameItem.addActionListener(event -> {
                    String newName = JOptionPane.showInputDialog("Adja meg az új nevet:", asztal.getNev());
                    if (newName != null && !newName.trim().isEmpty() && newName.length() <= 6) {
                        if (asztalok.stream().anyMatch(a -> a.getNev().equals(newName))) {
                            JOptionPane.showMessageDialog(this, "Már létezik asztal ezzel a névvel!", "Hiba", JOptionPane.ERROR_MESSAGE);
                        } else {
                            asztal.setNev(newName);
                            repaint();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Az asztal neve maximum 6 karakter lehet!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    }
                });

                deleteItem.addActionListener(event -> {
                    asztalok.remove(asztal);
                    repaint();
                });

                contextMenu.add(renameItem);
                contextMenu.add(deleteItem);
            }

            int x = (int) (e.getX() / zoomFactorX);
            int y = (int) (e.getY() / zoomFactorY);
            if (x >= 0 && x + 50 <= x_term * 10 && y >= 0 && y + 50 <= y_term * 10) {
                JMenuItem addItem = new JMenuItem("Új asztal hozzáadása");
                addItem.addActionListener(event -> {
                    String nev = JOptionPane.showInputDialog("Adja meg az asztal nevét:");
                    if (nev != null && !nev.trim().isEmpty() && nev.length() <= 6) {
                        if (asztalok.stream().anyMatch(a -> a.getNev().equals(nev))) {
                            JOptionPane.showMessageDialog(this, "Már létezik asztal ezzel a névvel!", "Hiba", JOptionPane.ERROR_MESSAGE);
                        } else {
                            asztal ujAsztal = new asztal(nev, x, y);
                            asztalok.add(ujAsztal);
                            repaint();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Az asztal neve maximum 6 karakter lehet!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    }
                });
                contextMenu.add(addItem);
            }

            contextMenu.show(e.getComponent(), e.getX(), e.getY());
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
                g2d.fillRect(asztal.getX(), asztal.getY(), 50, 50); // Fill the rectangle with the color
                g2d.setColor(Color.BLACK);
                g2d.drawRect(asztal.getX(), asztal.getY(), 50, 50); // Draw the border of the rectangle
                g2d.drawString(asztal.getNev(), asztal.getX() + 5, asztal.getY() + 25);
            }
        }
    }
}