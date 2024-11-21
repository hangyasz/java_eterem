package asztal;

import oszetevok.oszetevok;
import xml.XMLAsztal;

import java.util.ArrayList;
import java.util.Collection;

public class ObservableAsztalList extends ArrayList<asztal> {
    private XMLAsztal xmlAsztal;

    public ObservableAsztalList(XMLAsztal xmlAsztal) {
        this.xmlAsztal = xmlAsztal;
    }

    @Override
    public boolean add(asztal item) {
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
            detachObserver((asztal) o);
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
    public boolean addAll(Collection<? extends asztal> c) {
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
            c.stream().filter(o -> o instanceof asztal).forEach(o -> detachObserver((asztal) o));
        }
        return result;
    }


    private void attachObserver(asztal item) {
        item.setOnChangeCallback(this::updateXML);
    }

    private void detachObserver(asztal item) {
        item.setOnChangeCallback(null);
    }



    private void updateXML() {
        if (xmlAsztal != null) {
            xmlAsztal.asztalUpdate();
        }
    }
}