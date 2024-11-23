package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import role.Role;
import role.User;
import xml.XMLManager;
import xml.XMLUser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XMLUserTest {
    /*

    private static XMLUser xmlUser;
    private List<User> users;

    @BeforeAll
    static void setUpBeforeClass() {
        xmlUser = new XMLUser();
    }

    @BeforeEach
    void setUp() {

        users = new ArrayList<>();
    }

    @Test
    void saveUserToXML_savesCorrectly() {
        User user1 = new User("1223", "1234", Role.WAITER);
        User user2 = new User("1224", "1235", Role.WAITER);
        users.add(user1);
        users.add(user2);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        List<User> loadedUsers = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());
        assertEquals(2, loadedUsers.size());
        assertEquals("1223", loadedUsers.get(0).getUsername());
        assertEquals("1234", loadedUsers.get(0).getPassword());
        assertEquals(Role.WAITER, loadedUsers.get(0).getRole());
        assertEquals("1224", loadedUsers.get(1).getUsername());
        assertEquals("1235", loadedUsers.get(1).getPassword());
        assertEquals(Role.WAITER, loadedUsers.get(1).getRole());
    }

    @Test
    void loadUserFromXML_handlesEmptyFile() {
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(new ArrayList<>()));
        List<User> loadedUsers = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());
        assertEquals(1, loadedUsers.size());
        assertEquals("admin", loadedUsers.get(0).getUsername());
        assertEquals("0000", loadedUsers.get(0).getPassword());
        assertEquals(Role.ADMIN, loadedUsers.get(0).getRole());
    }



    @Test
    void saveUserToXML_handlesNullUsers() {
        assertThrows(NullPointerException.class, () -> xmlUser.saveUserToXML(null));
    }



    @Test
    void loadUserFromXML_handlesDuplicateUsernamesOrPasswords() {
        User user1 = new User("1223", "1234", Role.WAITER);
        User user2 = new User("1223", "1235", Role.WAITER);
        users.add(user1);
        users.add(user2);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        List<User> loadedUsers = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());
        assertEquals(2, loadedUsers.size());
        assertEquals("1223", loadedUsers.get(0).getUsername());
        assertEquals("1234", loadedUsers.get(0).getPassword());
        assertEquals(Role.WAITER, loadedUsers.get(0).getRole());
    }

    @Test
    void loadUserFromXML_handlesInvalidPINLength() {
        User user1 = new User("1223", "12345", Role.WAITER);
        users.add(user1);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        List<User> loadedUsers = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());
        assertEquals(1, loadedUsers.size());
        assertEquals("admin", loadedUsers.get(0).getUsername());
        assertEquals("0000", loadedUsers.get(0).getPassword());
        assertEquals(Role.ADMIN, loadedUsers.get(0).getRole());
    }

    @Test
    void loadUserFromXML_handlesNonNumericPIN() {
        User user1 = new User("1223", "abcd", Role.WAITER);
        users.add(user1);
        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));
        List<User> loadedUsers = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());
        assertEquals(1, loadedUsers.size());
        assertEquals("admin", loadedUsers.get(0).getUsername());
        assertEquals("0000", loadedUsers.get(0).getPassword());
        assertEquals(Role.ADMIN, loadedUsers.get(0).getRole());
    }

    @Test
    void modifyUsername_saveToXMLAndLoad() {
        User user = new User("1223", "1234", Role.WAITER);
        users.add(user);

        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));

        user.setUsername("NEW_USERNAME");

        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));

        List<User> loadedUsers = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());

        assertEquals(2, loadedUsers.size());
        assertEquals("NEW_USERNAME", loadedUsers.get(0).getUsername());
        assertEquals("1234", loadedUsers.get(0).getPassword());
        assertEquals(Role.WAITER, loadedUsers.get(0).getRole());
        assertEquals(Role.ADMIN, loadedUsers.get(1).getRole());
    }

    @Test
    void modifyPassword_saveToXMLAndLoad() {
        User user = new User("1223", "1234", Role.WAITER);
        users.add(user);

        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));

        user.setPassword("4321");

        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));

        List<User> loadedUsers = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());

        assertEquals(2, loadedUsers.size());
        assertEquals("1223", loadedUsers.get(0).getUsername());
        assertEquals("4321", loadedUsers.get(0).getPassword());
        assertEquals(Role.WAITER, loadedUsers.get(0).getRole());
        assertEquals(Role.ADMIN, loadedUsers.get(1).getRole());
    }

    @Test
    void modifyRole_saveToXMLAndLoad() {
        User user = new User("1223", "1234", Role.WAITER);
        users.add(user);

        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));

        user.setRole(Role.ADMIN);

        assertDoesNotThrow(() -> xmlUser.saveUserToXML(users));

        List<User> loadedUsers = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());

        assertEquals(1, loadedUsers.size());
        assertEquals("1223", loadedUsers.get(0).getUsername());
        assertEquals("1234", loadedUsers.get(0).getPassword());
        assertEquals(Role.ADMIN, loadedUsers.get(0).getRole());
    }

    @Test
    void list_chamge_saveToXMLAndLoad() {
        List<User> loadedUsers = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());
        assertEquals(1, loadedUsers.size());
        loadedUsers.add(new User("1223", "1234", Role.WAITER));
        List<User> loadedUsers2 = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());
        assertEquals(loadedUsers.size(), loadedUsers2.size());
        assertEquals(loadedUsers.get(1).getUsername(), loadedUsers2.get(1).getUsername());
        loadedUsers.add(new User("1224", "1235", Role.WAITER));
        loadedUsers.add(new User("1225", "1236", Role.CHEF));
        loadedUsers.remove(loadedUsers.get(0));
        loadedUsers2 = assertDoesNotThrow(() -> xmlUser.loadUserFromXML());
        assertEquals(loadedUsers.size(), loadedUsers2.size());
        assertEquals(loadedUsers.get(1).getUsername(), loadedUsers2.get(1).getUsername());
    }


*/
}