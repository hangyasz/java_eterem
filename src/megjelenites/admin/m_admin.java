package megjelenites.admin;

import megjelenites.ButtonEditor;
import megjelenites.ButtonRenderer;
import role.User;
import role.Role;

import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 * Az adminisztrációs felületet megjekenó osztály
 */
public class m_admin extends JFrame {
    private JTable userTable;
    private UserTableModel userTableModel;

    /**
     * Az adminisztrációs felületet megjelenítő konstruktor
     *
     * @param users        a felhasználók listája
     * @param loggedInUser a bejelentkezett felhasználó
     */
    public m_admin(List<User> users, User loggedInUser) {

        //az ablak beállításai
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Adminisztráció");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(true);

        //a felhastnálók táblázatának létrehozása
        userTableModel = new UserTableModel(users);
        userTable = new JTable(userTableModel);

        //a szerep oszlop szerkesztőjének beállítása
        JComboBox<Role> roleComboBox = new JComboBox<>(Role.values());
        userTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(roleComboBox));

        //a törlés gomb oszlop szerkesztőjének beállítása
        userTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        userTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), e -> {
            int row = userTable.convertRowIndexToModel(userTable.getSelectedRow());
            if (row >= 0 && row < userTableModel.getUsers().size()) {
                User userToDelete = userTableModel.getUsers().get(row);
                if (userToDelete.equals(loggedInUser)) {
                    JOptionPane.showMessageDialog(m_admin.this, "Nem törölheti saját magát!", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else if (userToDelete.getRole() == Role.OWNER) {
                    JOptionPane.showMessageDialog(m_admin.this, "Az owner fiókot nem lehet törölni!", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else {
                    users.remove(userToDelete); //törlés a felhasználók listából
                    userTableModel.fireTableDataChanged();  //táblázat frissítése
                }
            }
        }));

        //a táblázat hozzáadása a görgethető panelhez
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        //az új felhasználó hozzáadásához gomb hozzáadása
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Új felhasználó hozzáadása");
        bottomPanel.add(addButton);
        add(bottomPanel, BorderLayout.SOUTH);
        //az új felhasználó hozzáadásához dialógusablak megjelenítése
        addButton.addActionListener(e -> {
            add_User.addUser(users, userTableModel, m_admin.this);
        });

        setVisible(true);
        pack(); // Adjust the frame size to fit the preferred sizes of its components
    }
}