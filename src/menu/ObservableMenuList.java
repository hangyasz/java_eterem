package menu;

import xml.XMLMenu;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Az ObservableMenuList osztály
 * Az ObservableMenuList osztály felelős azért, hogy a menük listáját kezelje és értesítse a megfigyelőket a változásokról
 */
public class ObservableMenuList extends ArrayList<menu> {
    private XMLMenu xmlMenu;

    /**
     * Konstruktor
     * @param xmlMenu az XMLMenu objektum
     */
    public ObservableMenuList(XMLMenu xmlMenu) {
        this.xmlMenu = xmlMenu;
    }

    /**
     * hozzáad egy elemet a listához
     * @param item a hozzáadandó elem
     * @return igaz, ha sikerült hozzáadni
     * hozzáad egy elemet a listához és értesíti a megfigyelőket
     */
    @Override
    public boolean add(menu item) {
        boolean result = super.add(item);
        if (result) {
            updateXML();
            attachObserver(item);
        }
        return result;
    }

    /**
     * eltávolít egy elemet a listából
     * @param o az eltávolítandó elem
     * @return igaz, ha sikerült eltávolítani
     * eltávolít egy elemet a listából és értesíti a megfigyelőket
     */
    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);
        if (result) {
            updateXML();
            detachObserver((menu) o);
        }
        return result;
    }

    /**
     * törli a listát és értesíti a megfigyelőket
     */
    @Override
    public void clear() {
        this.forEach(this::detachObserver);
        super.clear();
        updateXML();
    }

    /**
     * hozzáad egy kollekciót a listához
     * @param c a hozzáadandó kollekció
     * @return igaz, ha sikerült hozzáadni
     * hozzáad egy kollekciót a listához és értesíti a megfigyelőket
     */
    @Override
    public boolean addAll(Collection<? extends menu> c) {
        boolean result = super.addAll(c);
        if (result) {
            updateXML();
            c.forEach(this::attachObserver);
        }
        return result;
    }

    /**
     * eltávolít egy kollekciót a listából
     * @param c az eltávolítandó kollekció
     * @return igaz, ha sikerült eltávolítani
     * eltávolít egy kollekciót a listából és értesíti a megfigyelőket
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);
        if (result) {
            updateXML();
            c.stream().filter(o -> o instanceof menu).forEach(o -> detachObserver((menu) o));
        }
        return result;
    }

    /**
     * hozzáad egy megfigyelőt az elemhez és értesíti a megfigyelőket akkor, lefut at updateXML
     * @param item az elem
     */
    private void attachObserver(menu item) {
        item.setOnChangeCallback(this::updateXML);
    }

    /**
     * eltávolít egy megfigyelőt az elemről
     * @param item az elem
     */
    private void detachObserver(menu item) {
        item.setOnChangeCallback(null);
    }

    /**
     * frissíti az XML-t
     */
    private void updateXML() {
        if (xmlMenu != null) {
            xmlMenu.menuUpdate();
        }
    }
}
