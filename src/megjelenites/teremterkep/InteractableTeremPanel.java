package megjelenites.teremterkep;

import asztal.asztal;
import megjelenites.Eterem.TeremPanel;
import terem.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InteractableTeremPanel extends TeremPanel {
    private asztal draggedAsztal = null;

    public InteractableTeremPanel(List<asztal> asztalok, terem terem) {
        super(asztalok, terem);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = getSize();
                zoomFactorX = (double) size.width / (x_term * cellSize);
                zoomFactorY = (double) size.height / (y_term * cellSize);
                revalidate();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                boolean tableClicked = false;
                for (asztal asztal : asztalok) {
                    int tableX = asztal.getX() * cellSize;
                    int tableY = asztal.getY() * cellSize;

                    if (e.getX() / zoomFactorX >= tableX &&
                            e.getX() / zoomFactorX < tableX + cellSize &&
                            e.getY() / zoomFactorY >= tableY &&
                            e.getY() / zoomFactorY < tableY + cellSize) {
                        tableClicked = true;
                        if (SwingUtilities.isRightMouseButton(e)) {
                            showContextMenu(e, asztal);
                        } else {
                            draggedAsztal = asztal;
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
                    int newX = (int) (e.getX() / (zoomFactorX * cellSize));
                    int newY = (int) (e.getY() / (zoomFactorY * cellSize));

                    if (newX < 0) newX = 0;
                    if (newY < 0) newY = 0;
                    if (newX >= x_term) newX = x_term - 1;
                    if (newY >= y_term) newY = y_term - 1;

                    draggedAsztal.setX(newX);
                    draggedAsztal.setY(newY);
                    repaint();
                }
            }
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
        } else {
            int x = (int) (e.getX() / (zoomFactorX * cellSize));
            int y = (int) (e.getY() / (zoomFactorY * cellSize));

            if (x >= 0 && x + 1 <= x_term && y >= 0 && y + 1 <= y_term) {
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