// UserTableModel.java
package megjelenites.admin;

import role.User;
import role.Role;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class UserTableModel extends AbstractTableModel {
    private List<User> filteredUsers;
    private final String[] columnNames = {"Felhasználónév", "Jelszó", "Szerep", "Törlés"};

    public UserTableModel(List<User> users) {
        updateUsers(users);
    }

    public List<User> getUsers() {
        return filteredUsers;
    }

    public void updateUsers(List<User> users) {
        this.filteredUsers = users.stream()
                .filter(user -> user.getRole() != Role.ADMIN)
                .collect(Collectors.toList());
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return filteredUsers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = filteredUsers.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return user.getUsername();
            case 1:
                return user.getPassword(); // Show password
            case 2:
                return user.getRole();
            case 3:
                return "Törlés";
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    // UserTableModel.java
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        User user = filteredUsers.get(rowIndex);
        switch (columnIndex) {
            case 0:
                String newUsername = (String) aValue;
                if (filteredUsers.stream().anyMatch(u -> u.getUsername().equals(newUsername))) {
                    JOptionPane.showMessageDialog(null, "A felhasználónév már létezik!", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else {
                    user.setUsername(newUsername);
                }
                break;
            case 1:
                String newPassword = (String) aValue;
                if (newPassword.matches("\\d{4}") && filteredUsers.stream().noneMatch(u -> u.getPassword().equals(newPassword))) {
                    user.setPassword(newPassword);
                } else {
                    JOptionPane.showMessageDialog(null, "A jelszónak 4 számjegyből kell állnia és egyedinek kell lennie!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 2:
                if (aValue == Role.ADMIN) {
                    JOptionPane.showMessageDialog(null, "Nem lehet admin jogköröket adni!", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else {
                    user.setRole((Role) aValue);
                }
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
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