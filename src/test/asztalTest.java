package test;

import asztal.asztal;
import menu.MenuType;
import menu.menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import oszetevok.oszetevok;
import raktar.raktar;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class asztalTest {

    private List<asztal> asztalok;
    private List<menu> menuItems;
    private List<raktar> raktars;

    @BeforeEach
    void setUp() {
        asztalok = new ArrayList<>();
        menuItems = new ArrayList<>();
        raktars = new ArrayList<>();


        raktars.add(new raktar("Raktar1", "db", 1000));
        oszetevok oszetevo1 = new oszetevok(raktars.get(0), 10);
        raktars.add(new raktar("Raktar2", "kg", 10));
        oszetevok oszetevo2 = new oszetevok(raktars.get(1), 1);
        raktars.add(new raktar("Raktar3", "l", 100));
        oszetevok oszetevo3 = new oszetevok(raktars.get(2), 20);
        raktars.add(new raktar("Raktar4", "kg", 1));
        oszetevok oszetevo4 = new oszetevok(raktars.get(3), 0.05);

        List<oszetevok> oszetevok = new ArrayList<>();
        oszetevok.add(oszetevo1);
        oszetevok.add(oszetevo2);

        List<oszetevok> oszetevok2 = new ArrayList<>();
        oszetevok2.add(oszetevo1);
        oszetevok2.add(oszetevo2);
        oszetevok2.add(oszetevo3);
        oszetevok2.add(oszetevo4);


        List<oszetevok> oszetevok3 = new ArrayList<>();
        oszetevok3.add(oszetevo1);
        oszetevok3.add(oszetevo4);
        // Create some menu items
        menu item1 = new menu("Menu1", 1000, oszetevok, MenuType.ELOETEL);
        menu item2 = new menu("Menu2", 1500, oszetevok2, MenuType.FOETEL);
        menu item3 = new menu("Menu3", 500, oszetevok3, MenuType.DESZERT);

        menuItems.add(item1);
        menuItems.add(item2);
        menuItems.add(item3);

        // Create some tables
        asztal asztal1 = new asztal("Asztal1", 1, 1);
        asztal asztal2 = new asztal("Asztal2", 2, 2);
        asztal asztal3 = new asztal("Asztal3", 3, 3);

        asztalok.add(asztal1);
        asztalok.add(asztal2);
        asztalok.add(asztal3);
    }


    /**
     * Teszteljük a vendéglátó egység működését egy hipotetikus példán keresztül.
     */
    @Test
    void testRestaurantScenario() {
        //rendelések hozzáadása
        asztalok.get(0).addRendeles(menuItems.get(0)); // Asztal1 orders Menu1
        asztalok.get(1).addRendeles(menuItems.get(1)); // Asztal2 orders Menu2
        asztalok.get(2).addRendeles(menuItems.get(2)); // Asztal3 orders Menu3

        //rendelések ellenőrzése
        assertEquals(1, asztalok.get(0).getRendelesek().size());
        assertEquals(1, asztalok.get(1).getRendelesek().size());
        assertEquals(1, asztalok.get(2).getRendelesek().size());

        //fizetés
        assertEquals(1000, asztalok.get(0).getEretke());
        assertEquals(1500, asztalok.get(1).getEretke());
        assertEquals(500, asztalok.get(2).getEretke());

        //fizetés
        for (asztal asztal : asztalok) {
            asztal.pay();
        }

        //menyiség ellenőrzése
        assertEquals(0, asztalok.get(0).getRendelesek().size());
        assertEquals(0, asztalok.get(1).getRendelesek().size());
        assertEquals(0, asztalok.get(2).getRendelesek().size());

        //érték ellenőrzése
        assertEquals(0, asztalok.get(0).getEretke());
        assertEquals(0, asztalok.get(1).getEretke());
        assertEquals(0, asztalok.get(2).getEretke());

        assertEquals(970, raktars.get(0).getMennyiseg());
        assertEquals(8, raktars.get(1).getMennyiseg());
        assertEquals(80, raktars.get(2).getMennyiseg());

        assertEquals(0.9, raktars.get(3).getMennyiseg(), 0.0001);
    }


    /**
     * Teszteljük, hogy az asztal rendeléseket tud hozzáadni és eltávolítani. nem teljesen tökéletes munka
     */
    @Test
    void testAddAndRemoveMultipleOrders() {
        asztal asztal = new asztal("Asztal4", 4, 4);
        asztalok.add(asztal);

        //10 rendelés hozzáadása
        menu item = menuItems.get(0);
        for (int i = 0; i < 10; i++) {
            asztal.addRendeles(item);
        }

        //ellenőrzés
        assertEquals(10, asztal.getRendelesek().size());
        assertEquals(10000, asztal.getEretke());

        //eltávolitunk 2 rendelést
        for (int i = 0; i < 2; i++) {
            asztal.removeRendeles(item);
        }

        //ellenőrzés
        assertEquals(8, asztal.getRendelesek().size());
        assertEquals(8000, asztal.getEretke());

        //elenorizzuk a raktár mennyiségét
        assertEquals(920, raktars.get(0).getMennyiseg());
        assertEquals(2, raktars.get(1).getMennyiseg());
        assertEquals(100, raktars.get(2).getMennyiseg());
        assertEquals(1, raktars.get(3).getMennyiseg());
    }
}