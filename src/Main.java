import megjelenites.*;
import menu.MenuType;
import raktar.raktar;
import menu.menu;
import asztal.asztal;
import oszetevok.oszetevok;
import role.*;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        double x = 50;
        double y = 50;
        List<raktar> raktars = new ArrayList<>();
        raktars.add(new raktar("Krumpli", "kg", 100));
        List<oszetevok> oszetevoks = new ArrayList<>();
        oszetevoks.add(new oszetevok(raktars.get(0), 10));
        List<menu> menus = new ArrayList<>();
        menus.add(new menu("Krumpli", 100, oszetevoks, MenuType.FOETEL));
        menus.add(new menu("Krumpli2", 100, oszetevoks, MenuType.LEVES));
        menus.get(0).setEnabled(true);
        menus.get(1).setEnabled(true);
        List<asztal> asztals = new ArrayList<>();
        asztals.add(new asztal("Asztal1", 25, 25));
        List<User> users = new ArrayList<>();
        users.add(new User("owner", "1234", Role.OWNER));
        users.add(new User("manager", "5678", Role.BUSINESS_MANAGER));
        users.add(new User("admin", "0000", Role.ADMIN));
        users.add(new User("waiter", "1111", Role.WAITER));
        users.add(new User("chef", "2222", Role.CHEF));

        new kezd(raktars, menus, asztals, x, y, users);


    }
}