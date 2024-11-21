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
    protected int cellSize = 50;


    public TeremPanel(List<asztal> asztalok, double x_term, double y_term) {
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
        g2d.fillRect(0, 0, (int) (x_term*cellSize * zoomFactorX), (int) (y_term *cellSize* zoomFactorY));

        for (asztal asztal : asztalok) {
            // Négyzet színe a rendelések állapotától függően
            if (asztal.getRendelesek().isEmpty()) {
                g2d.setColor(Color.GREEN);
            } else {
                g2d.setColor(Color.RED);
            }
            g2d.fillRect(asztal.getX()*cellSize, asztal.getY()*cellSize, cellSize, cellSize);

            // Szöveg rajzolása
            g2d.setColor(Color.BLACK);
            Font font = new Font("Arial",Font.BOLD, 12); // Fix méretű betűtípus
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);

            String text = asztal.getNev();
            int textWidth = metrics.stringWidth(text);
            int textHeight = metrics.getHeight();

            // Szöveg pozíciójának kiszámítása a négyzet közepére
            int textX = asztal.getX()*cellSize + (cellSize - textWidth) / 2;
            int textY = asztal.getY()*cellSize + ((cellSize - textHeight) / 2) + metrics.getAscent();

            g2d.drawString(text, textX, textY);
        }
    }

}