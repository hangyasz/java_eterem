// src/megjelenites/menu_Tabla.java
package megjelenites.menu;

import menu.MenuType;
import menu.menu;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class menu_Tabla extends AbstractTableModel {
    private final String[] columnNames = {"Elérhető","Tipus", "Név", "Ár", "Összetevők", "Törles"};
    private final List<menu> menus;

    //konstruktor
    public menu_Tabla(List<menu> menus) {
        this.menus = menus;
    }

    //visszaadja a sorok számát
    @Override
    public int getRowCount() {
        return menus.size();
    }

    //visszaadja az oszlopok számát
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    //visszaadja az adott cella értékét
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        menu menu = menus.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return menu.isEnabled();

            case 1:
                return menu.getType();
                case 2:
                return menu.getNev();
            case 3:
                return menu.getAr();
            case 4:
                return "Összetevők";
            case 5:
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
                return Boolean.class;
                case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Integer.class;
            case 4:
                return JButton.class;
            case 5:
                return JButton.class;
            default:
                return Object.class;
        }
    }

    //a cellák szerkeszthetőek alapesetben mindegyik az
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    //az adott cella értékét bálitás modositás estén
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        menu menu = menus.get(rowIndex);
        switch (columnIndex) {
            case 0:
                menu.setEnabled((boolean) value);
                break;
            case 1:
                if (value instanceof MenuType) {
                    menu.setType((MenuType) value);
                }
                break;
            case 2:
                //név ellenőrzése
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
                    JOptionPane.showMessageDialog(null, "Érvénytelen név! " + ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 3: //ár ellenőrzése
                try {
                    int ar = Integer.parseInt(value.toString());
                    if (ar < 0) {
                        throw new NumberFormatException("Negativ");
                    }
                    menu.setAr(ar);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Érvénytelen mennyiség !" + ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                break;
        }
        //tábla frissítése
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}