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
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static xml.XMLManager.MENU_FILE;

/**
 * Az osztály a menü XML fáljának kezelését végzi
 */

public class XMLMenu {
    private List<menu> menuItems = new ObservableMenuList(this);

    /**
     * Menü friss írása fájlba hibbes esetén hibaüzenet megjelenítése
     */
    public void menuUpdate(){
        try {
            saveMenuToXML(menuItems);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a menü írásakor", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Menü betöltése fájlból hibbes esetén hibaüzenet megjelenít a menü elemek listáját
     * @param raktarItems Raktár elemek listája
     * @return A menü elemek listája
     */
    public List<menu> menuLoad(List<raktar> raktarItems){
        try {
            return loadMenuFromXML(raktarItems);
        } catch (Exception e) {
            List<String> menuName = menuItems.stream().map(menu::getNev).collect(Collectors.toList());
            JTextArea textArea = new JTextArea(String.join("\n", menuName));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(300, 200));
            JOptionPane.showMessageDialog(null,  scrollPane, "Hiba a menü olvasásakor", JOptionPane.ERROR_MESSAGE);
            return menuItems;
        }
    }


    /**
     * Menü elemek fájlba írása
     * @param menuItems A menü elemek listája
     * @throws Exception Hiba esetén kivétel dobása
     */
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


    /**
     * Menü elemek betöltése fájlból
     * @param raktarItems Raktár elemek listája
     * @return A menü elemek listája
     * @throws Exception Hiba esetén kivétel dobása
     * ha nem található az alapanyag a raktárban vagy az asztal neve már létezik kivételt dob
     */
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
                // Az összetevők beolvasása ha nem található az alapanyag a raktárban kivételt dob kivéa ha ures a lista
                for (int j = 0; j < oszetevokNodes.getLength(); j++) {
                    Element oszElem = (Element) oszetevokNodes.item(j);
                    String oszNev = oszElem.getAttribute("nev");
                    double mennyiseg = Double.parseDouble(oszElem.getElementsByTagName("mennyiseg").item(0).getTextContent());

                    raktar raktarItem = raktarItems.stream()
                            .filter(r -> r.getNev().equals(oszNev))
                            .findFirst()
                            .orElse(null);

                    if (raktarItem == null) {
                        throw new Exception("Nem található a raktárban az alapanyag");
                    }
                    oszetevokList.add(new oszetevok(raktarItem, mennyiseg));
                }

                boolean exists = menuItems.stream().anyMatch(menuItem -> menuItem.getNev().equals(nev));
                if (exists) {
                    throw new Exception("Az asztal neve már létezik");
                }

                menu menuItem = new menu(nev, ar, oszetevokList, type);
                menuItem.setEnabled(enabled);
                menuItems.add(menuItem);
            }
        }
        return menuItems;
    }

}
