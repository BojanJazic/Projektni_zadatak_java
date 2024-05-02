package src.main.java.com.example.upravljanje;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import src.main.java.com.example.mapa.Mapa;
import src.main.java.com.example.mapa.Simulacija;
import src.main.java.com.example.model.Vozilo;

public class AzuriranjeGUIMape {

    public static Timeline generisiGridTimeline(GridPane grid, Mapa mapa, int interval){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(interval), actionEvent -> {
            azurirajMapu(grid, mapa);
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        if(UpravljanjeSimulacijom.UKUPAN_BROJ_VOZILA == 0){
            Simulacija.setKRAJ(true);
            timeline.stop();
            return timeline;
        }

        return timeline;
    }

    public static void azurirajMapu(GridPane grid, Mapa mapa) {


        for (Node n : grid.getChildren()) {

            int red;
            int kolona;

            if (n instanceof Rectangle) {

                red = GridPane.getRowIndex(n);
                kolona = GridPane.getColumnIndex(n);

                Rectangle rec = (Rectangle) n;

                if (mapa.daLiJePozicijaZauzeta(red, kolona)) {

                    Vozilo vozilo = mapa.dohvatiVozilo(red,kolona);

                    if (rec.getFill().equals(Color.GRAY)) {
                        rec.setFill(mapa.dohvatiVozilo(red, kolona).getBojaVozila());
                    }
                } else if (mapa.dohvatiVozilo(red, kolona) == null &&
                        (rec.getFill().equals(Color.RED) ||
                                rec.getFill().equals(Color.GREEN) ||
                                rec.getFill().equals(Color.BLUEVIOLET))) {

                    rec.setFill(Color.GRAY);
                }
            } else if( n instanceof Label){

                red = GridPane.getRowIndex(n);
                kolona = GridPane.getColumnIndex(n);

                if((((Label) n).getText().startsWith("П") || ((Label) n).getText().startsWith("Ц"))){

                }else if (mapa.daLiJePozicijaZauzeta(red, kolona)) {

                    Vozilo vozilo = mapa.dohvatiVozilo(red,kolona);

                    StringBuilder stringBuilder = new StringBuilder();

                    char prvoSlovoNaziva = vozilo.getNazivVozila().charAt(0);
                    stringBuilder.append(prvoSlovoNaziva);

                    for (char slovo : vozilo.getNazivVozila().toCharArray()) {
                        if (Character.isDigit(slovo)) {
                            stringBuilder.append(slovo);
                        }
                    }

                    ((Label) n).setText(stringBuilder.toString());

                } else{

                    ((Label) n).setText("");

                }
            }
        }
    }
}
