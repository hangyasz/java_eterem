package xml;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import raktar.raktar;
import menu.*;
import oszetevok.oszetevok;
import asztal.asztal;
import role.Role;
import role.User;


public class XMLManager {
    private static final String RAKTAR_FILE = "raktar.xml";
    private static final String MENU_FILE = "menu.xml";
    private static final String ASZTAL_FILE = "asztal.xml";

    public void saveRaktarToXML(List<raktar> raktarItems) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("raktar");
            doc.appendChild(rootElement);

            for (raktar item : raktarItems) {
                Element elem = doc.createElement("elem");
                elem.setAttribute("nev", item.getNev());

                Element mertekegyseg = doc.createElement("mertekegyseg");
                mertekegyseg.setTextContent(item.getMertekegyseg());
                elem.appendChild(mertekegyseg);

                Element mennyiseg = doc.createElement("mennyiseg");
                mennyiseg.setTextContent(String.valueOf(item.getMennyiseg()));
                elem.appendChild(mennyiseg);

                rootElement.appendChild(elem);
            }

            writeXmlFile(doc, RAKTAR_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<raktar> loadRaktarFromXML() {
        List<raktar> raktarItems = new ArrayList<>();
        try {
            Document doc = loadXmlFile(RAKTAR_FILE);
            NodeList elemList = doc.getElementsByTagName("elem");

            for (int i = 0; i < elemList.getLength(); i++) {
                Node node = elemList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    String nev = elem.getAttribute("nev");

                    String mertekegyseg = elem.getElementsByTagName("mertekegyseg").item(0).getTextContent();
                    double mennyiseg = Double.parseDouble(elem.getElementsByTagName("mennyiseg").item(0).getTextContent());

                    raktarItems.add(new raktar(nev, mertekegyseg, mennyiseg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return raktarItems;
    }


    public void saveMenuToXML(List<menu> menuItems) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("menu");
            doc.appendChild(rootElement);

            for (menu item : menuItems) {
                Element elem = doc.createElement("elem");
                elem.setAttribute("nev", item.getNev());

                Element ar = doc.createElement("ar");
                ar.setTextContent(String.valueOf(item.getAr()));
                elem.appendChild(ar);

                Element type = doc.createElement("type");
                type.setTextContent(item.getType().toString());
                elem.appendChild(type);

                Element enabled = doc.createElement("enabled");
                enabled.setTextContent(String.valueOf(item.isEnabled()));
                elem.appendChild(enabled);

                Element oszetevok = doc.createElement("oszetevok");
                for (oszetevok oszetevo : item.getOszetevok()) {
                    Element oszElem = doc.createElement("oszetevo");
                    oszElem.setAttribute("nev", oszetevo.getNev());

                    Element mennyiseg = doc.createElement("mennyiseg");
                    mennyiseg.setTextContent(String.valueOf(oszetevo.getMennyiseg()));
                    oszElem.appendChild(mennyiseg);

                    oszetevok.appendChild(oszElem);
                }
                elem.appendChild(oszetevok);

                rootElement.appendChild(elem);
            }

            writeXmlFile(doc, MENU_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveAsztalToXML(List<asztal> asztalok) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("asztalok");
            doc.appendChild(rootElement);

            for (asztal item : asztalok) {
                Element asztalElem = doc.createElement("asztal");
                asztalElem.setAttribute("nev", item.getNev());

                Element pozicio = doc.createElement("pozicio");
                Element x = doc.createElement("x");
                x.setTextContent(String.valueOf(item.getX()));
                pozicio.appendChild(x);

                Element y = doc.createElement("y");
                y.setTextContent(String.valueOf(item.getY()));
                pozicio.appendChild(y);

                asztalElem.appendChild(pozicio);

                Element ertek = doc.createElement("ertek");
                ertek.setTextContent(String.valueOf(item.getEretke()));
                asztalElem.appendChild(ertek);

                Element rendelesek = doc.createElement("rendelesek");
                for (menu rendeles : item.getRendelesek()) {
                    Element rendElem = doc.createElement("rendeles");
                    rendElem.setAttribute("nev", rendeles.getNev());
                    rendelesek.appendChild(rendElem);
                }
                asztalElem.appendChild(rendelesek);

                rootElement.appendChild(asztalElem);
            }

            writeXmlFile(doc, ASZTAL_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<menu> loadMenuFromXML(List<raktar> raktarItems) {
        List<menu> menuItems = new ArrayList<>();
        try {
            Document doc = loadXmlFile(MENU_FILE);
            NodeList elemList = doc.getElementsByTagName("elem");

            for (int i = 0; i < elemList.getLength(); i++) {
                Node node = elemList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    String nev = elem.getAttribute("nev");
                    int ar = Integer.parseInt(elem.getElementsByTagName("ar").item(0).getTextContent());
                    MenuType type = MenuType.valueOf(elem.getElementsByTagName("type").item(0).getTextContent());
                    boolean enabled = Boolean.parseBoolean(elem.getElementsByTagName("enabled").item(0).getTextContent());

                    List<oszetevok> oszetevokList = new ArrayList<>();
                    NodeList oszetevokNodes = elem.getElementsByTagName("oszetevo");
                    for (int j = 0; j < oszetevokNodes.getLength(); j++) {
                        Element oszElem = (Element) oszetevokNodes.item(j);
                        String oszNev = oszElem.getAttribute("nev");
                        double mennyiseg = Double.parseDouble(oszElem.getElementsByTagName("mennyiseg").item(0).getTextContent());

                        raktar raktarItem = raktarItems.stream()
                                .filter(r -> r.getNev().equals(oszNev))
                                .findFirst()
                                .orElse(null);

                        if (raktarItem != null) {
                            oszetevokList.add(new oszetevok(raktarItem, mennyiseg));
                        }
                    }

                    menu menuItem = new menu(nev, ar, oszetevokList, type);
                    menuItem.setEnabled(enabled);
                    menuItems.add(menuItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuItems;
    }


    // Asztalok betöltése fájlból
    public List<asztal> loadAsztalFromXML(List<menu> menuItems) {
        List<asztal> asztalok = new ArrayList<>();
        try {
            Document doc = loadXmlFile(ASZTAL_FILE);
            NodeList asztalNodes = doc.getElementsByTagName("asztal");

            for (int i = 0; i < asztalNodes.getLength(); i++) {
                Node node = asztalNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element asztalElem = (Element) node;
                    String nev = asztalElem.getAttribute("nev");

                    // Pozíció betöltése
                    Element pozicioElem = (Element) asztalElem.getElementsByTagName("pozicio").item(0);
                    int x = Integer.parseInt(pozicioElem.getElementsByTagName("x").item(0).getTextContent());
                    int y = Integer.parseInt(pozicioElem.getElementsByTagName("y").item(0).getTextContent());

                    // Érték betöltése
                    int ertek = Integer.parseInt(asztalElem.getElementsByTagName("ertek").item(0).getTextContent());

                    // Rendelések betöltése
                    List<menu> rendelesek = new ArrayList<>();
                    NodeList rendelesNodes = asztalElem.getElementsByTagName("rendeles");
                    for (int j = 0; j < rendelesNodes.getLength(); j++) {
                        Element rendElem = (Element) rendelesNodes.item(j);
                        String rendNev = rendElem.getAttribute("nev");

                        // Kapcsolódó menüelem keresése
                        menu rendeles = menuItems.stream()
                                .filter(menuItem -> menuItem.getNev().equals(rendNev))
                                .findFirst()
                                .orElse(null);

                        if (rendeles != null) {
                            rendelesek.add(rendeles);
                        }
                    }

                    // Asztal példány létrehozása és listához adása
                    asztal asztalItem = new asztal(nev, x, y);
                    asztalItem.setEretke(ertek);
                    asztalItem.SetRendelesek(rendelesek);
                    asztalok.add(asztalItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asztalok;
    }




    // Segéd metódus XML fájl írásához
    void writeXmlFile(Document doc, String fileName) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }

    // Segéd metódus XML fájl betöltéséhez
    Document loadXmlFile(String fileName) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(fileName));
        doc.getDocumentElement().normalize();
        return doc;
    }
}
