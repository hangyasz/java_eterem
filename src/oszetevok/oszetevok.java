package oszetevok;
import raktar.raktar;

public class oszetevok {
    private raktar raktar;
    private double mennyiseg;
    private Runnable onChangeCallback; // Megfigyelő

    public oszetevok(raktar raktar, double mennyiseg){
        this.raktar = raktar;
        this.mennyiseg = mennyiseg;
    }
    public raktar getRaktar(){
        return raktar;
    }
    public double getMennyiseg(){
        return mennyiseg;
    }
    public void setMennyiseg(double mennyiseg){
        if(mennyiseg <= 0){
            throw new IllegalArgumentException("A mennyiségnek nagyobbnak kell lennie, mint 0!");
        }
        this.mennyiseg = mennyiseg;
        notifyChange();
    }

    public String getNev(){
        return raktar.getNev();
    }
    public String getMertekegyseg(){
        return raktar.getMertekegyseg();
    }
    public void order(){
        raktar.addMennyiseg(-mennyiseg);
        notifyChange();

    }
    public void delete(){
        raktar.addMennyiseg(mennyiseg);
        notifyChange();
    }

    public void setOnChangeCallback(Runnable callback){
        this.onChangeCallback = callback;
    }

    private void notifyChange(){
        if(onChangeCallback != null){
            onChangeCallback.run();
        }
    }


}
