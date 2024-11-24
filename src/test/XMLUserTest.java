package test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;
import role.Role;
import role.User;
import xml.XMLManager;
import xml.XMLUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XMLUserTest {

    private static XMLUser xmlUser;
    private List<User> users;
    private static XMLManager xmlManager=new XMLManager();

    @BeforeAll
    static void setUpBeforeClass() {
        //átállítjuk a teszteléshez a fájlt
        xmlManager.USER_FILE = "users_test.xml";
    }

    @BeforeEach
    void setUp() {
        //a teszteléshez szükséges inicializálás
        xmlUser = new XMLUser();
        users = new ArrayList<>();
        //töröljük a fájl tartalmát
        try (PrintWriter writer = new PrintWriter(xmlManager.USER_FILE)) {
            writer.print("");
        } catch (FileNotFoundException e) {
            fail("Nem sikerült törölni a fájl tartalmát");
        }

    }

    /**
     * A felhasználók listáját kiírjuk a fájlba és betöltjük a fájlból megvizsgáljuk, hogy a kiírt és beolvasott felhasználók megegyeznek-e
     */
    @Test
    void saveUserToXML() {
        User user = new User("0000", "0000", Role.Tuljonos);
        User user1 = new User("1223", "1234", Role.Pincér);
        User user2 = new User("1224", "1235", Role.Pincér);
        users.add(user);
        users.add(user1);
        users.add(user2);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        List<User>loadedUsers=xmlUser.userLoad();
        assertEquals(3, loadedUsers.size());
        assertEquals("1223", loadedUsers.get(1).getUsername());
        assertEquals("1234", loadedUsers.get(1).getPassword());
        assertEquals(Role.Pincér, loadedUsers.get(1).getRole());
        assertEquals("1224", loadedUsers.get(2).getUsername());
        assertEquals("1235", loadedUsers.get(2).getPassword());
        assertEquals(Role.Pincér, loadedUsers.get(2).getRole());
    }


    /**
     * Azt vizsgáljuk, hogy a fájlban nincs felhasználó akkor a betöltés hibát dob
     */
    @Test
    void loadUserFromXML_handlesEmptyFile() {
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        Exception exception=  assertThrows(Exception.class, () -> xmlUser.loadUserFromXML());
        assertEquals("Nincs felhasználó az XML fájlban.", exception.getMessage());

    }

    /**
     * Azt vizsgáljuk, hogy a lista null értéket tartalmaz-e a fájlba írása közben dobb e hibát
     */

    @Test
    void saveUserToXML_handlesNullUsers() {
        assertThrows(NullPointerException.class, () -> xmlUser.saveUserToXML(null));
    }


    /**
     * Azt vizsgáljuk, hogy a fájlban duplán szereplő felhasználóneve akkor dob e hibát
     */

    @Test
    void loadUserFromXML_handlesDuplicateUsernames() {
        User user=new User("0000","0000",Role.Tuljonos);
        User user1 = new User("1223", "1234", Role.Pincér);
        User user2 = new User("1223", "1235", Role.Pincér);
        users.add(user);
        users.add(user1);
        users.add(user2);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        Exception exception =assertThrows(Exception.class, () -> xmlUser.loadUserFromXML());
        assertEquals("Olvasási hibba", exception.getMessage());
    }


    /**
     * Azt vizsgáljuk, hogy a fájlban duplán szereplő jelszó akkor dob e hibát
     */
    @Test
    void loadUserFromXML_handlesDuplicatePasswords() {
        User user=new User("0000","0000",Role.Tuljonos);
        User user1 = new User("1223", "1234", Role.Pincér);
        User user2 = new User("1224", "1234", Role.Pincér);
        users.add(user);
        users.add(user1);
        users.add(user2);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        Exception exception =assertThrows(Exception.class, () -> xmlUser.loadUserFromXML());
        assertEquals("Olvasási hibba", exception.getMessage());
    }



    /**
     * Azt vizsgáljuk, hogy a fájlban rosz formátumú jelszó esetén dob e hibát
     */

    @Test
    void loadUserFromXML_handlesNonNumericPIN() {
        User user1 = new User("1223", "abcd", Role.Pincér);
        users.add(user1);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        Exception exception = assertThrows(Exception.class, () -> xmlUser.loadUserFromXML());
        assertEquals("Olvasási hibba", exception.getMessage());
    }


    /**
     * Azt vizsgáljuk, hogy modosítjuk a felhasználónevet és azt a fájlba írjuk és betöltjük a fájlból és megnézzük, hogy a módosítás sikeres volt-e
     */

    @Test
    void modifyUsername_saveToXMLAndLoad() {
        User user = new User("0000", "0000", Role.Tuljonos);
        User user1 = new User("1223", "1234", Role.Pincér);
        users.add(user);
        users.add(user1);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        XMLUser xmlUser_olvas = new XMLUser();
        users=xmlUser_olvas.userLoad();
        users.get(1).setUsername("1224");
        List<User> loadedUsers =  xmlUser.userLoad();
        assertEquals(2, loadedUsers.size());
        assertEquals("1224", loadedUsers.get(1).getUsername());
        assertEquals("1234", loadedUsers.get(1).getPassword());
        assertEquals(Role.Pincér, loadedUsers.get(1).getRole());

    }

    /**
     * Azt vizsgáljuk, hogy modosítjuk a jelszót és azt a fájlba írjuk és betöltjük a fájlból és megnézzük, hogy a módosítás sikeres volt-e
     */

    @Test
    void modifyPassword_saveToXMLAndLoad() {
        User user = new User("0000", "0000", Role.Tuljonos);
        User user1 = new User("1223", "1234", Role.Pincér);
        users.add(user);
        users.add(user1);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        XMLUser xmlUser_olvas = new XMLUser();
        users=xmlUser_olvas.userLoad();
        users.get(1).setPassword("1225");
        List<User> loadedUsers =  xmlUser.userLoad();
        assertEquals(2, loadedUsers.size());
        assertEquals("1223", loadedUsers.get(1).getUsername());
        assertEquals("1225", loadedUsers.get(1).getPassword());
        assertEquals(Role.Pincér, loadedUsers.get(1).getRole());
    }

    /**
     * Azt vizsgáljuk, hogy modosítjuk a szerepés és azt a fájlba írjuk és betöltjük a fájlból és megnézzük, hogy a módosítás sikeres volt-e
     */

    @Test
    void modifyRole_saveToXMLAndLoad() {
        User user = new User("0000", "0000", Role.Tuljonos);
        User user1 = new User("1223", "1234", Role.Pincér);
        users.add(user);
        users.add(user1);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        XMLUser xmlUser_olvas = new XMLUser();
        users=xmlUser_olvas.userLoad();
        users.get(1).setRole(Role.Szakács);
        List<User> loadedUsers =  xmlUser.userLoad();
        assertEquals(2, loadedUsers.size());
        assertEquals("1223", loadedUsers.get(1).getUsername());
        assertEquals("1234", loadedUsers.get(1).getPassword());
        assertEquals(Role.Szakács, loadedUsers.get(1).getRole());
    }


    /**
     * Azt vizsgáljuk, hogy a felhasználók listáját módosítjuk és azt a fájlba írjuk és betöltjük a fájlból és megnézzük, hogy a módosítás sikeres volt-e
     */

    @Test
    void list_chamge_saveToXMLAndLoad() {
        User user = new User("0000", "0000", Role.Tuljonos);
        User user1 = new User("1223", "1234", Role.Pincér);
        users.add(user);
        users.add(user1);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        XMLUser xmlUser_olvas = new XMLUser();
        List<User> loadedUsers=xmlUser_olvas.userLoad();
        assertEquals(2, loadedUsers.size());
        loadedUsers.add(new User("1224", "1235", Role.Pincér));
        XMLUser xmlUser_olvas2 = new XMLUser();
        List<User> loadedUsers2=xmlUser_olvas2.userLoad();
        assertEquals(loadedUsers.size(), loadedUsers2.size());
        assertEquals(loadedUsers.get(1).getUsername(), loadedUsers2.get(1).getUsername());
        loadedUsers.add(new User("2222", "2222", Role.Tuljonos));
        loadedUsers.add(new User("3333", "3333", Role.Üzlet_Vezető));
        loadedUsers.remove(loadedUsers.get(0));
        xmlUser_olvas2 = new XMLUser();
        loadedUsers2=xmlUser_olvas2.userLoad();
        assertEquals(loadedUsers.size(), loadedUsers2.size());
        assertEquals(loadedUsers.get(1).getUsername(), loadedUsers2.get(1).getUsername());
        assertEquals(loadedUsers.get(2).getUsername(), loadedUsers2.get(2).getUsername());
        assertEquals(loadedUsers.get(3).getUsername(), loadedUsers2.get(3).getUsername());
    }

    @AfterAll
    static void tearDownAfterClass() {
        File myObj = new File(xmlManager.USER_FILE);
        myObj.delete();
        xmlManager.USER_FILE = "users.xml";
//zol
    }
}