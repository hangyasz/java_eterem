package menu;

import oszetevok.oszetevok;
import xml.XMLMenu;

import java.util.ArrayList;
import java.util.Collection;


public class ObservableMenuList extends ArrayList<menu> {
    private XMLMenu xmlMenu;

    public ObservableMenuList(XMLMenu xmlMenu) {
        this.xmlMenu = xmlMenu;
    }

    @Override
    public boolean add(menu item) {
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
            detachObserver((menu) o);
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
    public boolean addAll(Collection<? extends menu> c) {
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
            c.stream().filter(o -> o instanceof menu).forEach(o -> detachObserver((menu) o));
        }
        return result;
    }

    private void attachObserver(menu item) {
        item.setOnChangeCallback(this::updateXML);
    }

    private void detachObserver(menu item) {
        item.setOnChangeCallback(null);
    }

    private void updateXML() {
        if (xmlMenu != null) {
            xmlMenu.menuUpdate();
        }
    }
}
