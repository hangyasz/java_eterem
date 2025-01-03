package megjelenites.Eterem;

import asztal.asztal;
import terem.*;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


/**
 * A terem megjelenítését megvalósító osztály az m_Etteremhez
 */
public class TeremPanel_asztal extends TeremPanel {


    /**
     * Konstruktor a terem megjelenítéséhez
     * @param asztalok az asztalok listája
     * @param terem a terem mérete
     * @param parent az m_Etterem
     * beállítja a zoom faktorokat és a terem méretét
     * ha az egér kattint egy asztalra akkor az m_Etteremben megjeleníti a orders(rendelésekl) panel
     */
    public TeremPanel_asztal(List<asztal> asztalok,terem terem, m_Etterem parent) {
        super(asztalok, terem);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = getSize();
                zoomFactorX = (double) size.width / (x_term*cellSize);
                zoomFactorY = (double) size.height /( y_term*cellSize);
                revalidate();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (asztal asztal : asztalok) {
                    int tableX = asztal.getX() * cellSize;
                    int tableY = asztal.getY() * cellSize;

                    if (e.getX() / zoomFactorX >= tableX &&
                            e.getX() / zoomFactorX < tableX + cellSize &&
                            e.getY() / zoomFactorY >= tableY &&
                            e.getY() / zoomFactorY < tableY + cellSize) {
                            parent.showOrders(asztal);
                    }
                }
            }
        });
    }
}