package src.main.java.com.example.model;

import src.main.java.com.example.mapa.Mapa;
import src.main.java.com.example.mapa.Simulacija;
import javafx.scene.paint.Color;
import src.main.java.com.example.upravljanje.Main;
import src.main.java.com.example.upravljanje.UpravljanjeSimulacijom;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;

public abstract class Vozilo extends Thread{
    public static List<Vozilo> listaSvihKreiranihVozila = new ArrayList<>();
    public static Random random = new Random();
    protected static final Object LOCK = new Object();
    public static int BROJ_PROCESIRANIH_VOZILA = 0;
    public static int BROJ_PREOSTALIH_VOZILA = 45;
    public static Vozilo USPJESNO_PROCESIRANO_VOZILO = null;
    public static int BROJAC = 0;

    protected int id_vozila;
    protected String nazivVozila;
    protected int brojPutnika;
    protected int trenutniRed = -1;
    protected int trenutnaKolona = -1;

    public List<Putnik> putniciKojiSuImaliIncident = new ArrayList<>();

    protected List<Putnik> listaPutnika;
    protected Color bojaVozila;
    protected Putnik vozac;
    protected Simulacija simulacija;

    //kontrolne promjenljive

    public boolean voziloProcesiranoOdStranePolicije = false;
    public boolean voziloProcesiranoOdStraneCarine = false;
    public boolean voziloSuspendovano = false;
    protected boolean voziloUSimulacionomDijelu = false;
    protected boolean voziloUDijeluCekanja = false;
    public boolean voziloPresloGranicu = false;



    public Vozilo(String nazivVozila, int brojPutnika, Color bojaVozila){
        this.id_vozila = BROJAC++;
        this.nazivVozila = nazivVozila;
        this.brojPutnika = brojPutnika;
        this.bojaVozila = bojaVozila;
        this.listaPutnika = kreirajPutnike(brojPutnika);
        this.vozac = listaPutnika.get(0);
        this.listaPutnika.remove(vozac);
        this.vozac.setImaKofer(false);
        listaSvihKreiranihVozila.add(this);

    }

    public static List<Vozilo> getListaSvihKreiranihVozila(){
        return listaSvihKreiranihVozila;
    }

    public int getId_vozila() {
        return id_vozila;
    }

    public String getNazivVozila() {
        return nazivVozila;
    }

    public int getBrojPutnika() {
        return brojPutnika;
    }

    public int getTrenutniRed() {
        return trenutniRed;
    }

    public void setTrenutniRed(int trenutniRed) {
        this.trenutniRed = trenutniRed;
    }

    public int getTrenutnaKolona() {
        return trenutnaKolona;
    }

    public void setTrenutnaKolona(int trenutnaKolona) {
        this.trenutnaKolona = trenutnaKolona;
    }

    public static Object getLOCK() {
        return LOCK;
    }


    public List<Putnik> getListaPutnika() {
        return listaPutnika;
    }

    public Color getBojaVozila() {
        return bojaVozila;
    }

    public Putnik getVozac() {
        return vozac;
    }

    public void setVoziloProcesiranoOdStranePolicije(boolean voziloProcesiranoOdStranePolicije) {
        this.voziloProcesiranoOdStranePolicije = voziloProcesiranoOdStranePolicije;
    }

    public void setVoziloProcesiranoOdStraneCarine(boolean voziloProcesiranoOdStraneCarine) {
        this.voziloProcesiranoOdStraneCarine = voziloProcesiranoOdStraneCarine;
    }

    public void setVoziloUSimulacionomDijelu(boolean voziloUSimulacionomDijelu) {
        this.voziloUSimulacionomDijelu = voziloUSimulacionomDijelu;
    }

    public void setVoziloUDijeluCekanja(boolean voziloUDijeluCekanja) {
        this.voziloUDijeluCekanja = voziloUDijeluCekanja;
    }

    public Simulacija getSimulacija() {
        return simulacija;
    }

    public void setSimulacija(Simulacija simulacija) {
        this.simulacija = simulacija;
    }

    public List<Putnik> getPutniciKojiSuImaliIncident() {
        return putniciKojiSuImaliIncident;
    }

    public void setPutniciKojiSuImaliIncident(Putnik putnik) {
        this.putniciKojiSuImaliIncident.add(putnik);
    }

