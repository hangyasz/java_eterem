
package raktar;


/**
 * A raktár osztály egy raktár adatait ami a nevét, mértékegységét és mennyiségét tárolja.
 */
public class raktar {
    private String nev;
    private String mertekegyseg;
    private double mennyiseg;
    private Runnable onChangeCallback; // Megfigyelő


    /**
     * Konstruktor
     * @param nev A raktár neve
     * @param mertekegyseg A raktárban tárolt áru mértekegysége
     * @param mennyiseg A raktárban tárolt áru mennyisége
     */
    public raktar(String nev, String mertekegyseg, double mennyiseg) {
        this.nev = nev;
        this.mertekegyseg = mertekegyseg;
        this.mennyiseg = mennyiseg;
    }

    /**
     * Az áru nevének lekérdezése
     * @return Az áru neve
     */
    public String getNev() {
        return nev;
    }

    /**
     * Az áru mértekegységének lekérdezése
     * @return Az áru mértekegysége
     */
    public String getMertekegyseg() {
        return mertekegyseg;
    }

    /**
     * Az áru mennyiségének lekérdezése
     * @return Az áru mennyisége
     */
    public double getMennyiseg() {
        return mennyiseg;
    }

    /**
     * Az áru mennyiségének beállítása  ha a mennyiség negatív, akkor 0 lesz
     * @param mennyiseg Az áru mennyisége
     * és értesíti a megfigyelőket
     */
    public void addMennyiseg(double mennyiseg) {
        this.mennyiseg += mennyiseg;
        if (this.mennyiseg < 0) {
            this.mennyiseg = 0;
        }
        notifyChange();
    }

    /**
     * Visszaadja a raktár adatait szöveges formában csak a nevet
     */
    @Override
    public String toString() {
        return nev;
    }

    /**
     * Megfigyelő hozzáadása
     * @param callback A megfigyelő
     */
    public void setOnChangeCallback(Runnable callback) {
        this.onChangeCallback = callback;
    }

    /**
     * Értesíti a megfigyelőket
     */
    private void notifyChange() {
        if (onChangeCallback != null) {
            onChangeCallback.run();
        }
    }
}