package src.main.java.com.example.model;

import javafx.scene.paint.Color;


public class LicnoVozilo extends Vozilo{

    public static int ID_LICNOG_VOZILA = 0;

    public LicnoVozilo(){
        super("ЛичноВозило" + ID_LICNOG_VOZILA, random.nextInt(5) + 1, Color.BLUEVIOLET);
        ID_LICNOG_VOZILA++;
    }

}
