// src/megjelenites/menu_Tabla.java
package megjelenites.oszetevok;

import oszetevok.oszetevok;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Az összetevők táblázatának a megjelenítésére szolgáló osztály
 */

public class oszetevok_Tabla extends AbstractTableModel {
    private final String[] columnNames = {"Név", "Mértékegység", "Mennyiség", "Törles"};
    private final List<oszetevok> oszetevok;

    /**
     * Konstruktor
     * @param oszetevoks Az összetevők listája
     */
    public oszetevok_Tabla(List<oszetevok> oszetevoks) {
        this.oszetevok = oszetevoks;
    }

    /**
     * Visszaadja az összetevők számát
     * @return
     */
    @Override
    public int getRowCount() {
        return oszetevok.size();
    }

    /**
     * Visszaadja az oszlopok számát
     * @return
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Visszaadja az adott cella értékét
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //ha nincs összetevő akkor null
        if (oszetevok== null || oszetevok.size() == 0) {
            return null;
        }
        oszetevok oszetevo = oszetevok.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return oszetevo.getNev();
            case 1:
               return oszetevo.getMertekegyseg();
            case 2:
                 return oszetevo.getMennyiseg();
            case 3:
                return "Törles";
            default:
                return null;
        }
    }

    /**
     * Visszaadja az oszlopok nevét
     * @param column
     * @return
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }


    /**
     * Visszaadja az oszlopok típusát
     * @param columnIndex
     * @return
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Double.class;
            case 3:
                return JButton.class;
            default:
                return Object.class;
        }
    }

    /**
     * Megadja, hogy az adott cella szerkeszthető-e
     * @param rowIndex
     * @param columnIndex
     * @return a 3 és 4 oszlop szerkeszthető a meyniség és a torlés
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return columnIndex == 2 || columnIndex == 3;
    }

    /**
     * Az adott cella értékét állítja be
     * @param value
     * @param rowIndex sor index
     * @param columnIndex oszlop index
     * beállítja az új menyiséget
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 2:
                oszetevok oszetevo = oszetevok.get(rowIndex);
                try {
                    double newValue = ((Number) value).doubleValue();
                    if (newValue <= 0) {
                        throw new IllegalArgumentException("Érvénytelen mennyiség!");
                    }
                    oszetevo.setMennyiseg(newValue);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                break;
        }
        //tábla frissítése
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}