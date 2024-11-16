import megjelenites.*;
import raktar.raktar;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        List<raktar> raktars=new ArrayList<raktar>();
        raktars.add(new raktar("Alma","kg",10));
        raktars.add(new raktar("Körte","kg",20));
        raktars.add(new raktar("Banán","kg",30));
        raktars.add(new raktar("Narancs","kg",40));
        raktars.add(new raktar("Mandarin","kg",50));
        raktars.add(new raktar("Szilva","db",60));
        kezd frame = new kezd(raktars);

    }
}