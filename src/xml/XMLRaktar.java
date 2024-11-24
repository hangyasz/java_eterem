package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import raktar.*;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static xml.XMLManager.RAKTAR_FILE;

/**
 * Az osztály a raktár XML fáljának kezelését végzi
 */
public class XMLRaktar  {

    /**
     * A raktár elemek listája
     */
    List<raktar> raktarItems = new ObservableRaktarList(this);

    /**
     * Raktár frissítése fájlba kiírjuk a raktár adatokat a fájlba a raktár listából
     */
    public void raktarUpdate(){
        try {
            saveRaktarToXML(raktarItems);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a raktár írásakor", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Raktár betöltése fájból hibbes esetén hibaüzenet megjelenít a raktár elemek listáját
     * @return A raktár elemek listája
     */
    public List<raktar> raktarLoad(){
        try {
            return loadRaktarFromXML();
        } catch (Exception e) {
            List<String> raktarName = raktarItems.stream().map(raktar::getNev).collect(Collectors.toList());
            JTextArea textArea = new JTextArea(String.join("\n", raktarName));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(300, 200));
            JOptionPane.showMessageDialog(null,  scrollPane, "Hiba a raktár olvasásakor", JOptionPane.ERROR_MESSAGE);
            return raktarItems;
        }
    }

    /**
     * Raktár elemek fájlba írása
     * @param raktarItems A raktár elemek listája
     * @throws Exception Hiba esetén kivétel dobása
     */
    public void saveRaktarToXML(List<raktar> raktarItems) throws Exception {
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

        XMLManager.writeXmlFile(doc, RAKTAR_FILE);
    }

    /**
     * Raktár elemek fájlból beolvasása
     * @return A raktár elemek listája
     * @throws Exception Hiba esetén kivétel dobása a fájl beolvasásakor vagy ha már szerepel a raktárban az elem
     */
    public List<raktar> loadRaktarFromXML() throws Exception {
        Document doc =  XMLManager.loadXmlFile(RAKTAR_FILE);
        NodeList elemList = doc.getElementsByTagName("elem");

        for (int i = 0; i < elemList.getLength(); i++) {
            Node node = elemList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String nev = elem.getAttribute("nev");

                String mertekegyseg = elem.getElementsByTagName("mertekegyseg").item(0).getTextContent();
                double mennyiseg = Double.parseDouble(elem.getElementsByTagName("mennyiseg").item(0).getTextContent());
                if (mennyiseg < 0) {
                    mennyiseg = 0;
                }
                boolean include = raktarItems.stream().anyMatch(raktar -> raktar.getNev().equals(nev));
                if (include) {
                    throw new Exception(nev + " már szerepel a raktárban");
                }

                raktarItems.add(new raktar(nev, mertekegyseg, mennyiseg));
            }
        }
        return raktarItems;
    }



}
