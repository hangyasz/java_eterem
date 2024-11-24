package xml;

import terem.terem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;

import static xml.XMLManager.TEREM_FILE;

/**
 * Az osztály a terem XML fájlának kezelését végzi
 */
public class XMLTerem {

    /**
     * Terem betöltése fájból hibbes esetén bekéri a felhasználótól a terem méretét
     * @return A terem elemek listája
     */
    public terem loadTerem() {
        try {
            return loadTeremFromXML();
        } catch (Exception e) {
            return getTeremFromUser();
        }
    }

    /**
     * Betölti a terem adatait az XML fájlból
     * @return A terem adatai
     * @throws Exception Hiba esetén kivétel dobása a terem méretei nem lehetnek 0 vagy annál kisebb
     */
    private terem loadTeremFromXML() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(TEREM_FILE);

        Element element = (Element) doc.getElementsByTagName("Terem").item(0);

        int x = Integer.parseInt(element.getElementsByTagName("X").item(0).getTextContent());
        int y = Integer.parseInt(element.getElementsByTagName("Y").item(0).getTextContent());
        if(x <= 0 || y <= 0)
            throw new Exception("A terem mérete nem lehet 0 vagy annál kisebb!");

        return new terem(x, y);
    }


    /**
     * Bekéri a felhasználótól a terem méretét
     * @return A terem adatai
     * Ha a felhasználó nem ad meg érvényes méretet, akkor újra bekéri a méretet
     * Ha a felhasználó bezárja az ablakot, akkor kilép a program
     */
    private terem getTeremFromUser() {
       JTextField xField = new JTextField(10);
       JTextField yField = new JTextField(10);

         JPanel panel = new JPanel(new GridLayout(2, 2));
         panel.add(new JLabel("X:"));
         panel.add(xField);
            panel.add(new JLabel("Y:"));
            panel.add(yField);
         if (JOptionPane.showConfirmDialog(null, panel, "Terem méretének megadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
             try {
                 int x = Integer.parseInt(xField.getText());
                 int y = Integer.parseInt(yField.getText());
                 if (x <= 0 || y <= 0) {
                     JOptionPane.showMessageDialog(null, "X és Y értékének nagyobbnak kell lennie mint 0", "Hiba", JOptionPane.ERROR_MESSAGE);
                     return getTeremFromUser();
                 }
                 terem newTerem = new terem(x, y);
                 saveTeremToXML(newTerem);
                 return newTerem;
             } catch (NumberFormatException e) {
                 JOptionPane.showMessageDialog(null, "Kérem számokat adjon meg!", "Hiba", JOptionPane.ERROR_MESSAGE);
                 return getTeremFromUser();
             } catch (Exception e) {
                 JOptionPane.showMessageDialog(null, "Hiba történt: " + e.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
                 return getTeremFromUser();
             }
         }
         else {
             System.exit(0);
             return null;
         }
    }


    /**
     * Terem mentése XML fájlba
     * @param terem A terem adatai
     * @throws Exception Hiba esetén kivétel dobása
     */
    private void saveTeremToXML(terem terem) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("Terem");
        doc.appendChild(root);

        Element xElement = doc.createElement("X");
        xElement.setTextContent(String.valueOf(terem.getX()));
        root.appendChild(xElement);

        Element yElement = doc.createElement("Y");
        yElement.setTextContent(String.valueOf(terem.getY()));
        root.appendChild(yElement);

        XMLManager.writeXmlFile(doc, TEREM_FILE);
    }
}