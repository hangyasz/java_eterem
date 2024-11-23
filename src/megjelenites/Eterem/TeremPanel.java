package megjelenites.Eterem;

import asztal.asztal;
import terem.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TeremPanel extends JPanel {
    protected double zoomFactorX = 1.0;
    protected double zoomFactorY = 1.0;
    protected final List<asztal> asztalok;
    protected final int x_term;
    protected final int y_term;
    protected int cellSize = 50;


    public TeremPanel(List<asztal> asztalok, terem terem) {
        this.asztalok = asztalok;
        x_term = terem.getX();
        y_term = terem.getY();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomFactorX, zoomFactorY);
        g2d.setColor(Color.LIGHT_GRAY);
        int teremWidth;
        if (zoomFactorX < 1.0)  {
            teremWidth = (int) (x_term*cellSize / zoomFactorX);
        } else {
            teremWidth = (int) (x_term*cellSize * zoomFactorX);
        }
        int teremHeight;
        if (zoomFactorY < 1.0) {
            teremHeight = (int) (y_term *cellSize / zoomFactorY);
        } else {
            teremHeight = (int) (y_term *cellSize* zoomFactorY);
        }

        g2d.fillRect(0, 0, teremWidth, teremHeight);

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