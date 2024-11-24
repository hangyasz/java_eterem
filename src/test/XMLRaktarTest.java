package test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raktar.raktar;
import xml.XMLManager;
import xml.XMLRaktar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XMLRaktarTest {

    private static XMLRaktar xmlRaktar;
    private List<raktar> raktarItems;
    private static final String RAKTAR_FILE = "raktar_test.xml";

    @BeforeAll
    static void setUpBeforeClass() {
        // Set the file for testing
        XMLManager.RAKTAR_FILE = RAKTAR_FILE;
    }

    @BeforeEach
    void setUp() {
        xmlRaktar = new XMLRaktar();
        raktarItems = new ArrayList<>();
        // Clear the file content
        try (PrintWriter writer = new PrintWriter(RAKTAR_FILE)) {
            writer.print("");
        } catch (FileNotFoundException e) {
            fail("Nem sikerült törölni a fájl tartalmát");
        }
    }

    /**
     * Megvizsgáljuk,hogy a fájlba kiírt és beolvasott raktár elemek megegyeznek-e
     */

    @Test
    void saveRaktarToXML() {
        raktar item1 = new raktar("Item1", "kg", 10.0);
        raktar item2 = new raktar("Item2", "kg", 20.0);
        raktarItems.add(item1);
        raktarItems.add(item2);
        assertDoesNotThrow(() -> xmlRaktar.saveRaktarToXML(raktarItems));
        List<raktar> loadedItems = xmlRaktar.raktarLoad();
        assertEquals(2, loadedItems.size());
        assertEquals("Item1", loadedItems.get(0).getNev());
        assertEquals("kg", loadedItems.get(0).getMertekegyseg());
        assertEquals(10.0, loadedItems.get(0).getMennyiseg());
        assertEquals("Item2", loadedItems.get(1).getNev());
        assertEquals("kg", loadedItems.get(1).getMertekegyseg());
        assertEquals(20.0, loadedItems.get(1).getMennyiseg());
    }

    /**
     * Megvizsgáljuk,hogy a fálj ures akkor a raktár elemek listája is üres-e
     */



    @Test
    void raktarLoad_handlesEmptyFile() {
        assertDoesNotThrow(() -> xmlRaktar.saveRaktarToXML(raktarItems));
        List<raktar> loadedItems = xmlRaktar.raktarLoad();
        assertTrue(loadedItems.isEmpty());
    }

    /**
     * Ha null értéket kap a kiírás akkor kivételt dob
     */
    @Test
    void saveRaktarToXML_handlesNullItems() {
        assertThrows(NullPointerException.class, () -> xmlRaktar.saveRaktarToXML(null));
    }

    /**
     * Ha a fájlban szerepel egy elem többször akkor kivételt dob
     */
    @Test
    void raktarLoad_handlesDuplicateItems() {
        raktar item1 = new raktar("Item1", "kg", 10.0);
        raktar item2 = new raktar("Item1", "kg", 20.0);
        raktarItems.add(item1);
        raktarItems.add(item2);
        assertDoesNotThrow(() -> xmlRaktar.saveRaktarToXML(raktarItems));
        Exception exception = assertThrows(Exception.class, () -> xmlRaktar.loadRaktarFromXML());
        assertEquals("Item1 már szerepel a raktárban", exception.getMessage());
    }

    /**
     * Ha a mennyiség negatív értéket kap akkor 0 értéket tesz bele a listába
     */

    @Test
    void raktarLoad_handlesNegativeQuantity() {
        raktar item1 = new raktar("Item1", "kg", -10.0);
        raktarItems.add(item1);
        assertDoesNotThrow(() -> xmlRaktar.saveRaktarToXML(raktarItems));
        List<raktar> loadedItems = xmlRaktar.raktarLoad();
        assertEquals(0.0, loadedItems.get(0).getMennyiseg());
    }

    @Test
    void modifyRaktarItem_saveToXMLAndLoad() {
        raktar item1 = new raktar("Item1", "kg", 10.0);
        raktarItems.add(item1);
        assertDoesNotThrow(() -> xmlRaktar.saveRaktarToXML(raktarItems));
        List<raktar> loadedItems = xmlRaktar.raktarLoad();
        loadedItems.get(0).addMennyiseg(5.0);
        xmlRaktar=new XMLRaktar();
        List<raktar> modifiedItems = xmlRaktar.raktarLoad();
        assertEquals(15.0, modifiedItems.get(0).getMennyiseg());
        loadedItems.get(0).addMennyiseg(-5.0);
        xmlRaktar=new XMLRaktar();
        List<raktar> modifiedItems2 = xmlRaktar.raktarLoad();
        assertEquals(10.0, modifiedItems2.get(0).getMennyiseg());
    }

    @Test
    void addRaktarItem_saveToXMLAndLoad() {
        raktar item1 = new raktar("Item1", "kg", 10.0);
        raktarItems.add(item1);
        assertDoesNotThrow(() -> xmlRaktar.saveRaktarToXML(raktarItems));
        xmlRaktar=new XMLRaktar();
        List<raktar> loadedItems = xmlRaktar.raktarLoad();
        loadedItems.add(new raktar("Item2", "kg", 20.0));
        xmlRaktar=new XMLRaktar();
        List<raktar> updatedItems = xmlRaktar.raktarLoad();
        assertEquals(2, updatedItems.size());
        assertEquals("Item2", updatedItems.get(1).getNev());
        assertEquals(20.0, updatedItems.get(1).getMennyiseg());
    }

    @Test
    void deleteRaktarItem_saveToXMLAndLoad() {
        raktar item1 = new raktar("Item1", "kg", 10.0);
        raktarItems.add(item1);
        assertDoesNotThrow(() -> xmlRaktar.saveRaktarToXML(raktarItems));
        List<raktar> loadedItems = xmlRaktar.raktarLoad();
        loadedItems.remove(0);
        assertDoesNotThrow(() -> xmlRaktar.saveRaktarToXML(loadedItems));
        List<raktar> updatedItems = xmlRaktar.raktarLoad();
        assertTrue(updatedItems.isEmpty());
    }



    /**
     * visszaállítjuk a fájl nevét az eredetire és töröljük a teszt fájlt
     */
    @AfterAll
    static void tearDownAfterClass() {
        File myObj = new File(RAKTAR_FILE);
        myObj.delete();
        XMLManager.RAKTAR_FILE = "raktar.xml";
    }
}