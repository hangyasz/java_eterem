// src/megjelenites/menu_Tabla.java
package megjelenites.menu;

import menu.MenuType;
import menu.menu;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;


/**
 * A menü táblázatának a megjelenítését megvalósító osztály
 */

public class menu_Tabla extends AbstractTableModel {
    private final String[] columnNames = {"Elérhető","Tipus", "Név", "Ár", "Összetevők", "Törles"};
    private final List<menu> menus;

    /**
     * Konstruktor a menü táblázatának létrehozásához
     * @param menus a menük listája
     */
    public menu_Tabla(List<menu> menus) {
        this.menus = menus;
    }

    /**
     * Visszaadja a menus lista méretét
     * @return
     */
    @Override
    public int getRowCount() {
        return menus.size();
    }

    /**
     * Visszaadja az oszlopok számát az oszlopok nevei alapján
     * @return
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Visszaadja a cella értékét a megadott sor és oszlop alapján
     * @param rowIndex a sor sorszáma
     * @param columnIndex az oszlop sorszáma
     * @return
     */
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

    /**
     * Visszaadja az oszlop nevét
     * @param column az oszlop sorszáma
     * @return
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Visszaadja az oszlopok típusát
     * @param columnIndex az oszlop sorszáma
     * @return
     */
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

    /**
     * Megadja, hogy az adott cella szerkeszthető-e
     * @param rowIndex a sor sorszáma
     * @param columnIndex az oszlop sorszáma
     * @return
     * nálunk minden cella szerkeszthető
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     * A cella értékének beállítása
     * @param value az új érték
     * @param rowIndex a sor sorszáma
     * @param columnIndex az oszlop sorszáma
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        menu menu = menus.get(rowIndex);
        switch (columnIndex) {
            //elérhető beállítása
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
                        throw new IllegalArgumentException("Üres név");
                    }
                    // Check for duplicate names
                    for (menu m : menus) {
                        if (m.getNev().equalsIgnoreCase(newName) && m != menu) {
                            throw new IllegalArgumentException("Név már létezik");
                        }
                    }
                    menu.setNev(newName);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 3: //ár ellenőrzése
                try {
                    int ar = Integer.parseInt(value.toString());
                    if (ar < 0) {
                        throw new IllegalArgumentException("Negativ");
                    }
                    menu.setAr(ar);
                } catch (IllegalArgumentException ex) {
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