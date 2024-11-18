package menu;

import java.util.List;
import java.util.ArrayList;

public class menu {
    private String nev;
    private int ar;
    private List<oszetevok.oszetevok> oszetevok= new ArrayList<oszetevok.oszetevok>();
    private boolean enabled = false;

    public menu(String nev, int ar, List<oszetevok.oszetevok> oszetevok){
        this.nev = nev;
        this.ar = ar;
        this.oszetevok = oszetevok;
    }
    public String getNev(){
        return nev;
    }
    public int getAr(){
        return ar;
    }
    public List<oszetevok.oszetevok> getOszetevok(){
        return oszetevok;
    }
    public boolean isEnabled(){
        return enabled;
    }
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    public void setNev(String nev){
        this.nev = nev;
    }
    public void setAr(int ar){
        this.ar = ar;
    }


}
