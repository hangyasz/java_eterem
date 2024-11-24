package xml;

import menu.*;
import asztal.*;
import role.User;
import terem.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static xml.XMLManager.ASZTAL_FILE;


/**
 * Az osztály az asztalok XML fáljának kezelését végzi
 */
public class XMLAsztal {
    private List<asztal> asztalok = new ObservableAsztalList(this);

    /**
     * Asztalok frissítése fájlba kiírjuk az asztal adatokat a fájlba az asztalok listából
     */
    public void asztalUpdate() {
        try {
            saveAsztalToXML();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Hiba az asztalok írásakor", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Asztalok betöltése fájból
     * @param menuItems Menüelemek listája
     * @param terem A terem adatai
     * @return Az asztalok listája
     * Megpróbálja betölteni az asztalokat a fájlból, ha nem sikerül, akkor kiírja a betöltött asztalok neveit
     */
    public List<asztal> asztalLoad(List<menu> menuItems,terem terem) {
        try {
            loadAsztalFromXML(menuItems, terem);
            asztalok.clear();
            asztalok.addAll(asztalok);
            return asztalok;
        } catch (Exception e) {
            List<String> asztalname = asztalok.stream().map(asztal::getNev).collect(Collectors.toList());
            JTextArea textArea = new JTextArea(String.join("\n", asztalname));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(300, 200));
            JOptionPane.showMessageDialog(null, scrollPane, "Hiba az asztalok olvasásakor", JOptionPane.ERROR_MESSAGE);
            return asztalok;
        }
    }



    /**
     * Asztalok mentése XML fájlba
     * @throws Exception Ha hiba történik a fájl írásakor
     * Az asztalok listából kiírja az asztalokat a fájlba
     */
    public void saveAsztalToXML() throws Exception {
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

    /**
     * Asztalok betöltése XML fájlból
     * @param menuItems Menüelemek listája
     * @param terem A terem adatai
     * @return Az asztalok listája
     * @throws Exception Ha hiba történik a fájl olvasásakor
     * Betölti az asztalokat a fájlból, ha nem sikerül, akkor hibát dobunk hibba lehet az asztal pozíciója nem lehet nagyobb, mint a terem mérete
     * vagy a rendeléshez tartozó menüelem nem található
     * vagy az asztal neve vagy pozíciója már létezik
     */
    public void loadAsztalFromXML(List<menu> menuItems,terem terem) throws Exception {
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

                if(x+1 >= terem.getX() || y+1 >= terem.getY()) {
                    throw new Exception("Az asztal pozíciója nem lehet nagyobb, mint a terem mérete!");
                }

                // Érték betöltése
                Long ertek = Long.parseLong(asztalElem.getElementsByTagName("ertek").item(0).getTextContent());

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
                    else {
                        throw new Exception("A rendeléshez tartozó menüelem nem található!");
                    }

                }


                // Ellenőrzés, hogy van-e már ilyen nevű asztal vagy azonos pozíciójú asztal
                boolean exists = asztalok.stream().anyMatch(a -> a.getNev().equals(nev) || (a.getX() == x && a.getY() == y));
                if (exists) {
                    throw new Exception("Az asztal neve vagy pozíciója már létezik!");
                }

                // Asztal létrehozása és hozzáadása a listához
                asztal asztalItem = new asztal(nev, x, y, rendelesek, ertek);
                asztalok.add(asztalItem);
            }
        }

    }

}
