// src/megjelenites/ButtonRenderer.java
package megjelenites;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


/**
 * A gomb megjelenítését megvalósító osztály
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {
    /**
     * Konstruktor a gomb megjelenítéséhez
     */
    public ButtonRenderer() {
            setOpaque(true); // A gomb háttérszíne átlátszó
        }

        /**
         * A gombot adja vissza beállítva az értéket nevével
         * @param table a tábla
         * @param value az érték
         * @param isSelected kiválasztva van-e
         * @param hasFocus fókuszban van-e
         * @param row a sor
         * @param column az oszlop
         * @return a gomb
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }
    }
