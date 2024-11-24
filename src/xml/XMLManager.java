package xml;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;


import java.io.*;

/**
 * Az osztály a XML fájlok kezelését végzi
 */

public class XMLManager{

    /**
     * Az XML fájlok nevei
     */
    static public String RAKTAR_FILE = "raktar.xml";
    static public String MENU_FILE = "menu.xml";
    static public String ASZTAL_FILE = "asztal.xml";
    static public String USER_FILE = "users.xml";
    static public String TEREM_FILE = "terem.xml";

    /**
     * Segéd metódus XML fájl írásához
     * @param doc Az írandó XML dokumentum
     * @param fileName A fájl neve
     */
    static void writeXmlFile(Document doc, String fileName) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }

    /**
     * Segéd metódus XML fájl beolvasásához
     * @param fileName A beolvasandó fájl neve
     * @return A beolvasott XML dokumentum
     */
    static Document loadXmlFile(String fileName) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(fileName));
        doc.getDocumentElement().normalize();
        return doc;
    }
}
