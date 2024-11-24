package oszetevok;


import xml.XMLMenu;

import java.util.ArrayList;
import java.util.Collection;

/**
 *  Az összetevők listájának osztálya
 */

public class ObservableOszetevokList extends ArrayList<oszetevok> {
    private XMLMenu xmloszetevok;

    public ObservableOszetevokList(XMLMenu xmloszetevok) {
        this.xmloszetevok = xmloszetevok;
    }

    /**
     * ArrayList metodok feluldefinialasa azert, hogy ertesitse az XMLMenu-t a valtozasrol és a megfigyeloket is kezeli
     */

    @Override
    public boolean add(oszetevok item) {
        boolean result = super.add(item);
        if (result) {
            updateXML();
            attachObserver(item);

        }
        return result;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);
        if (result) {
            updateXML();
            detachObserver((oszetevok) o);
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
    public boolean addAll(Collection<? extends oszetevok> c) {
        boolean result = super.addAll(c);
        if (result) {
            updateXML();
            c.forEach(this::attachObserver);
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);
        if (result) {
            updateXML();
            c.stream().filter(o -> o instanceof oszetevok).forEach(o -> detachObserver((oszetevok) o));
        }
        return result;
    }


    /**
     * Megfigyelő hozzáadása és az XMLMenu értesítése
     */
    private void attachObserver(oszetevok item) {
        item.setOnChangeCallback(this::updateXML);
    }

    /**
     * Megfigyelő eltávolítása
     */
    private void detachObserver(oszetevok item) {
        item.setOnChangeCallback(null);
    }

    /**
     * XMLMenu értesítése
     */
    private void updateXML() {
        if (xmloszetevok != null) {
            xmloszetevok.menuUpdate();
        }
    }


}