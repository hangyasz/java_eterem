package test;

import menu.MenuType;
import menu.menu;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import oszetevok.oszetevok;
import raktar.raktar;
import xml.XMLManager;
import xml.XMLMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XMLMenuTest {

    private static XMLMenu xmlMenu;
    private List<menu> menuItems;
    private List<raktar> raktarItems;
    private static final String MENU_FILE = "menu_test.xml";

    @BeforeAll
    static void setUpBeforeClass() {
        //a teszteléshez a fájl nevének átállítása
        XMLManager.MENU_FILE = MENU_FILE;
    }

    @BeforeEach
    void setUp() {
        xmlMenu = new XMLMenu();
        menuItems = new ArrayList<>();
        raktarItems = new ArrayList<>();
        try (PrintWriter writer = new PrintWriter(MENU_FILE)) {
            writer.print("");
        } catch (FileNotFoundException e) {
            fail("Nem sikerült törölni a fájl tartalmát");
        }
    }

    /**
     * Megvizsgáljuk, hogy a fájlba kiírt és beolvasott menü elemek megegyeznek-e
     */
    @Test
    void saveMenuToXML() {
        menu item1 = new menu("Menu1", 1000, new ArrayList<>(), MenuType.ELOETEL);
        menu item2 = new menu("Menu2", 2000, new ArrayList<>(), MenuType.ITAL);
        menuItems.add(item1);
        menuItems.add(item2);
        assertDoesNotThrow(() -> xmlMenu.saveMenuToXML(menuItems));
        List<menu> loadedItems = xmlMenu.menuLoad(raktarItems);
        assertEquals(2, loadedItems.size());
        assertEquals("Menu1", loadedItems.get(0).getNev());
        assertEquals(1000, loadedItems.get(0).getAr());
        assertEquals(MenuType.ELOETEL, loadedItems.get(0).getType());
        assertEquals("Menu2", loadedItems.get(1).getNev());
        assertEquals(2000, loadedItems.get(1).getAr());
        assertEquals(MenuType.ITAL, loadedItems.get(1).getType());
    }

    /**
     * Megvizsgáljuk, hogy a fájl üres akkor a menü elemek listája is üres-e
     */
    @Test
    void menuLoad_handlesEmptyFile() {
        assertDoesNotThrow(() -> xmlMenu.saveMenuToXML(menuItems));
        List<menu> loadedItems = xmlMenu.menuLoad(raktarItems);
        assertTrue(loadedItems.isEmpty());
    }

    /**
     * Ha null értéket kap a kiírás akkor kivételt dob
     */
    @Test
    void saveMenuToXML_handlesNullItems() {
        assertThrows(NullPointerException.class, () -> xmlMenu.saveMenuToXML(null));
    }

    /**
     * Ha a fájlban szerepel egy elem többször akkor kivételt dob
     */
    @Test
    void menuLoad_handlesDuplicateItems() {
        menu item1 = new menu("Menu1", 1000, new ArrayList<>(), MenuType.ELOETEL);
        menu item2 = new menu("Menu1", 2000, new ArrayList<>(), MenuType.ITAL);
        menuItems.add(item1);
        menuItems.add(item2);
        assertDoesNotThrow(() -> xmlMenu.saveMenuToXML(menuItems));
        Exception exception = assertThrows(Exception.class, () -> xmlMenu.loadMenuFromXML(raktarItems));
        assertEquals("Az asztal neve már létezik", exception.getMessage());
    }

    /**
     * Megvizsgáljuk, hogy a menü elemet módosítjuk és azt a fájlba írjuk és betöltjük a fájlból és megnézzük, hogy a módosítás sikeres volt-e
     */
    @Test
    void modifyMenuItem_saveToXMLAndLoad() {
        menu item1 = new menu("Menu1", 1000, new ArrayList<>(), MenuType.ELOETEL);
        menuItems.add(item1);
        assertDoesNotThrow(() -> xmlMenu.saveMenuToXML(menuItems));
        List<menu> loadedItems = xmlMenu.menuLoad(raktarItems);
        loadedItems.get(0).setAr(1500);
        xmlMenu=new XMLMenu();
        List<menu> modifiedItems = xmlMenu.menuLoad(raktarItems);
        assertEquals(1500, modifiedItems.get(0).getAr());
        loadedItems.get(0).setNev("Menu2");
        xmlMenu=new XMLMenu();
        modifiedItems = xmlMenu.menuLoad(raktarItems);
        assertEquals("Menu2", modifiedItems.get(0).getNev());
        loadedItems.get(0).setType(MenuType.LEVES);
        xmlMenu=new XMLMenu();
        modifiedItems = xmlMenu.menuLoad(raktarItems);
        assertEquals(MenuType.LEVES, modifiedItems.get(0).getType());
        raktarItems.add(new raktar("Raktar1", "Raktar1", 1000));
        loadedItems.get(0).getOszetevok().add(new oszetevok(raktarItems.get(0), 100));
        xmlMenu=new XMLMenu();
        loadedItems = xmlMenu.menuLoad(raktarItems);
        assertEquals(1, loadedItems.get(0).getOszetevok().size());
        assertEquals(100, loadedItems.get(0).getOszetevok().get(0).getMennyiseg());
    }

    /**
     * Megvizsgáljuk, hogy a menü elemet hozzáadjuk és azt a fájlba írjuk és betöltjük a fájlból és megnézzük, hogy a hozzáadás sikeres volt-e
     */
    @Test
    void addMenuItem_saveToXMLAndLoad() {
        menu item1 = new menu("Menu1", 1000, new ArrayList<>(), MenuType.ELOETEL);
        menuItems.add(item1);
        assertDoesNotThrow(() -> xmlMenu.saveMenuToXML(menuItems));
        List<menu> loadedItems = xmlMenu.menuLoad(raktarItems);
        loadedItems.add(new menu("Menu2", 2000, new ArrayList<>(), MenuType.ITAL));
        xmlMenu=new XMLMenu();
        List<menu> updatedItems = xmlMenu.menuLoad(raktarItems);
        assertEquals(2, updatedItems.size());
        assertEquals("Menu2", updatedItems.get(1).getNev());
        assertEquals(2000, updatedItems.get(1).getAr());
    }

    /**
     * Megvizsgáljuk, hogy a menü elemet töröljük és azt a fájlba írjuk és betöltjük a fájlból és megnézzük, hogy a törlés sikeres volt-e
     */
    @Test
    void deleteMenuItem_saveToXMLAndLoad() {
        menu item1 = new menu("Menu1", 1000, new ArrayList<>(), MenuType.ELOETEL);
        menuItems.add(item1);
        assertDoesNotThrow(() -> xmlMenu.saveMenuToXML(menuItems));
        List<menu> loadedItems = xmlMenu.menuLoad(raktarItems);
        loadedItems.remove(loadedItems.get(0));
        xmlMenu=new XMLMenu();
        List<menu> updatedItems = xmlMenu.menuLoad(raktarItems);
        assertTrue(updatedItems.isEmpty());
    }

    /**
     * visszaállítjuk a fájl nevét az eredetire és töröljük a teszt fájlt
     */
    @AfterAll
    static void tearDownAfterClass() {
        File myObj = new File(MENU_FILE);
        myObj.delete();
        XMLManager.MENU_FILE = "menu.xml";
    }
}