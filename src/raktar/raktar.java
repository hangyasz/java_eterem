
package raktar;


public class raktar {
    private String nev;
    private String mertekegyseg;
    private double mennyiseg;

    public raktar(String nev, String mertekegyseg, double mennyiseg) {
        this.nev = nev;
        this.mertekegyseg = mertekegyseg;
        this.mennyiseg = mennyiseg;
    }

    public String getNev() {
        return nev;
    }

    public String getMertekegyseg() {
        return mertekegyseg;
    }

    public double getMennyiseg() {
        return mennyiseg;
    }

    public void setMennyiseg(double mennyiseg) {
        this.mennyiseg = mennyiseg;
    }

    public void addMennyiseg(double mennyiseg) {
        this.mennyiseg += mennyiseg;
        if (this.mennyiseg < 0) {
            this.mennyiseg = 0;
        }
    }
    @Override
    public String toString() {
        return nev;
    }
}