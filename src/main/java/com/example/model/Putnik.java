package src.main.java.com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Putnik {
    public static Random random = new Random();

    //staticka promjenljiva koju cu iskoristiti da bih u 3% svih putnika postavio neispravna dokumenta
    public static List<Putnik> sviPutnici = new ArrayList<>();
    public static int REDNI_BROJ = 0;
    protected String ime;
    protected String prezime;
    protected boolean imaKofer = false;
    protected boolean imaNedozvoljeneStvari = false;
    protected boolean imaIspravnaDokumenta = true;


    public Putnik(){
        this.ime = "Име_" + REDNI_BROJ;
        this.prezime = "Презиме_" + REDNI_BROJ;
        sviPutnici.add(this);

        REDNI_BROJ++;
    }


    public static void postaviNeispravnaDokumenta(){
        int postotak = (int)(sviPutnici.size() * 0.03);
        int tmp = REDNI_BROJ;
        int i = 0;
        int[] izabraniPutnici = new int[postotak];

        while (i < postotak){
            boolean vecPostoji = false;
            int pom = random.nextInt(tmp)+1;
            for(int j = 0; j < i; j++){
                if(izabraniPutnici[j] == pom){
                    vecPostoji = true;
                    break;
                }
            }
            if(!vecPostoji){
                sviPutnici.get(pom).setImaIspravnaDokumenta(false);
                i++;
            }
        }
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public boolean isImaKofer() {
        return imaKofer;
    }

    public void setImaKofer(boolean imaKofer) {
        this.imaKofer = imaKofer;
    }

    public boolean isImaNedozvoljeneStvari() {
        return imaNedozvoljeneStvari;
    }

    public void setImaNedozvoljeneStvari(boolean imaNedozvoljeneStvari) {
        this.imaNedozvoljeneStvari = imaNedozvoljeneStvari;
    }

    public boolean isImaIspravnaDokumenta() {
        return imaIspravnaDokumenta;
    }

    public void setImaIspravnaDokumenta(boolean imaIspravnaDokumenta) {
        this.imaIspravnaDokumenta = imaIspravnaDokumenta;
    }


    @Override
    public String toString() {
        return "Путник{" +
                "име = '" + ime + '\'' +
                ", презиме = '" + prezime + '\'' +
                ", има_кофер = " + imaKofer +
                ", има_Недозвољене_ствари = " + imaNedozvoljeneStvari +
                ", има_исправна_документа = " + imaIspravnaDokumenta +
                '}';
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Putnik putnik = (Putnik) obj;
        if(this.ime != putnik.ime || this.prezime != putnik.prezime)
            return false;

        return true;
    }
}
