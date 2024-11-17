package oszetevok;
import raktar.raktar;

public class oszetevok {
    private raktar raktar;
    private double mennyiseg;

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
        this.mennyiseg = mennyiseg;
    }
    public void addMennyiseg(double mennyiseg){
        this.mennyiseg += mennyiseg;
        if(this.mennyiseg < 0){
            this.mennyiseg = 0;
        }
    }
    public String getNev(){
        return raktar.getNev();
    }
    public String getMertekegyseg(){
        return raktar.getMertekegyseg();
    }

}
