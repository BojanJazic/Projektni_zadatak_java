package src.main.java.com.example.model;


import src.main.java.com.example.mapa.Mapa;
import src.main.java.com.example.mapa.Simulacija;
import src.main.java.com.example.upravljanje.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;

public abstract class Terminal extends Thread{
    protected static int BROJAC;
    protected int id;
    protected String nazivTerminala;
    protected int red;
    protected int kolona;


    protected Mapa simulacioniDio;

    public String getNazivTerminala(){
        return nazivTerminala;
    }

    @Override
    public void run(){
        while (true){

            if(Simulacija.isKRAJ()){
                break;
            }
            if(Simulacija.isPAUZA()){
                synchronized (Vozilo.getLOCK()){
                    try {
                        Vozilo.getLOCK().wait();
                    }catch (InterruptedException ex){
                        Main.LOGGER.log(Level.SEVERE, "Greska u run metodi klase Terminal prilikom provjere da li je PAUZA == true", ex);
                    }
                }
            }

            if(daLiMozeDaRadi(id)) {
                Vozilo trenutnoVozilo = null;
                synchronized (Vozilo.LOCK) {
                    if (simulacioniDio.daLiJePozicijaZauzeta(red, kolona - 1)) {
                        trenutnoVozilo = simulacioniDio.dohvatiVozilo(red, kolona - 1);
                    }

                    if (trenutnoVozilo != null) {
                        procesirajVozilo(trenutnoVozilo);
                    }
                }
                synchronized (Vozilo.LOCK) {
                    Vozilo.getLOCK().notifyAll();
                }
            }
        }
    }

    public void procesirajVozilo(Vozilo vozilo){

    }


    private boolean daLiMozeDaRadi(int id){
        boolean result = false;
        try{
            Path putanja = Paths.get("src", "main", "resources", "com", "example", "projekat", "konfiguracija", "stanjeTerminala.txt");
            List<String> stanje = Files.readAllLines(putanja);
            String trazeniTerminal = null;
            if(id >=0 && id < stanje.size()){
                trazeniTerminal = stanje.get(id);
                return Boolean.parseBoolean(trazeniTerminal.substring(0, trazeniTerminal.length() - 1).trim());
            }

        }catch (IOException ex){
            Main.LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return result;
    }
}
