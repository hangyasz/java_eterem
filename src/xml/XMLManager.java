package xml;

import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import raktar.raktar;
import asztal.asztal;
import role.Role;
import role.User;


public class XMLManager{
    static public final String RAKTAR_FILE = "raktar.xml";
    static public final String MENU_FILE = "menu.xml";
    static public final String ASZTAL_FILE = "asztal.xml";
    static public  String USER_FILE = "users.xml";

    // Segéd metódus XML fájl írásához
    static void writeXmlFile(Document doc, String fileName) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }

    // Segéd metódus XML fájl betöltéséhez
    static Document loadXmlFile(String fileName) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(fileName));
        doc.getDocumentElement().normalize();
        return doc;
    }
}
