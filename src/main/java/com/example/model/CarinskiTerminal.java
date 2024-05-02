package src.main.java.com.example.model;

import src.main.java.com.example.dokumentacija.CarinskaDokumentacija;
import src.main.java.com.example.dokumentacija.RadSaCarinskomDokumentacijom;
import src.main.java.com.example.dokumentacija.RadSaSuspenzijama;
import src.main.java.com.example.dokumentacija.Suspenzija;
import src.main.java.com.example.mapa.Mapa;
import src.main.java.com.example.mapa.Simulacija;
import src.main.java.com.example.upravljanje.Main;

import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;

public class CarinskiTerminal extends Terminal{
    public static Random random = new Random();

    public CarinskiTerminal(Mapa mapa, int red, int kolona){
        this.id = BROJAC++;
        this.nazivTerminala = "Царински_терминал " + id;
        this.simulacioniDio = mapa;
        this.red = red;
        this.kolona = kolona;
    }

    @Override
    public void procesirajVozilo(Vozilo vozilo){
        synchronized (Vozilo.getLOCK()){
            if(!Simulacija.isPAUZA()){

                if(vozilo instanceof LicnoVozilo){
                    System.out.println();
                    System.out.println(vozilo);
                    System.out.println();
                    try{
                        sleep(2000);
                    }catch (InterruptedException ex){
                        Main.LOGGER.log(Level.SEVERE, "Greska u metodi procesirajVozilo u klasi CarinskiTerminal - LicnoVozilo", ex);
                    }
                    Vozilo.getLOCK().notifyAll();
                }else if(vozilo instanceof Autobus){
                    Iterator<Putnik> putnici = vozilo.getListaPutnika().iterator();
                    System.out.println();
                    System.out.println(vozilo);
                    System.out.println();

                    try {
                        sleep(100);
                    }catch (InterruptedException ex){
                        Main.LOGGER.log(Level.SEVERE, "Greska u metodi procesirajVozilo u klasi CarinskiTerminal - procesiranje putnika u Autobusu", ex);
                    }

                    while (putnici.hasNext()){

                        Putnik p = putnici.next();
                        System.out.println(p);

                        if(p.isImaNedozvoljeneStvari()){
                            System.out.println();
                            System.out.println(p + " POSJEDUJE NEDOZVOLJENE STVARI!");
                            System.out.println();

                            vozilo.setPutniciKojiSuImaliIncident(p);

                            CarinskaDokumentacija carinskaDokumentacija = new CarinskaDokumentacija(this, vozilo, p);
                            RadSaCarinskomDokumentacijom.kreirajCarinskuDokumentaciju(carinskaDokumentacija);

                           // vozilo.listaPutnika.remove(p);
                            putnici.remove();
                        }
                    }
                    Vozilo.getLOCK().notifyAll();
                }else{
                    System.out.println(vozilo);
                    try {
                        sleep(500);
                    }catch (InterruptedException ex){
                        Main.LOGGER.log(Level.SEVERE, "Greska u metodi procesirajVozilo u klasi CarinskiTerminal - Kamion", ex);
                    }

                    Kamion kamion = (Kamion)vozilo;
                    if(random.nextInt(101) > 50){
                        System.out.println();
                        System.out.println("POTREBNO GENERISATI CARINSKU DOKUMENTACIJU ZA VOZILO: " + kamion.getNazivVozila());
                        System.out.println();
                        if(!kamion.dokumentacija) {
                            CarinskaDokumentacija carinskaDokumentacija = new CarinskaDokumentacija(this, vozilo, vozilo.getVozac());
                            RadSaCarinskomDokumentacijom.kreirajCarinskuDokumentaciju(carinskaDokumentacija);
                        }
                        if(kamion.getStvarnaTezina() > kamion.getDeklarisanaTezina()){
                            Suspenzija suspenzija = new Suspenzija(vozilo, vozilo.getVozac(), this, RazloziSuspenzije.ПРЕКОРАЧЕЊЕ_ДОЗВОЉЕНЕ_ТЕЖИНЕ.toString());
                            RadSaSuspenzijama.suspendujVozilo(suspenzija);

                            simulacioniDio.ukloniVoziloSaMape(kamion.getTrenutniRed(), kamion.getTrenutnaKolona());
                        }
                    }
                    Vozilo.getLOCK().notifyAll();
                }

                vozilo.setVoziloProcesiranoOdStraneCarine(true);
            } else {
                try{
                    Vozilo.getLOCK().wait();
                }catch (InterruptedException ex){
                    Main.LOGGER.log(Level.SEVERE, "Greska prilikom pauziranja u carinskom terminalu", ex);
                }
            }

        }
    }
}
