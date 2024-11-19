package asztal;

import oszetevok.oszetevok;
import menu.menu;
import java.util.List;
import java.util.ArrayList;

public class asztal {
    private String nev;
    private int eretke = 0;
    private int x, y;
    private List<menu> rendelesek = new ArrayList<>();

    public asztal(String nev, int x, int y) {
        this.nev = nev;
        this.x = x;
        this.y = y;
    }

    public int getEretke() {
        return eretke;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getNev() {
        return nev;
    }

    public List<menu> getRendelesek() {
        return rendelesek;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void addRendeles(menu item) {
        rendelesek.add(item);
        eretke += item.getAr();
        for(oszetevok oszetevo : item.getOszetevok()) {
            oszetevo.order();
        }
    }

    public void removeRendeles(menu item) {
        rendelesek.remove(item);
        eretke -= item.getAr();
        for(oszetevok oszetevo : item.getOszetevok()) {
            oszetevo.delete();
        }
    }

    public void setEretke(int eretke) {
        this.eretke = eretke;
    }
}