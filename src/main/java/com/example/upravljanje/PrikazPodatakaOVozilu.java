package src.main.java.com.example.upravljanje;

import javafx.fxml.FXML;
import src.main.java.com.example.model.Kamion;
import src.main.java.com.example.model.Putnik;
import src.main.java.com.example.model.Vozilo;
import javafx.scene.control.TextArea;
import java.util.Arrays;
import java.util.List;

public class PrikazPodatakaOVozilu{

    @FXML
    public TextArea taPrikazInformacijaOVozilu;

    public void metoda(int idVozila){
        List<Vozilo> listaVozila = Vozilo.getListaSvihKreiranihVozila();
        for(Vozilo v : listaVozila){
            if(v.getId_vozila() == idVozila){
                List<Putnik> putnici = v.getListaPutnika();
                String podaci = "Ид возила: " + v.getId_vozila() + "\n" +
                                "Назив возила: " + v.getNazivVozila() + "\n" +
                                "Број путника: " + v.getBrojPutnika() + "\n" +
                                "Путници: " + Arrays.toString(putnici.toArray());
                if(v instanceof Kamion){
                    podaci += "\nДекларисана тежина: " + ((Kamion) v).getDeklarisanaTezina() + "\nСтварна тежинa: " + ((Kamion)v).getStvarnaTezina();
                }
                taPrikazInformacijaOVozilu.setText(podaci);
            }
        }
    }
}
