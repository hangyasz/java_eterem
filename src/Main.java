import megjelenites.*;
import menu.MenuType;
import raktar.*;
import menu.menu;
import asztal.*;
import oszetevok.oszetevok;
import role.*;
import xml.*;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        int x = 10;
        int y = 10;
        XMLUser xmlUser = new XMLUser();
        List<User> users = xmlUser.userLoad();
        XMLRaktar xmlRaktar = new XMLRaktar();
        List<raktar> raktars=xmlRaktar.raktarLoad();
        XMLMenu xmlMenu = new XMLMenu();
        List<menu> menus = xmlMenu.menuLoad(raktars);
        XMLAsztal xmlAsztal = new XMLAsztal();
        List<asztal> asztals = xmlAsztal.asztalLoad(menus, x, y);
        users.add(new User("owner", "1234", Role.OWNER));
        users.add(new User("manager", "5678", Role.BUSINESS_MANAGER));
        users.add(new User("admin", "0000", Role.ADMIN));
        users.add(new User("waiter", "1111", Role.WAITER));
        users.add(new User("chef", "2222", Role.CHEF));

        new kezd(raktars, menus, asztals, x, y, users);


    }
}