package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import raktar.raktar;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

import static xml.XMLManager.RAKTAR_FILE;

public class XMLRaktar  {

    List<raktar> raktarItems = new ArrayList<>();

    public void raktarUpdate(){
        try {
            saveRaktarToXML(raktarItems);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a raktár írásakor", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<raktar> raktarLoad(){
        try {
            return loadRaktarFromXML();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage(), "Hiba a raktár olvasásakor", JOptionPane.ERROR_MESSAGE);
            return raktarItems;
        }
    }

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

                raktarItems.add(new raktar(nev, mertekegyseg, mennyiseg));
            }
        }
        return raktarItems;
    }



}
