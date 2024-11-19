package megjelenites.teremterkep;

import asztal.asztal;
import megjelenites.Eterem.TeremPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InteractableTeremPanel extends TeremPanel {
    private asztal draggedAsztal = null;
    private int offsetX, offsetY;

    public InteractableTeremPanel(List<asztal> asztalok, double x_term, double y_term) {
        super(asztalok, x_term, y_term, null);

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
        else {
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
    }
        contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }
}