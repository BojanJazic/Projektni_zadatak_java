package src.main.java.com.example.upravljanje;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.main.java.com.example.mapa.Simulacija;
import src.main.java.com.example.model.Vozilo;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class UpravljanjeSimulacijom implements Initializable {

    public static int UKUPAN_BROJ_VOZILA = 50;
    public static int BROJ_REDOVA = 9;
    public static int BROJ_KOLONA = 19;
    public static boolean PRITISNUTA_PAUZA = false;
    public static boolean PRITISNUT_NASTAVAK = true;

    public int intervalIzvrsavanja = 50;
    private long pocetnoVrijeme;
    private long protekloVrijeme;
    long tmp;
    long trenutnoVrijeme;
    private Timeline timeline;


    private GridPane grid;
    private Simulacija simulacija;

    @FXML
    private VBox vbGridVBox;
    @FXML
    private Button btnPauzirajSimulaciju;
    @FXML
    private Button btnNastaviSimulaciju;
    @FXML
    private Button btnVozilaKojaSuImalaIncident;
    @FXML
    private Button btnVozilaUCekanju;
    @FXML
    private Button btnPrikaziPodatke;
    @FXML
    private Label lbVrijemeIzvrsavanjaSimulacije;
    @FXML
    public Label lbVozilaKojaNisuProcesirana;
    @FXML
    public Label lbBrojVozilaNaMapiCekanja;
    @FXML
    public TextField tbPodaciZaPretragu;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grid = generisiMapu();
        VBox.setVgrow(grid, Priority.ALWAYS);
        vbGridVBox.getChildren().add(grid);

        azurirajVrijemeTrajanjaSimulacije();

        btnPauzirajSimulaciju.setOnAction(actionEvent -> pauzirajSimulaciju());
        btnNastaviSimulaciju.setOnAction(actionEvent -> nastaviSimulaciju());
        btnVozilaUCekanju.setOnAction(actionEvent -> otvoriProzorSaVozilimaUCekanju());
        btnPrikaziPodatke.setOnAction(actionEvent -> prikaziPodatkeOVozilu());
        btnVozilaKojaSuImalaIncident.setOnAction(actionEvent -> otvoriProzorSaVozilimaKojaSuImalaIncident());

    }

    public Simulacija getSimulacija() {
        return simulacija;
    }

    public void setSimulacija(Simulacija simulacija) {
        this.simulacija = simulacija;
    }

    private void azurirajVrijemeTrajanjaSimulacije(){
        pocetnoVrijeme = System.currentTimeMillis();
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    trenutnoVrijeme = System.currentTimeMillis();
                    protekloVrijeme = (trenutnoVrijeme - pocetnoVrijeme) / 1000;
                    tmp = protekloVrijeme;

                    long sati = tmp / 3600;
                    long minuti = (tmp % 3600) / 60;
                    long sekunde = tmp % 60;

                    String formatiranoVrijeme = String.format("Вријеме трајања симулације: %02d:%02d:%02d", sati, minuti, sekunde);

                    Platform.runLater(() -> lbVrijemeIzvrsavanjaSimulacije.setText(formatiranoVrijeme));
                    lbVozilaKojaNisuProcesirana.setText("Број непроцесираних возила: " + String.valueOf(UKUPAN_BROJ_VOZILA));
                    lbBrojVozilaNaMapiCekanja.setText("Број возила на мапи чекања: " + String.valueOf(Vozilo.BROJ_PREOSTALIH_VOZILA));

                    if(Simulacija.isKRAJ()){
                        timeline.stop();
                        btnPauzirajSimulaciju.setDisable(true);
                        btnNastaviSimulaciju.setDisable(true);
                    }

                })
        );

        timeline.setCycleCount(Animation.INDEFINITE);

        timeline.play();


    }


    public void azurirajGridPane(){
        Timeline timeline1 = AzuriranjeGUIMape.generisiGridTimeline(grid, simulacija.simulacioniDio, intervalIzvrsavanja);
        timeline1.play();

    }
    @FXML
    public void pauzirajSimulaciju(){
        if(!PRITISNUTA_PAUZA){
            timeline.pause();
            protekloVrijeme = System.currentTimeMillis() - pocetnoVrijeme;
            PRITISNUTA_PAUZA = true;
            Simulacija.setPAUZA(true);
            btnNastaviSimulaciju.setDisable(false);
            btnPauzirajSimulaciju.setDisable(true);
        }
    }

    @FXML
    public void nastaviSimulaciju(){
        if(PRITISNUTA_PAUZA){
            pocetnoVrijeme = System.currentTimeMillis() - protekloVrijeme;
            timeline.play();
            PRITISNUTA_PAUZA = false;
            Simulacija.setPAUZA(false);
            btnPauzirajSimulaciju.setDisable(false);
            btnNastaviSimulaciju.setDisable(true);
            synchronized (Vozilo.getLOCK()){
                Vozilo.getLOCK().notifyAll();
            }

        }

    }

    private GridPane generisiMapu(){
        GridPane gridPane = new GridPane();

        for(int i = 0; i < BROJ_REDOVA; i++){
            for(int j = 0; j < BROJ_KOLONA; j++){
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(48.0);
                rectangle.setHeight(46.0);

                Label oznakaTerminala = new Label();

                if(i == 2 && j > 9 && j < 16 ){
                    if(j == 13){
                        oznakaTerminala.setText("П1");

                        rectangle.setFill(Color.CADETBLUE);
                    }else
                        rectangle.setFill(Color.GRAY);
                } else if (i == 3 && (j == 10 || j == 15)) {
                    rectangle.setFill(Color.GRAY);
                } else if (i == 4) {
                    if(j == 13){
                        oznakaTerminala.setText("П2");
                        rectangle.setFill(Color.CADETBLUE);
                    } else if (j == 16) {
                        oznakaTerminala.setText("Ц1");
                        rectangle.setFill(Color.YELLOWGREEN);
                    } else
                        rectangle.setFill(Color.GRAY);
                } else if (i == 5 && (j == 10 || j == 15)) {
                    rectangle.setFill(Color.GRAY);
                } else if (i == 6 && j > 9) {
                    if(j == 13){
                        oznakaTerminala.setText("ПК");
                        rectangle.setFill(Color.CADETBLUE);
                    } else if (j == 16) {
                        oznakaTerminala.setText("ЦК");
                        rectangle.setFill(Color.YELLOWGREEN);
                    }else
                        rectangle.setFill(Color.GRAY);
                } else {
                    rectangle.setFill(Color.LIGHTGRAY);
                }

                oznakaTerminala.setFont(Font.font("Arial", FontWeight.BOLD, 16));

                GridPane.setRowIndex(rectangle, i);
                GridPane.setColumnIndex(rectangle, j);

                gridPane.getChildren().add(rectangle);
                gridPane.add(oznakaTerminala, j, i);

                GridPane.setHalignment(oznakaTerminala, HPos.CENTER);
                GridPane.setValignment(oznakaTerminala, VPos.CENTER);
            }
        }

        gridPane.setGridLinesVisible(true);

        return gridPane;
    }

    @FXML
    public void otvoriProzorSaVozilimaUCekanju(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../../resources/com/example/projekat/kontroleri/vozilaUCekanju.fxml"));
            Parent root = fxmlLoader.load();

            VozilaUCekanju kontroler = fxmlLoader.getController();
            kontroler.setSimulacija(PocetnaStranica.simulacija);
            kontroler.azurirajGridPane();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        }catch (IOException ex){
            Main.LOGGER.log(Level.SEVERE, "Greska unutar metode pokreniSimulaciju", ex);
        }
    }

    @FXML
    public void otvoriProzorSaVozilimaKojaSuImalaIncident(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../../resources/com/example/projekat/kontroleri/vozilaSaIncidentom.fxml"));
            Parent root = fxmlLoader.load();

            VozilaSaIncidentom vozilaSaIncidentom = fxmlLoader.getController();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Возила која су имала инцидент");
            stage.setResizable(false);
            stage.show();

        }catch (IOException ex){
            Main.LOGGER.log(Level.SEVERE, "Greska prilikom otvaranja prozora za vozila sa incidentom.", ex);
        }
    }

    @FXML
    public void prikaziPodatkeOVozilu(){
        if(!tbPodaciZaPretragu.getText().isEmpty()){
            if(daLiJeBroj(tbPodaciZaPretragu.getText())){
                if(Integer.parseInt(tbPodaciZaPretragu.getText()) >= 0 && Integer.parseInt(tbPodaciZaPretragu.getText()) < 50){
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../../resources/com/example/projekat/kontroleri/prikazPodataka.fxml"));
                        Parent root = fxmlLoader.load();

                        PrikazPodatakaOVozilu prikazPodatakaOVozilu = fxmlLoader.getController();
                        prikazPodatakaOVozilu.metoda(Integer.parseInt(tbPodaciZaPretragu.getText()));

                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    }catch (IOException ex){
                        Main.LOGGER.log(Level.SEVERE, "Greska prilikom otvaranja prozora za prikaz podataka", ex);
                    }
                    tbPodaciZaPretragu.setText("");
                }else {
                    JOptionPane.showMessageDialog(null, "ИД вриједност мора бити у опсегу [0, 49].");
                    tbPodaciZaPretragu.setText("");
                }
            }else {
                JOptionPane.showMessageDialog(null, "Искључиво цјелобројни унос");
                tbPodaciZaPretragu.setText("");
            }

        }else {
            JOptionPane.showMessageDialog(null, "Текстуално поље мора бити попуњено!");
        }
    }

    public static boolean daLiJeBroj(String str) {
        // Regularni izraz koji odgovara samo ciframa
        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();
    }

}
