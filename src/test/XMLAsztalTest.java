package test;

import asztal.asztal;
import menu.MenuType;
import menu.menu;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import terem.terem;
import xml.XMLAsztal;
import xml.XMLManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XMLAsztalTest {

    private static XMLAsztal xmlAsztal;
    private List<asztal> asztalok;
    private List<menu> menuItems;
    private terem terem;
    private static final String ASZTAL_FILE = "asztal_test.xml";

    @BeforeAll
    static void setUpBeforeClass() {
        // Set the file for testing
        XMLManager.ASZTAL_FILE = ASZTAL_FILE;
    }

    @BeforeEach
    void setUp() {
        xmlAsztal = new XMLAsztal();
        asztalok = new ArrayList<>();
        menuItems = new ArrayList<>();
        terem = new terem(10,10);
        // Clear the file content
        try (PrintWriter writer = new PrintWriter(ASZTAL_FILE)) {
            writer.print("");
        } catch (FileNotFoundException e) {
            fail("Nem sikerült törölni a fájl tartalmát");
        }
    }

    /**
     * Megvizsgáljuk, hogy a fájlba kiírt és beolvasott asztal elemek megegyeznek-e
     */
    @Test
    void saveAsztalToXML() {
        menu item1 = new menu("Menu1", 1000, new ArrayList<>(), MenuType.ELOETEL);
        menuItems.add(item1);
        asztal asztal1 = new asztal("Asztal1", 1, 1, menuItems, 1000L);
        asztalok.add(asztal1);
        assertDoesNotThrow(() -> xmlAsztal.saveAsztalToXML(asztalok));
        List<asztal> loadedAsztalok = xmlAsztal.asztalLoad(menuItems, terem);
        assertEquals(1, loadedAsztalok.size());
        assertEquals("Asztal1", loadedAsztalok.get(0).getNev());
        assertEquals(1, loadedAsztalok.get(0).getX());
        assertEquals(1, loadedAsztalok.get(0).getY());
        assertEquals(1000L, loadedAsztalok.get(0).getEretke());
    }

    /**
     * Megvizsgáljuk, hogy a fájl üres akkor az asztal elemek listája is üres-e
     */
    @Test
    void asztalLoad_handlesEmptyFile() {
        assertDoesNotThrow(() -> xmlAsztal.saveAsztalToXML(asztalok));
        List<asztal> loadedAsztalok = xmlAsztal.asztalLoad(menuItems, terem);
        assertTrue(loadedAsztalok.isEmpty());
    }

    /**
     * Ha null értéket kap a kiírás akkor kivételt dob
     */
    @Test
    void saveAsztalToXML_handlesNullItems() {
        assertThrows(NullPointerException.class, () -> xmlAsztal.saveAsztalToXML(null));
    }

    /**
     * Ha a fájlban szerepel egy elem többször akkor kivételt dob
     */
    @Test
    void asztalLoad_handlesDuplicateItems() {
        menu item1 = new menu("Menu1", 1000, new ArrayList<>(), MenuType.ELOETEL);
        menuItems.add(item1);
        asztal asztal1 = new asztal("Asztal1", 1, 1, menuItems, 1000L);
        asztal asztal2 = new asztal("Asztal1", 2, 2, menuItems, 2000L);
        asztalok.add(asztal1);
        asztalok.add(asztal2);
        assertDoesNotThrow(() -> xmlAsztal.saveAsztalToXML(asztalok));
        Exception exception = assertThrows(Exception.class, () -> xmlAsztal.loadAsztalFromXML(menuItems, terem));
        assertEquals("Az asztal neve vagy pozíciója már létezik!", exception.getMessage());
    }

    /**
     * Megvizsgáljuk, hogy az asztal elemet módosítjuk és azt a fájlba írjuk és betöltjük a fájlból és megnézzük, hogy a módosítás sikeres volt-e
     */
    @Test
    void modifyAsztalItem_saveToXMLAndLoad() {
        menu item1 = new menu("Menu1", 1000, new ArrayList<>(), MenuType.ELOETEL);
        menuItems.add(item1);
        asztal asztal1 = new asztal("Asztal1", 1, 1, menuItems, 1000L);
        asztalok.add(asztal1);
        assertDoesNotThrow(() -> xmlAsztal.saveAsztalToXML(asztalok));
        List<asztal> loadedAsztalok = xmlAsztal.asztalLoad(menuItems, terem);
        loadedAsztalok.get(0).setNev("Asztal2");
        xmlAsztal = new XMLAsztal();
        List<asztal> modifiedAsztalok = xmlAsztal.asztalLoad(menuItems, terem);
        assertEquals("Asztal2", modifiedAsztalok.get(0).getNev());
        loadedAsztalok.get(0).setX(2);
        loadedAsztalok.get(0).setY(2);
        loadedAsztalok.get(0).setEretke(2000L);
        xmlAsztal = new XMLAsztal();
        List<asztal> modifiedAsztalok2 = xmlAsztal.asztalLoad(menuItems, terem);
        assertEquals(2, modifiedAsztalok2.get(0).getX());
        assertEquals(2, modifiedAsztalok2.get(0).getY());
        assertEquals(2000L, modifiedAsztalok2.get(0).getEretke());
    }

    /**
     * Megvizsgáljuk, hogy az asztal elemet hozzáadjuk és azt a fájlba írjuk és betöltjük a fájlból és megnézzük, hogy a hozzáadás sikeres volt-e
     */
    @Test
    void addAsztalItem_saveToXMLAndLoad() {
        menu item1 = new menu("Menu1", 1000, new ArrayList<>(), MenuType.ELOETEL);
        menuItems.add(item1);
        asztal asztal1 = new asztal("Asztal1", 1, 1, menuItems, 1000L);
        asztalok.add(asztal1);
        assertDoesNotThrow(() -> xmlAsztal.saveAsztalToXML(asztalok));
        List<asztal> loadedAsztalok = xmlAsztal.asztalLoad(menuItems, terem);
        loadedAsztalok.add(new asztal("Asztal2", 2, 2, new ArrayList<>(), 2000L));
        xmlAsztal = new XMLAsztal();
        List<asztal> updatedAsztalok = xmlAsztal.asztalLoad(menuItems, terem);
        assertEquals(2, updatedAsztalok.size());
        assertEquals("Asztal2", updatedAsztalok.get(1).getNev());
    }

    /**
     * Megvizsgáljuk, hogy az asztal elemet töröljük és azt a fájlba írjuk és betöltjük a fájlból és megnézzük, hogy a törlés sikeres volt-e
     */
    @Test
    void deleteAsztalItem_saveToXMLAndLoad() {
        menu item1 = new menu("Menu1", 1000, new ArrayList<>(), MenuType.ELOETEL);
        menuItems.add(item1);
        asztal asztal1 = new asztal("Asztal1", 1, 1, menuItems, 1000L);
        asztalok.add(asztal1);
        assertDoesNotThrow(() -> xmlAsztal.saveAsztalToXML(asztalok));
        List<asztal> loadedAsztalok = xmlAsztal.asztalLoad(menuItems, terem);
        loadedAsztalok.remove(loadedAsztalok.get(0));
        xmlAsztal = new XMLAsztal();
        List<asztal> updatedAsztalok = xmlAsztal.asztalLoad(menuItems, terem);
        assertTrue(updatedAsztalok.isEmpty());
    }

    /**
     * visszaállítjuk a fájl nevét az eredetire és töröljük a teszt fájlt
     */

    @AfterAll
    static void tearDownAfterClass() {
        File myObj = new File(ASZTAL_FILE);
        myObj.delete();
        XMLManager.ASZTAL_FILE = "asztal.xml";
    }
}