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

    /**
     * @param args a parancssori argumentumok
     * beolvasuk a termet, a felhasznalokat, a raktart, a menut, az asztalokat es elinditjuk a programot
     */
    public static void main(String[] args) {
       XMLTerem xmlTerem = new XMLTerem();
        terem terem = xmlTerem.loadTerem();
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