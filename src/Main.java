import megjelenites.*;
import menu.MenuType;
import raktar.*;
import menu.menu;
import asztal.*;
import oszetevok.oszetevok;
import role.*;
import xml.*;
import terem.*;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        terem terem= new terem(10, 10);
        XMLUser xmlUser = new XMLUser();
        List<User> users = xmlUser.userLoad();
        XMLRaktar xmlRaktar = new XMLRaktar();
        List<raktar> raktars=xmlRaktar.raktarLoad();
        XMLMenu xmlMenu = new XMLMenu();
        List<menu> menus = xmlMenu.menuLoad(raktars);
        XMLAsztal xmlAsztal = new XMLAsztal();
        List<asztal> asztals = xmlAsztal.asztalLoad(menus, terem);
        new kezd(raktars, menus, asztals, terem, users);


    }
}