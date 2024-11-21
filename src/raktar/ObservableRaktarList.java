package raktar;

import xml.XMLRaktar;
import java.util.ArrayList;
import java.util.Collection;

public class ObservableRaktarList extends ArrayList<raktar> {
    private XMLRaktar xmlRaktar;

    public ObservableRaktarList(XMLRaktar xmlRaktar) {
        this.xmlRaktar = xmlRaktar;
    }

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

    private void attachObserver(raktar item) {
        item.setOnChangeCallback(this::updateXML);
    }

    private void detachObserver(raktar item) {
        item.setOnChangeCallback(null);
    }

    private void updateXML() {
        if (xmlRaktar != null) {
            xmlRaktar.raktarUpdate();
        }
    }
}
