// m_admin.java
package megjelenites.admin;

import megjelenites.ButtonEditor;
import megjelenites.ButtonRenderer;
import role.User;
import role.Role;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class m_admin extends JFrame {
    private List<User> users;
    private JTable userTable;
    private UserTableModel userTableModel;

    public m_admin(List<User> users) {
        this.users = users;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Adminisztráció");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(true);

        userTableModel = new UserTableModel(users);
        userTable = new JTable(userTableModel);

        // Use combo box for roles
        JComboBox<Role> roleComboBox = new JComboBox<>(Role.values());
        userTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(roleComboBox));

        // Add delete button functionality
        userTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        userTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), e -> {
            int row = userTable.convertRowIndexToModel(userTable.getSelectedRow());
            if (row >= 0 && row < userTableModel.getUsers().size()) {
                User userToDelete = userTableModel.getUsers().get(row);
                if (userToDelete.getRole() == Role.OWNER) {
                    JOptionPane.showMessageDialog(m_admin.this, "Az owner fiókot nem lehet törölni!", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else {
                    users.remove(userToDelete); // Remove the user from the main list
                    userTableModel.updateUsers(users); // Update the table model
                }
            }
        }));

        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Új felhasználó hozzáadása");
        bottomPanel.add(addButton);
        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            JTextField usernameField = new JTextField(20);
            JTextField passwordField = new JTextField(20);
            JComboBox<Role> roleComboBoxNew = new JComboBox<>(Role.values());

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Felhasználónév:"));
            panel.add(usernameField);
            panel.add(new JLabel("Jelszó:"));
            panel.add(passwordField);
            panel.add(new JLabel("Szerep:"));
            panel.add(roleComboBoxNew);

            int result = JOptionPane.showConfirmDialog(null, panel, "Új felhasználó hozzáadása", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                Role role = (Role) roleComboBoxNew.getSelectedItem();
                if (!username.isEmpty() && password.matches("\\d{4}") && role != null) {
                    if (users.stream().anyMatch(u -> u.getUsername().equals(username)||u.getPassword().equals(password))) {
                        JOptionPane.showMessageDialog(m_admin.this, "A felhasználónév már létezik!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    } else if (role == Role.ADMIN) {
                        JOptionPane.showMessageDialog(m_admin.this, "Nem lehet új admin felhasználót hozzáadni!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    } else {
                        users.add(new User(username, password, role));
                        userTableModel.updateUsers(users); // Update the table model
                    }
                } else {
                    JOptionPane.showMessageDialog(m_admin.this, "Minden mezőt ki kell tölteni és a jelszónak 4 számjegyből kell állnia!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
        pack(); // Adjust the frame size to fit the preferred sizes of its components
    }
}