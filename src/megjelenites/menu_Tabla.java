// src/megjelenites/menu_Tabla.java
package megjelenites;

import menu.menu;
import oszetevok.oszetevok;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class menu_Tabla extends AbstractTableModel {
    private final String[] columnNames = {"Elérhető", "Név", "Ár", "Összetevők"};
    private final List<menu> menus;

    public menu_Tabla(List<menu> menus) {
        this.menus = menus;
    }

    @Override
    public int getRowCount() {
        return menus.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        menu menu = menus.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return menu.isEnabled();
            case 1:
                return menu.getNev();
            case 2:
                return menu.getAr();
            case 3:
                // Egy belső JTable az összetevők megjelenítéséhez
                String[] columnNames = {"Összetevő", "Mennyiség", "Mértékegység"};
                Object[][] data = menu.getOszetevok().stream()
                        .map(oszetevo -> new Object[]{
                                oszetevo.getRaktar().getNev(),
                                oszetevo.getMennyiseg(),
                                oszetevo.getRaktar().getMertekegyseg()
                        })
                        .toArray(Object[][]::new);
                return new JTable(data, columnNames);
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
                return Boolean.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
            case 3:
                return String.class;
            default:
                return Object.class;
        }
    }
}