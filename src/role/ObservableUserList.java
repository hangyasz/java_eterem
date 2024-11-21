package role;

import raktar.raktar;
import xml.XMLUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObservableUserList extends ArrayList<User> {
    private XMLUser xmlUser;

    public ObservableUserList(XMLUser xmlUser) {
        this.xmlUser = xmlUser;
    }

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


    private void attachObserver(User item) {
        item.setOnChangeCallback(this::updateXML);
    }

    private void detachObserver(User item) {
        item.setOnChangeCallback(null);
    }

    private void updateXML() {
        if (xmlUser != null) {
            xmlUser.userUpdate(this);
        }
    }
}