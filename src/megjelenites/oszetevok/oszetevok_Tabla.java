// src/megjelenites/menu_Tabla.java
package megjelenites.oszetevok;

import oszetevok.oszetevok;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class oszetevok_Tabla extends AbstractTableModel {
    private final String[] columnNames = {"Név", "Mértékegység", "Mennyiség", "Törles"};
    private final List<oszetevok> oszetevok;

    public oszetevok_Tabla(List<oszetevok> oszetevoks) {
        this.oszetevok = oszetevoks;
    }

    //visszaadja a sorok számát
    @Override
    public int getRowCount() {
        return oszetevok.size();
    }

    //visszaadja az oszlopok számát
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    //visszaadja az adott cella értékét
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

    //visszaadja az oszlop nevét
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    //visszaadja az oszlopok típusát
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

    //visszaadja hogy az adott cella szerkeszthető itt a menyiség és a torlés gomb
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return columnIndex == 2 || columnIndex == 3;
    }

    //az adott cella értékét beállítja
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