    @Override
    public String toString() {
        return "Возило{" +
                "назив_возила = '" + nazivVozila + '\'' +
                ", број_путника = " + brojPutnika +
                ", возач = " + vozac.getIme() + " " + vozac.getPrezime() +
                '}';
    }


    private List<Putnik> kreirajPutnike(int brojPutnika){
        List<Putnik> tmp = new ArrayList<>();
        for(int i = 0; i < brojPutnika; i++){
            tmp.add(new Putnik());
        }

        return tmp;
    }


    @Override
    public void run(){
        while (true){
           if(Simulacija.isKRAJ()) {
               JOptionPane.showMessageDialog(null,"Симулација завршена");
               break;
           }

            if (voziloUSimulacionomDijelu) {
                synchronized (LOCK){
                    if(!voziloProcesiranoOdStranePolicije && !Simulacija.isPAUZA()){
                        if(trenutniRed == 4 && trenutnaKolona < 10 && !simulacija.simulacioniDio.daLiJePozicijaZauzeta(trenutniRed, trenutnaKolona + 2)){
                            for (int i = 0; i < 2; i++){
                                pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.simulacioniDio);
                            }
                        } else if(trenutniRed == 4 && trenutnaKolona == 10){
                            pomjeriVoziloNaPolicijskiTerminal();
                        } else if (trenutniRed == 2 && trenutnaKolona == 10 && !simulacija.simulacioniDio.daLiJePozicijaZauzeta(2, 12)) {
                            for(int i = 0; i < 2; i++)
                                pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.simulacioniDio);
                        } else if (trenutniRed == 6 && trenutnaKolona == 10 && !simulacija.simulacioniDio.daLiJePozicijaZauzeta(6, 12)) {
                            for (int i = 0; i < 2; i++)
                                pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.simulacioniDio);
                        } else {
                            LOCK.notifyAll();
                        }
                    } else if (voziloProcesiranoOdStranePolicije && !voziloProcesiranoOdStraneCarine && !Simulacija.isPAUZA()) {
                            pomjeriVoziloNaCarinskiTerminal();

                            if(trenutniRed == 2 && trenutnaKolona == 15 && !simulacija.simulacioniDio.daLiJePozicijaZauzeta(4, 15)){
                                for (int i = 0; i < 2; i++)
                                    pomjeriVozilo(SmjerKretanjaVozila.GORE, simulacija.simulacioniDio);
                            }
                    }else if(!Simulacija.isPAUZA()){
                        for (int i = 0; i < 3; i++)
                            pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.simulacioniDio);

                        voziloPresloGranicu = true;

                        UpravljanjeSimulacijom.UKUPAN_BROJ_VOZILA--;

                        USPJESNO_PROCESIRANO_VOZILO = this;
                        BROJ_PROCESIRANIH_VOZILA++;
                        simulacija.simulacioniDio.ukloniVoziloSaMape(trenutniRed, trenutnaKolona);

                        if(UpravljanjeSimulacijom.UKUPAN_BROJ_VOZILA == 0){
                            Simulacija.setKRAJ(true);
                            break;
                        }

                        LOCK.notifyAll();
                        break;
                    }else {
                        synchronized (LOCK){
                            try {
                                this.LOCK.wait();
                            }catch (InterruptedException ex){
                                Main.LOGGER.log(Level.SEVERE, "Greska u run metodi klase Vozilo prilikom provjere da li je simulacija zaustavljena", ex);
                            }
                        }
                    }
                    LOCK.notifyAll();
                }
            } else if (voziloUDijeluCekanja) {
                synchronized (LOCK){
                    if(!Simulacija.isPAUZA()){
                        if (trenutniRed == 1 && trenutnaKolona == 0 && !simulacija.vozilaUCekanju.daLiJePozicijaZauzeta(3, 0)){
                            for(int i = 0; i < 2; i++)
                                pomjeriVozilo(SmjerKretanjaVozila.GORE, simulacija.vozilaUCekanju);
                        } else if(trenutniRed == 1 && trenutnaKolona > 0 && !simulacija.vozilaUCekanju.daLiJePozicijaZauzeta(trenutniRed, trenutnaKolona - 1)){
                            pomjeriVozilo(SmjerKretanjaVozila.LIJEVO, simulacija.vozilaUCekanju);
                        } else if (trenutniRed == 3 && trenutnaKolona < 24 && !simulacija.vozilaUCekanju.daLiJePozicijaZauzeta(trenutniRed, trenutnaKolona + 1)) {
                            pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.vozilaUCekanju);
                        } else if (trenutniRed == 3 && trenutnaKolona == 24 && !simulacija.simulacioniDio.daLiJePozicijaZauzeta(4, 0)) {
                            premjestiVoziloNaGlavnuMapu(simulacija.vozilaUCekanju, simulacija.simulacioniDio, this);

                        }

                    } else {
                        try {
                            LOCK.wait();
                        }catch (InterruptedException ex){
                            Main.LOGGER.log(Level.SEVERE, "Greska prilikom uspavljivanja niti u mapi cekanja", ex);
                        }
                    }

                }
            }
        }
    }

    private void premjestiVoziloNaGlavnuMapu(Mapa mapaCekanja, Mapa glavnaMapa, Vozilo vozilo){
        mapaCekanja.ukloniVoziloSaMape(trenutniRed, trenutnaKolona);
        vozilo.setVoziloUDijeluCekanja(false);

        vozilo.setTrenutniRed(4);
        vozilo.setTrenutnaKolona(0);
        vozilo.setVoziloUSimulacionomDijelu(true);
        glavnaMapa.postaviVoziloNaPolje(vozilo, 4, 0);
        if (BROJ_PREOSTALIH_VOZILA > 0)
            BROJ_PREOSTALIH_VOZILA--;

        try {
            sleep(50);
        }catch (InterruptedException ex){
            Main.LOGGER.log(Level.SEVERE, "Greska prilikom uspavljivanja niti u metodi premjestiVoziloNaGlavnuMapu", ex);
        }
    }

    public void pomjeriVozilo(SmjerKretanjaVozila smjerKretanjaVozila, Mapa mapa){
        mapa.ukloniVoziloSaMape(trenutniRed, trenutnaKolona);

        if("GORE".equals(smjerKretanjaVozila.toString()))
            trenutniRed++;
        else if ("DOLE".equals(smjerKretanjaVozila.toString()))
            trenutniRed--;
        else if ("LIJEVO".equals(smjerKretanjaVozila.toString()))
            trenutnaKolona--;
        else
            trenutnaKolona++;


        mapa.postaviVoziloNaPolje(this, trenutniRed, trenutnaKolona);

        try {
            sleep(100);
        }catch (InterruptedException ex){
            Main.LOGGER.log(Level.SEVERE, "Greska prilikom pomjeranja vozila. Metoda pomjeriVozilo()", ex);
        }
    }

    public void pomjeriVoziloNaPolicijskiTerminal(){
        if(!simulacija.simulacioniDio.daLiJePozicijaZauzeta(4, 12)){
            for (int i = 0; i < 2; i++)
                pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.simulacioniDio);
        } else if (!simulacija.simulacioniDio.daLiJePozicijaZauzeta(2, 10)) {
            for (int i = 0; i < 2; i++)
                pomjeriVozilo(SmjerKretanjaVozila.DOLE, simulacija.simulacioniDio);
            if(!simulacija.simulacioniDio.daLiJePozicijaZauzeta(2, 12))
                for(int i = 0; i < 2; i++)
                    pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.simulacioniDio);
        }
        LOCK.notifyAll();
    }

    public void pomjeriVoziloNaCarinskiTerminal(){
        if(trenutniRed == 4 && trenutnaKolona == 12 &&!simulacija.simulacioniDio.daLiJePozicijaZauzeta(4, 15)){
            for(int i = 0; i < 3; i++){
                pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.simulacioniDio);
            }
        } else if (trenutniRed == 2 && trenutnaKolona == 12 && !simulacija.simulacioniDio.daLiJePozicijaZauzeta(2, 15)) {
            for(int i = 0; i < 3; i++)
                pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.simulacioniDio);
            if(!simulacija.simulacioniDio.daLiJePozicijaZauzeta(4, 15))
                for (int i = 0; i < 2; i++)
                    pomjeriVozilo(SmjerKretanjaVozila.GORE, simulacija.simulacioniDio);
        }

        LOCK.notifyAll();
    }

}
