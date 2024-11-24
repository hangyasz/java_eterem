package menu;

import java.util.List;
import java.util.ArrayList;


/**
 * A menü osztály
 * Tartalmazza a menü nevét, árát, összetevőit, hogy engedélyezve van-e és a menü típusát és a változás callbacket
 */
public class menu {
    private String nev;
    private int ar;
    private List<oszetevok.oszetevok> oszetevok = new ArrayList<>();
    private boolean enabled = false;
    private MenuType type;
    private Runnable onChangeCallback;

    /**
     * Konstruktor
     * @param nev a menü neve
     * @param ar a menü ára
     * @param oszetevok a menü összetevői
     * @param type a menü típusa
     * létrehoz egy menüt a megadott paraméterekkel
     */
    public menu(String nev, int ar, List<oszetevok.oszetevok> oszetevok, MenuType type) {
        this.nev = nev;
        this.ar = ar;
        this.oszetevok = oszetevok;
        this.type = type;
    }

    /**
     * Visszaadja a menü nevét
     * @return a menü neve
     */
    public String getNev() {
        return nev;
    }

    /**
     * Visszaadja a menü árát
     * @return
     */
    public int getAr() {
        return ar;
    }

    /**
     * Visszaadja a menü összetevőit
     * @return
     */
    public List<oszetevok.oszetevok> getOszetevok() {
        return oszetevok;
    }

    /**
     * Visszaadja, hogy engedélyezve van-e a menü
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Beállítja, hogy engedélyezve van-e a menü
     * @param enabled az alpaptjuk
     *szol hogy változott a menü
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyChange();
    }

    /**
     * Beállítja a menü nevét
     * @param nev az új név
     * szol hogy változott a menü
     */
    public void setNev(String nev) {
        this.nev = nev;
        notifyChange();
    }

    /**
     * Beállítja a menü árát
     * @param ar az új ár
     * szol hogy változott a menü
     */
    public void setAr(int ar) {
        this.ar = ar;
        notifyChange();
    }

    /**
     *visszaadja a menü típusát
     * @return
     */
    public MenuType getType() {
        return type;
    }

    /**
     * Beállítja a menü típusát
     * @param type az új típus
     *
     */
    public void setType(MenuType type) {
        this.type = type;
        notifyChange();
    }

    /**
     * Beállítja a változás callbacket
     * @param callback a callback
     */
    public void setOnChangeCallback(Runnable callback){
        this.onChangeCallback = callback;
    }

    /**
     * Szól a változás callbacknek
     */
    private void notifyChange(){
        if(onChangeCallback != null){
            onChangeCallback.run();
        }
    }
}