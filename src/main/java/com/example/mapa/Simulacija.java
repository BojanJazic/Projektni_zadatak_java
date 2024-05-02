package src.main.java.com.example.mapa;

import src.main.java.com.example.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Simulacija {
    //zadane vrijednosti
    public static final int BROJ_POLICIJSKIH_TERMINALA = 3;
    public static final int BROJ_CARINSKIH_TERMINALA = 2;
    public static final int BROJ_AUTOBUSA = 5;
    public static final int BROJ_AUTOMOBILA = 35;
    public static final int BROJ_KAMIONA = 10;

    //dimenzije mapa
    public static final int BROJ_REDOVA_SIMULACIJA = 9;
    public static final int BROJ_KOLONA_SIMULACIJA = 19;
    public static final int BROJ_REDOVA_CEKANJE = 5;
    public static final int BROJ_KOLONA_CEKANJE = 25;



    public Mapa simulacioniDio;
    public Mapa vozilaUCekanju;
    List<Vozilo> listaVozila;
    List<Terminal> listaTerminala;

    //kontrolne staticke varijable
    public static boolean KRAJ = false;
    public static boolean PAUZA = false;


    public Simulacija(){
        //kreiraju se mape za simulaciju i za cekanje
        this.simulacioniDio = new Mapa(BROJ_REDOVA_SIMULACIJA, BROJ_KOLONA_SIMULACIJA);
        this.vozilaUCekanju = new Mapa(BROJ_REDOVA_CEKANJE, BROJ_KOLONA_CEKANJE);

        this.listaTerminala = kreirajTerminale();
        this.listaVozila = kreirajVozila();

        postaviVozilaNaGlavnuMapu();
        postaviVozilaNaMapuCekanja();

    }

    public static boolean isKRAJ() {
        return KRAJ;
    }

    public static void setKRAJ(boolean KRAJ) {
        Simulacija.KRAJ = KRAJ;
    }

    public static boolean isPAUZA() {
        return PAUZA;
    }

    public static void setPAUZA(boolean PAUZA) {
        Simulacija.PAUZA = PAUZA;
    }


    private List<Vozilo> kreirajVozila(){
        List<Vozilo> kreiranaVozila = new ArrayList<>();
        //licna vozila
        for(int i = 0; i < BROJ_AUTOMOBILA; i++){
            kreiranaVozila.add(new LicnoVozilo());
        }

        //autobusi
        for(int i = 0; i < BROJ_AUTOBUSA; i++){
            kreiranaVozila.add(new Autobus());
        }

        //kamioni
        for(int i = 0; i < BROJ_KAMIONA; i++){
            kreiranaVozila.add(new Kamion());
        }

        //poziv staticke metode za postavljanje neispravnih dokumenata u 3% putnika
        Putnik.postaviNeispravnaDokumenta();

        //poziv staticke metode koja u 20% ukupnog broja kamiona postavlja tezinu vecu od deklarisane
        Kamion.izracunajNovuTezinu();


        Collections.shuffle(kreiranaVozila);

        return kreiranaVozila;
    }
    private List<Terminal> kreirajTerminale(){
        List<Terminal> kreiraniTerminali = new ArrayList<>();
        for(int i = 0; i < BROJ_POLICIJSKIH_TERMINALA; i++){
            kreiraniTerminali.add(new PolicijskiTerminal(simulacioniDio, 2 * (i + 1), 13));
        }

        for (int i = 0; i < BROJ_CARINSKIH_TERMINALA; i++){
            kreiraniTerminali.add(new CarinskiTerminal(simulacioniDio, 2 * (i + 2), 16));
        }

        return kreiraniTerminali;
    }


    private void postaviVozilaNaGlavnuMapu(){
        int i = 0;
        for(int red = 4, kolona = 0; kolona <= 8; kolona += 2){
            Vozilo vozilo = listaVozila.get(i);
            if(vozilo != null){
                simulacioniDio.postaviVoziloNaPolje(vozilo, red, kolona);
                vozilo.setTrenutniRed(red);
                vozilo.setTrenutnaKolona(kolona);
                vozilo.setSimulacija(this);
                vozilo.setVoziloUSimulacionomDijelu(true);
                i++;
            }
        }
    }

    private void postaviVozilaNaMapuCekanja(){
        int brojac = 5;


        for(int i = 0; i < BROJ_REDOVA_CEKANJE; i ++){
            int tmp = 0;
            for(int j = 0; j <= BROJ_KOLONA_CEKANJE; j++){
                if(brojac < 50 && tmp <= 24){
                    if(i == 1){
                        Vozilo vozilo = listaVozila.get(brojac);
                        if(vozilo != null){
                            vozilaUCekanju.postaviVoziloNaPolje(vozilo, i, tmp++);
                            vozilo.setTrenutniRed(i);
                            vozilo.setTrenutnaKolona(j);
                            vozilo.setVoziloUDijeluCekanja(true);
                            vozilo.setSimulacija(this);
                            brojac++;
                        }
                    } else if (i == 3) {
                        Vozilo vozilo = listaVozila.get(brojac);
                        if(vozilo != null){
                            vozilaUCekanju.postaviVoziloNaPolje(vozilo, i, tmp++);
                            vozilo.setTrenutniRed(i);
                            vozilo.setTrenutnaKolona(j);
                            vozilo.setVoziloUDijeluCekanja(true);
                            vozilo.setSimulacija(this);
                            brojac++;
                        }
                    }
                }

            }
        }
    }

    public void launch(){
        for(Vozilo vozilo : listaVozila)
            vozilo.start();

        for (Terminal terminal : listaTerminala)
            terminal.start();
    }

}
