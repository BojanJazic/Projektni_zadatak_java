package src.main.java.com.example.model;

import javafx.scene.paint.Color;


public class Autobus extends Vozilo{
    public static int ID_AUTOBUSA = 0;
    public Autobus(){
        super("Аутобус" + ID_AUTOBUSA, random.nextInt(52) + 1, Color.GREEN);

        for (int i = 1; i < listaPutnika.size(); i++){
            if(random.nextInt(101) < 70){
                listaPutnika.get(i).setImaKofer(true);
                if(random.nextInt(101) > 90){
                    listaPutnika.get(i).setImaNedozvoljeneStvari(true);
                }
            }
        }

        ID_AUTOBUSA++;
    }
}
