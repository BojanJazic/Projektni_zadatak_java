package src.main.java.com.example.upravljanje;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import src.main.java.com.example.mapa.Simulacija;
import java.io.IOException;
import java.util.logging.Level;

public class PocetnaStranica {

    public static Simulacija simulacija;

    @FXML
    Button btnPokreni;

    @FXML
    void pokreniSimulaciju(ActionEvent actionEvent){
        simulacija = new Simulacija();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../../../../resources/com/example/projekat/kontroleri/simulacija.fxml"));
            Parent root = fxmlLoader.load();

            UpravljanjeSimulacijom kontroler = fxmlLoader.getController();
            kontroler.setSimulacija(simulacija);
            kontroler.azurirajGridPane();


            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            Stage currentStage = (Stage) btnPokreni.getScene().getWindow();
            currentStage.close();

        }catch (IOException ex){
            Main.LOGGER.log(Level.SEVERE, "Greska unutar metode pokreniSimulaciju", ex);
        }

        simulacija.launch();
    }
}
