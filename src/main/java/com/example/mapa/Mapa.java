package src.main.java.com.example.mapa;

import src.main.java.com.example.model.Vozilo;

public class Mapa {
    public Vozilo[][] mapaVozila;
    int brojRedova;
    int brojKolona;

    public Mapa(int brojRedova, int brojKolona){
        mapaVozila = new Vozilo[brojRedova][brojKolona];
        this.brojRedova = brojRedova;
        this.brojKolona = brojKolona;
    }

    public boolean daLiJePozicijaZauzeta(int red, int kolona){
        return mapaVozila[red][kolona] != null;
    }

    public boolean daLiJePozicijaValidna(int red, int kolona){
        if((red >= 0 && red <= brojRedova) && (kolona >= 0 && kolona <= brojKolona))
            return true;
        return false;
    }

    public void postaviVoziloNaPolje(Vozilo vozilo, int red, int kolona){
        if(daLiJePozicijaValidna(red, kolona))
            mapaVozila[red][kolona] = vozilo;
    }

    public void ukloniVoziloSaMape(int red, int kolona){
        if(daLiJePozicijaValidna(red, kolona))
            mapaVozila[red][kolona] = null;
    }

    public Vozilo dohvatiVozilo(int red, int kolona){
        if(daLiJePozicijaValidna(red, kolona))
            return mapaVozila[red][kolona];
        return null;
    }

}
