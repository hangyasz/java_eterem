package role;

import megjelenites.login.login_side;

import javax.swing.*;
import java.util.List;


/**
 * Az osztály a felhasználók jogosultságait kezeli
 */
public class exes {
    /**
     * A felhasználó jogosultságainak ellenőrzése
     * @param user
     * @return true, ha a felhasználó Tuljonos, egyébként false
     */
    public static Boolean allex(User user) {
        if(user==null){
            return false;
        }
        return  user.getRole() == Role.Tuljonos;
    }

    /**
     * A felhasználó jogosultságainak ellenőrzése
     * @param user
     * @return true, ha a felhasználó Üzlet_Vezető vagy magasabb, egyébként false
     */
    public static Boolean ratar_menuex(User user) {
        if(user==null){
            return false;
        }
        return user.getRole() == Role.Üzlet_Vezető || allex(user);
    }

    /**
     * A felhasználó jogosultságainak ellenőrzése
     * @param user
     * @return true, ha a felhasználó Pincár vagy magasabb, egyébként false
     */
    public static Boolean asztalex(User user) {
        if(user==null){
            return false;
        }
        return user.getRole() == Role.Pincér || ratar_menuex(user);
    }

    /**
     * A felhasználó jogosultságainak ellenőrzése
     * @param user
     * @return true, ha a felhasználó Szakács vagy magasabb, egyébként false
     */
    public static Boolean menuex(User user) {
        if(user==null){
            return false;
        }
        return user.getRole() == Role.Szakács || ratar_menuex(user);
    }

    /**
     * A felhasználó jogosultságainak ellenőrzése
     * @param users A felhasználók listája
     * @param frame A szülőablak
     * @return true, ha a felhasználó Üzlet_Vezető vagy magasabb, egyébként false
     */
    public static boolean magasab( List<User> users, JFrame frame) {
        login_side login = new login_side(users);
        User user = login.showLoginDialog(frame);
        if (user == null) {
            return false;
        }
        if (!exes.ratar_menuex(user)) {
            JOptionPane.showMessageDialog(frame, "Nincs hozzáférése megtagava", "Hitelsités", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }


}
