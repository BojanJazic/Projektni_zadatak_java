package src.main.java.com.example.dokumentacija;

import src.main.java.com.example.model.Kamion;
import src.main.java.com.example.model.Putnik;
import src.main.java.com.example.model.Terminal;
import src.main.java.com.example.model.Vozilo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Suspenzija implements Serializable {

    String vozilo;
    String putnik;
    String terminal;
    String vozac;
    String razlogSuspenzije;

    String datumIVrijeme;

    public Suspenzija(Vozilo vozilo, Putnik putnik, Terminal terminal, String razlogSuspenzije){
        this.vozilo = vozilo.getNazivVozila();
        if(vozilo instanceof Kamion)
            vozac = vozilo.getVozac().getIme() + " " + vozilo.getVozac().getPrezime();
        else
            this.putnik = putnik.getIme() + " " + putnik.getPrezime();
        this.terminal = terminal.getNazivTerminala();
        this.razlogSuspenzije = razlogSuspenzije;

        LocalDateTime trenutnoVrijeme = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss__dd.MM.yyyy");
        this.datumIVrijeme = trenutnoVrijeme.format(formatter);
    }

    @Override
    public String toString(){



        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append("Терминал: ").append(terminal).append("\n");
        stringBuilder.append("Возило: ").append(vozilo).append("\n");
        if(vozac != null)
            stringBuilder.append("Возач: ").append(vozac).append("\n");
        if(putnik != null)
            stringBuilder.append("Путник: ").append(putnik).append("\n");
        stringBuilder.append("Разлог суспензије: ").append(razlogSuspenzije).append("\n");
        stringBuilder.append("Датум и вријеме: ").append(datumIVrijeme).append("\n");

        return stringBuilder.toString();
    }
}
