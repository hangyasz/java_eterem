package megjelenites.admin;

import role.User;
import role.Role;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * A felhasználók táblázatának a megjelenítését megvalósító osztály
 */
public class UserTableModel extends AbstractTableModel {
    private List<User> userList; //felhasználók listája
    private final String[] columnNames = {"Felhasználónév", "Jelszó", "Szerep", "Törlés"}; //oszlopok nevei


    /**
     * Konstruktor a felhasználók táblázatának létrehozásához
     *
     * @param users a felhasználók listája
     */
    public UserTableModel(List<User> users) {
        userList = users;
    }


    /**
     * Visszaadja a felhasználókat
     * @return
     */
    public List<User> getUsers() {
        return userList;
    }

    /**
     * Visszaadja a sorok számát a felhasználók száma alapján
     * @return
     */
    @Override
    public int getRowCount() {
        return userList.size();
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
     * Visszaadja az oszlop nevét
     * @param column az oszlop sorszáma
     * @return
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Visszaadja az adott cella értékét
     * @param rowIndex a sor sorszáma
     * @param columnIndex az oszlop sorszáma
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = userList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return user.getUsername();
            case 1:
                return user.getPassword();
            case 2:
                return user.getRole();
            case 3:
                return "Törlés";
            default:
                return null;
        }
    }

    /**
     * A táblázatban lévő cellák szerkeszthetőek midnen esetben szerkeszthetőek
     * @param rowIndex a sor sorszáma
     * @param columnIndex az oszlop sorszáma
     * @return
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     * A táblázatban lévő cellák értékének módosítása
     *@param aValue az új érték
     *@param rowIndex a sor sorszáma
     *@param columnIndex az oszlop sorszáma
     *
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        User user = userList.get(rowIndex);
        switch (columnIndex) {
            case 0: //felhasználónév módosítása elenörzése az egyediség
                String newUsername = (String) aValue;
                if (userList.stream().anyMatch(u -> u.getUsername().equals(newUsername))) {
                    JOptionPane.showMessageDialog(null, "A felhasználónév már létezik!", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else {
                    user.setUsername(newUsername);
                }
                break;
            case 1: //jelszó módosítása elenörzése az egyediség
                String newPassword = (String) aValue;
                if (newPassword.matches("\\d{4}") && userList.stream().noneMatch(u -> u.getPassword().equals(newPassword))) {
                    user.setPassword(newPassword);
                } else {
                    JOptionPane.showMessageDialog(null, "A jelszónak 4 számjegyből kell állnia és egyedinek kell lennie!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 2:
                    user.setRole((Role) aValue);
                break;
        }
        //táblázat frissítése
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Az oszlopok típusának beállítása
     * @param columnIndex az oszlop sorszáma
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
                return Role.class;
            case 3:
                return JButton.class;
            default:
                return Object.class;
        }
    }
}