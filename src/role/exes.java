package role;

import megjelenites.login.login_side;

import javax.swing.*;
import java.util.List;

public class exes {
    public static Boolean allex(User user) {
        if(user==null){
            return false;
        }
        return  user.getRole() == Role.OWNER;
    }

    public static Boolean ratar_menuex(User user) {
        if(user==null){
            return false;
        }
        return user.getRole() == Role.BUSINESS_MANAGER || allex(user);
    }

    public static Boolean asztalex(User user) {
        if(user==null){
            return false;
        }
        return user.getRole() == Role.WAITER || ratar_menuex(user);
    }

    public static Boolean menuex(User user) {
        if(user==null){
            return false;
        }
        return user.getRole() == Role.CHEF || ratar_menuex(user);
    }

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
