package megjelenites;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * A gombot valositja meg a tablazatban a katiáséet segit
 */
public class ButtonEditor extends DefaultCellEditor {
    private JButton button;

    /**
     * Konstruktor
     * @param actionListener az actionListener
     * a gomb megnyomásakor az actionListener actionperformed metódusa hívódik meg
     */
    public ButtonEditor(ActionListener actionListener) {

        super(new JCheckBox()); // Szükséges az ősosztály miatt
        button = new JButton();
        button.addActionListener(e -> {
            fireEditingStopped();
            actionListener.actionPerformed(e);
        });
    }

    /**
     * A gombot adja vissza
     * @param table a tábla
     * @param value az érték
     * @param isSelected kiválasztva van-e
     * @param row a sor
     * @param column az oszlop
     * @return a gomb
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText(value != null ? value.toString() : "");
        return button;
    }
}