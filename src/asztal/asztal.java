// asztal.java
package asztal;

import java.util.List;
import java.util.ArrayList;

public class asztal {
    private String nev;
    private int eretke = 0;
    private int x, y;
    private List<menu.menu> rendelesek = new ArrayList<>();

    public asztal(String nev, int x, int y) {
        this.nev = nev;
        this.x = x;
        this.y = y;
    }

    public void rendel(menu.menu menu) {
        rendelesek.add(menu);
        eretke += menu.getAr();
    }

    public void torol(menu.menu menu) {
        rendelesek.remove(menu);
        eretke -= menu.getAr();
    }

    public void kiir() {
        System.out.println("Asztal: " + nev);
        System.out.println("Érték: " + eretke);
        for (menu.menu menu : rendelesek) {
            System.out.println(menu.getNev() + " " + menu.getAr());
        }
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

    public List<menu.menu> getRendelesek() {
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
}