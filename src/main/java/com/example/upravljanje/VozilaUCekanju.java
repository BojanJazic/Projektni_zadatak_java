package src.main.java.com.example.upravljanje;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import src.main.java.com.example.mapa.Simulacija;

import java.net.URL;
import java.util.ResourceBundle;

public class VozilaUCekanju implements Initializable {



    @FXML
    VBox vbPrikazVozilaUCekanju;

    private int intervalTrajanja = 50;
    private GridPane gridPane;
    public Simulacija simulacija;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        gridPane = generisiMapuCekanja();
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        vbPrikazVozilaUCekanju.getChildren().add(gridPane);
    }

    public Simulacija getSimulacija() {
        return simulacija;
    }

    public void setSimulacija(Simulacija simulacija) {
        this.simulacija = simulacija;
    }

    public void azurirajGridPane(){
        Timeline timeline = AzuriranjeGUIMape.generisiGridTimeline(gridPane, simulacija.vozilaUCekanju, intervalTrajanja);
        timeline.play();

    }
    private GridPane generisiMapuCekanja(){
        GridPane grid = new GridPane();

        for(int i = 0; i < Simulacija.BROJ_REDOVA_CEKANJE; i++){
            for(int j = 0; j < Simulacija.BROJ_KOLONA_CEKANJE; j++){
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(48.0);
                rectangle.setHeight(49.0);

                if(i == 1 || i == 3){
                    rectangle.setFill(Color.GRAY);
                } else if (i == 2 && j == 0) {
                    rectangle.setFill(Color.GRAY);
                } else {
                    rectangle.setFill(Color.LIGHTGRAY);
                }

                GridPane.setRowIndex(rectangle, i);
                GridPane.setColumnIndex(rectangle, j);

                grid.getChildren().add(rectangle);
            }
        }
        grid.setGridLinesVisible(true);

        return grid;
    }
}
