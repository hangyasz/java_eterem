// src/megjelenites/menu_Tabla.java
package megjelenites;

import menu.menu;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class menu_Tabla extends AbstractTableModel {
    private final String[] columnNames = {"Elérhető", "Név", "Ár", "Összetevők", "Törles"};
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
                return "Összetevők";
            case 4:
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
                return Boolean.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
            case 3:
                return JButton.class;
            case 4:
                return JButton.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        menu menu = menus.get(rowIndex);
        switch (columnIndex) {
            case 0:
                menu.setEnabled((boolean) value);
                break;
            case 1:
                try {
                    String newName = value.toString();
                    if (newName.equals("")) {
                        throw new NumberFormatException("Üres");
                    }
                    // Check for duplicate names
                    for (menu m : menus) {
                        if (m.getNev().equalsIgnoreCase(newName) && m != menu) {
                            throw new IllegalArgumentException("Név már létezik");
                        }
                    }
                    menu.setNev(newName);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Érvénytelen név!", "Hiba", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 2:
                try {
                    int ar = Integer.parseInt(value.toString());
                    if (ar < 0) {
                        throw new NumberFormatException("Negativ");
                    }
                    menu.setAr(ar);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Érvénytelen mennyiség!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}