package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import role.ObservableUserList;
import role.Role;
import role.User;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static xml.XMLManager.USER_FILE;

public class XMLUser {

    private List<User> users = new ObservableUserList(this);

    public void userUpdate(List<User> users){
        try {
            saveUserToXML(users);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a User írásakor", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<User> userLoad() {
        try {
            loadUserFromXML();
        } catch (Exception e) {
            // Ha hiba van, megjelenítjük a betöltött felhasználókat
            List<String> usernameList = users.stream().map(User::getUsername).collect(Collectors.toList());
            JTextArea textArea = new JTextArea(String.join("\n", usernameList));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(300, 200));
            JOptionPane.showMessageDialog(null, scrollPane, "A beolvasott felhasználók", JOptionPane.ERROR_MESSAGE);
        }

        // Ellenőrizzük, hogy van-e OWNER szerepkörű felhasználó
        if (users.stream().noneMatch(user -> user.getRole() == Role.OWNER)) {
            String baseUsername = "default";
            String uniqueUsername = baseUsername;
            int count = 1;

            // Addig keresünk egyedi felhasználónevet, amíg nem találunk egy szabadot
            Set<String> existingUsernames = users.stream().map(User::getUsername).collect(Collectors.toSet());
            while (existingUsernames.contains(uniqueUsername)) {
                uniqueUsername = baseUsername + count;
                count++;
            }

            String defaultPassword = "000"; // 3 karakteres jelszó
            JOptionPane.showMessageDialog(null,
                    "Nincs tulajdonos felhasználó, ezért hozzá lett adva egy jelszó: " + defaultPassword,
                    "Tulajdonos hozzáadása",
                    JOptionPane.INFORMATION_MESSAGE);

            // Új tulajdonos felhasználó hozzáadása a listához
            users.add(new User(uniqueUsername, defaultPassword, Role.OWNER));
        }

        return users;
    }




    public void saveUserToXML(List<User> users) throws Exception  {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("users");
        doc.appendChild(rootElement);

        for (User user : users) {
            if (!user.getPassword().matches("\\d{4}")) {
                continue;
            }
            Element userElem = doc.createElement("user");
            userElem.setAttribute("nev", user.getUsername());

            Element password = doc.createElement("password");
            password.setTextContent(user.getPassword());
            userElem.appendChild(password);

            Element role = doc.createElement("role");
            role.setTextContent(user.getRole().toString());
            userElem.appendChild(role);

            rootElement.appendChild(userElem);
        }

        XMLManager.writeXmlFile(doc, USER_FILE);
    }

    public  void loadUserFromXML() throws Exception {
        Document doc = XMLManager.loadXmlFile(USER_FILE);
        NodeList userNodes = doc.getElementsByTagName("user");

        for (int i = 0; i < userNodes.getLength(); i++) {
            Node node = userNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element userElem = (Element) node;
                String nev = userElem.getAttribute("nev");
                String password = userElem.getElementsByTagName("password").item(0).getTextContent();
                Role role = Role.valueOf(userElem.getElementsByTagName("role").item(0).getTextContent());
                // Ellenőrzés, hogy a név vagy jelszó már szerepel-e a listában
                boolean exists = (users.stream().anyMatch(user -> user.getUsername().equals(nev) || user.getPassword().equals(password))) || !password.matches("\\d{4}");
                if (exists) {
                    throw new Exception("Olvasási hibba");
                } else {
                    users.add(new User(nev, password, role));
                }
            }
        }
    }
}