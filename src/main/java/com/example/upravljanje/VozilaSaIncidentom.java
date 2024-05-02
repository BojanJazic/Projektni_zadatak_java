package src.main.java.com.example.upravljanje;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import src.main.java.com.example.dokumentacija.RadSaSuspenzijama;
import src.main.java.com.example.dokumentacija.Suspenzija;
import src.main.java.com.example.model.Vozilo;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class VozilaSaIncidentom {

    public static int ID;

    @FXML
    public Button btnVozilaKojaSuPreslaGranicu;
    @FXML
    public Button btnVozilaKojaNisuPreslaGranicu;
    @FXML
    public TextArea taInformacije;


    @FXML
    public void prikaziVozilaKojaSuPreslaGranicu(){

        taInformacije.setText("");

        Iterator<Vozilo> iterator = Vozilo.getListaSvihKreiranihVozila().iterator();

        while (iterator.hasNext()){
            Vozilo v = iterator.next();
            if(!v.getPutniciKojiSuImaliIncident().isEmpty()){
                taInformacije.appendText("\nИНФОРМАЦИЈЕ: ");
                taInformacije.appendText("\nВозило: " + v.toString());
                taInformacije.appendText("\nПроблем са путницима: ");
                taInformacije.appendText(Arrays.toString(v.getPutniciKojiSuImaliIncident().toArray()));
            }
        }
    }

    @FXML
    public void prikaziVozilaKojaNisuPreslaGranicu(){
        List<Suspenzija> listaSuspendovanihVozila = RadSaSuspenzijama.ucitajListuSuspendovanihVozila();
        if(!listaSuspendovanihVozila.isEmpty()) {
            taInformacije.setText("");
            taInformacije.setText(Arrays.toString(listaSuspendovanihVozila.toArray()));
        }else
            taInformacije.setText("Засад нема информација!");
    }

}
