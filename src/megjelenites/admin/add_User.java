// add_User.java
package megjelenites.admin;

import role.User;
import role.Role;



import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Új felhasználó hozzáadását megvalósító ablak.
 */

public class add_User {

    /**
     * Új felhasználó hozzáadása.
     *
     * @param felhasznalok           a felhasználók listája
     * @param felhasznaloTablaModell a felhasználók táblázatának osztály (ezt kell frissímiat)
     * @param parentComponent        a szülő komponens, amelyhez a dialógus ablak kapcsolódik
     */
    public static void addUser(List<User> felhasznalok, UserTableModel felhasznaloTablaModell, Component parentComponent) {
        JTextField ussername = new JTextField(20);
        JTextField passworde = new JTextField(20);
        JComboBox<Role> role = new JComboBox<>(Role.values());

        // Adatbeviteli panel létrehozása
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Felhasználónév:"));
        panel.add(ussername);
        panel.add(new JLabel("Jelszó:"));
        panel.add(passworde);
        panel.add(new JLabel("Szerep:"));
        panel.add(role);

        // Adatbeviteli panel megjelenítése és kezelése
        //oldal akazasa
        if ( JOptionPane.showConfirmDialog(parentComponent, panel, "Új felhasználó hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            //adatok begyujtése
            String new_username = ussername.getText();
            String new_pasword = passworde.getText();
            Role new_role = (Role) role.getSelectedItem();
            //feltételek elenörzése
            if (!new_username.isEmpty() && new_pasword.matches("\\d{4}") && new_role != null) {
                if (felhasznalok.stream().anyMatch(u -> u.getUsername().equals(new_username) || u.getPassword().equals(new_pasword))) {
                    JOptionPane.showMessageDialog(parentComponent, "A felhasználó már létezik!", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else {
                    felhasznalok.add(new User(new_username, new_pasword, new_role)); //felhasználo hozaáadása
                    felhasznaloTablaModell.fireTableDataChanged(); //táblázat frissítése
                }
            } else {
                JOptionPane.showMessageDialog(parentComponent, "Minden mezőt ki kell tölteni és a jelszónak 4 számjegyből kell állnia!", "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}