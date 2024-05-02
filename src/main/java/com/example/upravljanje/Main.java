package src.main.java.com.example.upravljanje;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    public static Logger LOGGER = Logger.getLogger("LOGGER");

    static  Instant instant = Instant.now();
    static  DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_YYYY__HH_mm_ss");

    public static String podImeFajla = dateTimeFormatter.format(instant.atZone(ZoneId.systemDefault()));

    public static void main(String[] args){
        try {
            LOGGER.addHandler(new FileHandler("logs.log", false));
        }catch (IOException ex){
            LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
        }
        try {
            launch();
        }catch (Exception ex){
            LOGGER.log(Level.SEVERE, "Greska u main", ex);
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../../../../resources/com/example/projekat/kontroleri/pocetna.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Гранични прелаз");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
