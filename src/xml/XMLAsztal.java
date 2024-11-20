package xml;

import menu.*;

import asztal.asztal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;


import static xml.XMLManager.ASZTAL_FILE;

public class XMLAsztal {

    // Asztalok mentése fájlba
    void asztaUpdae(List<asztal> asztalok ){
        try {
            saveAsztalToXML(asztalok);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a asztal írásakor", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Asztalok betöltése fájlból
    public List<asztal> asztalLoad(List<menu> menuItems){
        try {
            return loadAsztalFromXML(menuItems);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a aztal olvasásakor", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public void saveAsztalToXML(List<asztal> asztalok) throws Exception {
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

        XMLManager.writeXmlFile(doc, ASZTAL_FILE);
    }

    // Asztalok betöltése fájlból
    public List<asztal> loadAsztalFromXML(List<menu> menuItems) throws Exception {
        List<asztal> asztalok = new ArrayList<>();
        Document doc = XMLManager.loadXmlFile(ASZTAL_FILE);
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
        return asztalok;
    }

}
