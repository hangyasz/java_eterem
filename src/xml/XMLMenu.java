package xml;

import menu.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import oszetevok.oszetevok;

import menu.menu;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import oszetevok.*;
import raktar.raktar;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

import static xml.XMLManager.MENU_FILE;

public class XMLMenu {
    private List<menu> menuItems = new ObservableMenuList(this);

    // Menü mentése fájlba
    public void menuUpdate(){
        try {
            saveMenuToXML(menuItems);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a menü írásakor", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menü betöltése fájlból
    public List<menu> menuLoad(List<raktar> raktarItems){
        try {
            return loadMenuFromXML(raktarItems);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a menü olvasásakor", JOptionPane.ERROR_MESSAGE);
            return menuItems;
        }
    }



    public void saveMenuToXML(List<menu> menuItems) throws Exception {
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

        XMLManager.writeXmlFile(doc, MENU_FILE);
    }


    public List<menu> loadMenuFromXML(List<raktar> raktarItems) throws Exception {
        Document doc = XMLManager.loadXmlFile(MENU_FILE);
        NodeList elemList = doc.getElementsByTagName("elem");

        for (int i = 0; i < elemList.getLength(); i++) {
            Node node = elemList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String nev = elem.getAttribute("nev");
                int ar = Integer.parseInt(elem.getElementsByTagName("ar").item(0).getTextContent());
                MenuType type = MenuType.valueOf(elem.getElementsByTagName("type").item(0).getTextContent());
                boolean enabled = Boolean.parseBoolean(elem.getElementsByTagName("enabled").item(0).getTextContent());

                List<oszetevok> oszetevokList = new ObservableOszetevokList(this);
                NodeList oszetevokNodes = elem.getElementsByTagName("oszetevo");
                for (int j = 0; j < oszetevokNodes.getLength(); j++) {
                    Element oszElem = (Element) oszetevokNodes.item(j);
                    String oszNev = oszElem.getAttribute("nev");
                    double mennyiseg = Double.parseDouble(oszElem.getElementsByTagName("mennyiseg").item(0).getTextContent());

                    raktar raktarItem = raktarItems.stream()
                            .filter(r -> r.getNev().equals(oszNev))
                            .findFirst()
                            .orElse(null);

                    if (raktarItem == null) {
                        throw new Exception("Nem található a raktárban az alapanyag: " + oszNev);
                    }
                    oszetevokList.add(new oszetevok(raktarItem, mennyiseg));
                }

                menu menuItem = new menu(nev, ar, oszetevokList, type);
                menuItem.setEnabled(enabled);
                menuItems.add(menuItem);
            }
        }
        return menuItems;
    }

}
