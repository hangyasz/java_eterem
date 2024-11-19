// UserTableModel.java
package megjelenites.admin;

import role.User;
import role.Role;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class UserTableModel extends AbstractTableModel {
    private List<User> filteredUsers; //szürt felhasználok
    private final String[] columnNames = {"Felhasználónév", "Jelszó", "Szerep", "Törlés"};

    public UserTableModel(List<User> users) {
        //beálitja a filteredUserst
        fliter_user(users);
    }
    //vistadja a tárolt felhasználókat
    public List<User> getUsers() {
        return filteredUsers;
    }

    //felhasználók szűrése és táblázat frissítése
    public void fliter_user(List<User> users) {
        this.filteredUsers = users.stream()
                .filter(user -> user.getRole() != Role.ADMIN)
                .collect(Collectors.toList());
        fireTableDataChanged();
    }

    //visszaadja a szűrt felhasználók számát
    @Override
    public int getRowCount() {
        return filteredUsers.size();
    }

    //visszaadja az oszlopok számát
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    //visszaadja az oszlop nevét
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    //visszaadja az adott cella értékét
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = filteredUsers.get(rowIndex);
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

    //minden cella szerkeszthető
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    //cella értékének módosítása
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        User user = filteredUsers.get(rowIndex);
        switch (columnIndex) {
            case 0: //felhasználónév módosítása elenörzése az egyediség
                String newUsername = (String) aValue;
                if (filteredUsers.stream().anyMatch(u -> u.getUsername().equals(newUsername))) {
                    JOptionPane.showMessageDialog(null, "A felhasználónév már létezik!", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else {
                    user.setUsername(newUsername);
                }
                break;
            case 1: //jelszó módosítása elenörzése az egyediség
                String newPassword = (String) aValue;
                if (newPassword.matches("\\d{4}") && filteredUsers.stream().noneMatch(u -> u.getPassword().equals(newPassword))) {
                    user.setPassword(newPassword);
                } else {
                    JOptionPane.showMessageDialog(null, "A jelszónak 4 számjegyből kell állnia és egyedinek kell lennie!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 2: //szerep módosítása elenörzése az admin szerep
                if (aValue == Role.ADMIN) {
                    JOptionPane.showMessageDialog(null, "Nem lehet admin jogköröket adni!", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else {
                    user.setRole((Role) aValue);
                }
                break;
        }
        //táblázat frissítése
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    //oszlopok típusának beállítása
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