package role;

import xml.XMLUser;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Az ObservableUserList osztály egy felhasználókat tároló lista, amely értesíti a hozzá tartozó XMLUser objektumot a változásokról.
 */
public class ObservableUserList extends ArrayList<User> {
    private XMLUser xmlUser;

    /**
     * Konstruktor
     * @param xmlUser Az XMLUser objektum, amelyet értesíteni kell a változásokról
     */
    public ObservableUserList(XMLUser xmlUser) {
        this.xmlUser = xmlUser;
    }

    /**
     * ArrayList metodok feluldefinialasa azert, hogy ertesitse az XMLUser-t a valtozasrol es a megfigyeloket is kezeli
     */

    @Override
    public boolean add(User user) {
        boolean result = super.add(user);
        if (result) {
            attachObserver(user); // Megfigyelő csatolása
            updateXML();
        }
        return result;
    }


    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);
        if (result && o instanceof User) {
            detachObserver((User) o); // Megfigyelő leválasztása
            updateXML();
        }
        return result;
    }

    @Override
    public void clear() {
        this.forEach(this::detachObserver); // Minden elemről leválasztjuk a megfigyelőt
        super.clear();
        updateXML();
    }


    @Override
    public boolean addAll(Collection<? extends User> c) {
        boolean result = super.addAll(c);
        if (result) {
            c.forEach(this::attachObserver); // Minden elemhez megfigyelő csatolása
            updateXML();
        }
        return result;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);
        if (result) {
            c.stream().filter(o -> o instanceof User).forEach(o -> detachObserver((User) o));
            updateXML();
        }
        return result;
    }

    /**
     * Megfigyelő hozzáadása és az XMLUser értesítése
     */

    private void attachObserver(User item) {
        item.setOnChangeCallback(this::updateXML);
    }

    /**
     * Megfigyelő eltávolítása
     */
    private void detachObserver(User item) {
        item.setOnChangeCallback(null);
    }

    /**
     * Az XMLUser értesítése
     */
    private void updateXML() {
        if (xmlUser != null) {
            xmlUser.userUpdate(this);
        }
    }
}