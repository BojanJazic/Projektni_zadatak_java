package src.main.java.com.example.dokumentacija;

import src.main.java.com.example.model.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CarinskaDokumentacija implements Serializable {
    public static int REDNI_BROJ_DOKUMENTACIJE;
    String terminal;
    String vozilo;
    String putnik;
    int deklarisanaTezina;
    int stvarnaTezina;

    boolean daLiJeKamion = false;


    public CarinskaDokumentacija(Terminal terminal, Vozilo vozilo, Putnik putnik){
        this.terminal = terminal.getNazivTerminala();
        this.vozilo = vozilo.getNazivVozila();
        this.putnik = putnik.getIme() + " " + putnik.getPrezime();
        if(vozilo instanceof Kamion){
            deklarisanaTezina = ((Kamion) vozilo).getDeklarisanaTezina();
            stvarnaTezina = ((Kamion) vozilo).getStvarnaTezina();
            daLiJeKamion = true;
        }
        REDNI_BROJ_DOKUMENTACIJE++;
    }

    @Override
    public String toString(){
        //radi postizanja jedinstvenosti serijskog broja
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy__hh_mm_ss");
        String formatiranoVrijeme = localDateTime.format(dateTimeFormatter);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append("СЕРИЈСКИ БРОЈ ДОКУМЕНТАЦИЈЕ: ").append(REDNI_BROJ_DOKUMENTACIJE).append("_").append(formatiranoVrijeme).append("\n");
        stringBuilder.append("Терминал: ").append(terminal).append("\n");
        stringBuilder.append("Возило: ").append(vozilo).append("\n");
        if(daLiJeKamion){
            stringBuilder.append("Возач: ").append(putnik).append("\n");
            if(stvarnaTezina > deklarisanaTezina)
                stringBuilder.append("Разлог: ").append(RazloziSuspenzije.ПРЕКОРАЧЕЊЕ_ДОЗВОЉЕНЕ_ТЕЖИНЕ.toString()).append("\n");
            stringBuilder.append("Декларисана тежина: ").append(deklarisanaTezina).append("\n");
            stringBuilder.append("Стварна тежина: ").append(stvarnaTezina).append("\n");
        }else {
            stringBuilder.append("Разлог: ").append(RazloziSuspenzije.ПУТНИК_ПОСЈЕДУЈЕ_НЕДОЗВОЉЕНЕ_СТВАРИ_У_КОФЕРУ.toString()).append("\n");
            stringBuilder.append("Путник: ").append(putnik).append("\n");
        }

        stringBuilder.append("Вријеме и датум: ").append(formatiranoVrijeme).append("\n");

        return stringBuilder.toString();
    }

}
