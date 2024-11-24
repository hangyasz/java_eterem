package raktar;

import xml.XMLRaktar;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Az ObservableRaktarList osztály egy raktárakat tároló lista, amely értesíti a hozzá tartozó XMLRaktar objektumot a változásokról.
 */
public class ObservableRaktarList extends ArrayList<raktar> {
    private XMLRaktar xmlRaktar;

    /**
     * Konstruktor
     * @param xmlRaktar Az XMLRaktar objektum, amelyet értesíteni kell a változásokról
     */
    public ObservableRaktarList(XMLRaktar xmlRaktar) {
        this.xmlRaktar = xmlRaktar;
    }

    /**
     * ArrayList metodok feluldefinialasa azert, hogy ertesitse az XMLMenu-t a valtozasrol és a megfigyeloket is kezeli
     */

    @Override
    public boolean add(raktar item) {
        boolean result = super.add(item);
        if (result) {
            attachObserver(item);
            updateXML();
        }
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends raktar> c) {
        boolean result = super.addAll(c);
        if (result) {
            c.forEach(this::attachObserver);
            updateXML();
        }
        return result;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);
        if (result && o instanceof raktar) {
            detachObserver((raktar) o);
            updateXML();
        }
        return result;
    }

    @Override
    public void clear() {
        this.forEach(this::detachObserver);
        super.clear();
        updateXML();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);
        if (result) {
            c.stream().filter(o -> o instanceof raktar).forEach(o -> detachObserver((raktar) o));
            updateXML();
        }
        return result;
    }

    /**
     * Megfigyelő hozzáadása és az XMLMenu értesítése
     */
    private void attachObserver(raktar item) {
        item.setOnChangeCallback(this::updateXML);
    }

    /**
     * Megfigyelő eltávolítása
     */
    private void detachObserver(raktar item) {
        item.setOnChangeCallback(null);
    }


    /**
     * Az XMLMenu értesítése a változásokról
     */
    private void updateXML() {
        if (xmlRaktar != null) {
            xmlRaktar.raktarUpdate();
        }
    }
}
