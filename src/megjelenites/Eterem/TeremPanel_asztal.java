package megjelenites.Eterem;

import asztal.asztal;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TeremPanel_asztal extends TeremPanel {

    public TeremPanel_asztal(List<asztal> asztalok, double x_term, double y_term, m_Etterem parent) {
        super(asztalok, x_term, y_term);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = getSize();
                zoomFactorX = size.width / (x_term*cellSize);
                zoomFactorY = size.height /( y_term*cellSize);
                revalidate();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (asztal asztal : asztalok) {
                    if (e.getX() >= asztal.getX() * zoomFactorX && e.getX() <= (asztal.getX() +cellSize) * zoomFactorX &&
                            e.getY() >= asztal.getY() * zoomFactorY && e.getY() <= (asztal.getY() + cellSize) * zoomFactorY) {
                        if (parent != null) {
                            parent.showOrders(asztal);
                        }
                        break;
                    }
                }
            }
        });
    }
}