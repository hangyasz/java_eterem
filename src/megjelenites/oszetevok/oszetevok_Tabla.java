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

    @Override
    public int getRowCount() {
        return oszetevok.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
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

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

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

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2 || columnIndex == 3;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 2:
                oszetevok oszetevo = oszetevok.get(rowIndex);
                try {
                    double newValue = ((Number) value).doubleValue();
                    oszetevo.setMennyiseg(newValue);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                break;
        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }
}