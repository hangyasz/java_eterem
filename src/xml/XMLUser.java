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
import java.util.ArrayList;
import java.util.List;

import static xml.XMLManager.USER_FILE;

public class XMLUser {

    List<User> users = new ObservableUserList(this);

    public void userUpdate(List<User> users){
        try {
            saveUserToXML(users);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a User írásakor", JOptionPane.ERROR_MESSAGE);

        }
    }

    public List<User> userLoad(){
        try {
            return loadUserFromXML();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a User olvasásakor", JOptionPane.ERROR_MESSAGE);
            return users;
        }
    }

    public void saveUserToXML(List<User> users) throws Exception  {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("users");
        doc.appendChild(rootElement);

        for (User user : users) {
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


    public List<User> loadUserFromXML() throws Exception {
        Document doc =XMLManager.loadXmlFile(USER_FILE);
        NodeList userNodes = doc.getElementsByTagName("user");

        for (int i = 0; i < userNodes.getLength(); i++) {
            Node node = userNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element userElem = (Element) node;
                String nev = userElem.getAttribute("nev");
                String password = userElem.getElementsByTagName("password").item(0).getTextContent();
                Role role = Role.valueOf(userElem.getElementsByTagName("role").item(0).getTextContent());


                users.add(new User(nev, password, role));
            }
        }
        return users;
    }
}
