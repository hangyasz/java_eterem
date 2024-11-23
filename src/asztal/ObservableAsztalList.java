package asztal;

import xml.XMLAsztal;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Az astal osztályokat tartalmazó lista, amely figyeli az asztalok változásait.
 */
public class ObservableAsztalList extends ArrayList<asztal> {
    private XMLAsztal xmlAsztal;

    /**
     * Konstruktor az ObservableAsztalList osztályhoz.
     * @param xmlAsztal az XMLAsztal osztály
     */
    public ObservableAsztalList(XMLAsztal xmlAsztal) {
        this.xmlAsztal = xmlAsztal;
    }

    /**
     * Az asztal hozzáadása a listához és az XML fájl frissítése. Az asztalhoz observer csatolása.
     * @param item az asztal
     * @return a hozzáadás sikeressége
     */

    @Override
    public boolean add(asztal item) {
        boolean result = super.add(item);
        if (result) {
            updateXML();
            attachObserver(item);
        }
        return result;
    }

    /**
     * Az asztal eltávolítása a listából és az XML fájl frissítése. Az asztalhoz csatolt observer eltávolítása.
     * @param o az asztal
     * @return az eltávolítás sikeressége
     */
    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);
        if (result) {
            updateXML();
            detachObserver((asztal) o);
        }
        return result;
    }

    /**
     * Az asztalok törlése a listából és az XML fájl frissítése. Az asztalokhoz csatolt observer eltávolítása.
     */
    @Override
    public void clear() {
        this.forEach(this::detachObserver);
        super.clear();
        updateXML();
    }

    /**
     * Az asztalok hozzáadása a listához és az XML fájl frissítése. Az asztalokhoz observer csatolása.
     * @param c az asztalok
     * @return a hozzáadás sikeressége
     */
    @Override
    public boolean addAll(Collection<? extends asztal> c) {
        boolean result = super.addAll(c);
        if (result) {
            updateXML();
            c.forEach(this::attachObserver);
        }
        return result;
    }

    /**
     * Az asztalok eltávolítása a listából és az XML fájl frissítése. Az asztalokhoz csatolt observer eltávolítása.
     * @param c az asztalok
     * @return az eltávolítás sikeressége
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);
        if (result) {
            updateXML();
            c.stream().filter(o -> o instanceof asztal).forEach(o -> detachObserver((asztal) o));
        }
        return result;
    }


    /**
     * Az asztalokhoz observer csatolása.
     */
    private void attachObserver(asztal item) {
        item.setOnChangeCallback(this::updateXML);
    }

    /**
     * Az asztalokhoz csatolt observer eltávolítása.
     */
    private void detachObserver(asztal item) {
        item.setOnChangeCallback(null);
    }


    /**
     * Az XML fájl frissítése.
     */
    private void updateXML() {
        if (xmlAsztal != null) {
            xmlAsztal.asztalUpdate();
        }
    }
}