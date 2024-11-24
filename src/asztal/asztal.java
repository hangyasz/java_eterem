package asztal;

import oszetevok.oszetevok;
import menu.menu;
import java.util.List;
import java.util.ArrayList;

/**
 * Az asztalokat reprezentáló osztály, amely az asztalok nevét, rendelés értékeit, pozícióját és rendeléseit tárolja.
 */

public class asztal {
    private String nev;
    private long eretke = 0;
    private int x, y;
    private List<menu> rendelesek = new ArrayList<>();
    private Runnable onChangeCallback;

    /**
     * Konstruktor az asztal osztályhoz.
     * @param nev az asztal neve
     * @param x az asztal x koordinátája
     * @param y az asztal y koordinátája
     */
    public asztal(String nev, int x, int y) {
        this.nev = nev;
        this.x = x;
        this.y = y;
    }

    /**
     * Konstruktor az asztal osztályhoz.
     * @param nev az asztal neve
     * @param x az asztal x koordinátája
     * @param y az asztal y koordinátája
     * @param rendelesek az asztal rendelései
     * @param eretke az asztal értéke
     */
    public asztal(String nev, int x, int y, List<menu> rendelesek, Long eretke) {
        this.nev = nev;
        this.x = x;
        this.y = y;
        this.rendelesek = rendelesek;
        this.eretke = eretke;
    }

    /**
     * Visszaadja az asztal értékét.
     * @return az asztal értéke
     */
    public long getEretke() {
        return eretke;
    }

    /**
     * Visszaadja az asztal x koordinátáját.
     * @return az asztal x koordinátája
     */
    public int getX() {
        return x;
    }

    /**
     * Visszaadja az asztal y koordinátáját.
     * @return az asztal y koordinátája
     */
    public int getY() {
        return y;
    }

    /**
     * Visszaadja az asztal nevét.
     * @return az asztal neve
     */
    public String getNev() {
        return nev;
    }

    /**
     * Visszaadja az asztal rendeléseit.
     * @return az asztal rendelései
     */
    public List<menu> getRendelesek() {
        return rendelesek;
    }

    /**
     * Beállítja az asztal x koordinátáját.
     * @param x az asztal x koordinátája
     */
    public void setX(int x) {
        this.x = x;
        notifyChange();

    }

    /**
     * Beállítja az asztal y koordinátáját.
     * @param y az asztal y koordinátája
     */
    public void setY(int y) {
        this.y = y;
        notifyChange();
    }

    /**
     * Beállítja az asztal nevét.
     * @param nev az asztal neve
     */
    public void setNev(String nev) {
        this.nev = nev;
        notifyChange();
    }

    /**
     * Hozzáad egy rendelést az asztalhoz.
     * @param item a rendelés
     */
    public void addRendeles(menu item) {
        rendelesek.add(item);
        eretke += item.getAr();
        for(oszetevok oszetevo : item.getOszetevok()) {
            oszetevo.order();
        }
        notifyChange();
    }

    /**
     * Eltávolít egy rendelést az asztalról.
     * @param item a rendelés
     */
    public void removeRendeles(menu item) {
        rendelesek.remove(item);
        eretke -= item.getAr();
        for(oszetevok oszetevo : item.getOszetevok()) {
            oszetevo.delete();
        }
        notifyChange();
    }

    /**
     * Beállítja az asztal értékét.
     * @param eretke az asztal értéke
     */
    public void setEretke(long eretke) {
        this.eretke = eretke;
        notifyChange();
    }


    /**
     * Beállítja az onChangeCallback értékét.
     * @param callback a callback függvény
     */
    public void setOnChangeCallback(Runnable callback){
        this.onChangeCallback = callback;
    }

    /**
     * Értesíti a megfigyelőt az asztal változásáról.
     */
    private void notifyChange(){
        if(onChangeCallback != null){
            onChangeCallback.run();
        }
    }


}