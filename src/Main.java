import megjelenites.*;
import raktar.raktar;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        List<raktar> raktars=new ArrayList<raktar>();
        List<menu.menu> menus=new ArrayList<menu.menu>();
        List<asztal.asztal>asztals =new ArrayList<asztal.asztal>();
        List<oszetevok.oszetevok> oszetevoks=new ArrayList<oszetevok.oszetevok>();
        oszetevoks.add(new oszetevok.oszetevok(new raktar("Alma","kg",10),1));
        oszetevoks.add(new oszetevok.oszetevok(new raktar("Körte","kg",20),2));
        oszetevoks.add(new oszetevok.oszetevok(new raktar("Banán","kg",30),3));
        menus.add(new menu.menu("Menü1",100,oszetevoks));
        menus.add(new menu.menu("Menü2",200,oszetevoks));
        raktars.add(new raktar("Alma","kg",10));
        raktars.add(new raktar("Körte","kg",20));
        raktars.add(new raktar("Banán","kg",30));
        raktars.add(new raktar("Narancs","kg",40));
        raktars.add(new raktar("Mandarin","kg",50));
        raktars.add(new raktar("Szilva","db",60));
        kezd frame = new kezd(raktars,menus,asztals);


    }
}