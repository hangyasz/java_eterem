package oszetevok;
import raktar.raktar;


/**
 * Az összetevők osztálya
 * Tartalmazza az összetevő nevét, mennyiségét, mértékegységét és a raktárat
 */
public class oszetevok {
    private raktar raktar;
    private double mennyiseg;
    private Runnable onChangeCallback; // Megfigyelő

    /**
     * Konstruktor
     * @param raktar a raktár
     * @param mennyiseg a mennyiség
     */
    public oszetevok(raktar raktar, double mennyiseg){
        this.raktar = raktar;
        this.mennyiseg = mennyiseg;
    }

    /**
     * Visszaadja a raktárat
     * @return a raktár
     */
    public raktar getRaktar(){
        return raktar;
    }
    /**
     * Visszaadja a mennyiséget
     * @return a mennyiség
     */
    public double getMennyiseg(){
        return mennyiseg;
    }

    /**
     * Beállítja a mennyiséget és értesíti a megfigyelőt a változásról
     * @param mennyiseg a mennyiség
     * a mennyiségnek nagyobbnak kell lennie, mint 0
     */
    public void setMennyiseg(double mennyiseg){
        if(mennyiseg < 0){
            throw new IllegalArgumentException("A mennyiségnek nagyobbnak kell lennie, mint 0!");
        }
        this.mennyiseg = mennyiseg;
        notifyChange();
    }

    /**
     * Visszaadja az összetevő nevét
     * @return az összetevő neve
     */
    public String getNev(){
        return raktar.getNev();
    }

    /**
     * Visszaadja az összetevő mértékegységét
     * @return az összetevő mértékegysége
     */
    public String getMertekegyseg(){
        return raktar.getMertekegyseg();
    }
    /**
     * csokeni a mennyiséget a raktárban
     */
    public void order(){
        raktar.addMennyiseg(-mennyiseg);

    }

    /**
     * növeli a mennyiséget a raktárban
     */
    public void delete(){
        raktar.addMennyiseg(mennyiseg);
    }

    /**
     * Beállítja a megfigyelőt
     * @param callback a megfigyelő
     */
    public void setOnChangeCallback(Runnable callback){
        this.onChangeCallback = callback;
    }

    /**
     * Értesíti a megfigyelőt a változásról
     */
    private void notifyChange(){
        if(onChangeCallback != null){
            onChangeCallback.run();
        }
    }


}
