package src.main.java.com.example.model;

import javafx.scene.paint.Color;
import src.main.java.com.example.upravljanje.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class Kamion extends Vozilo{
    public static int ID_KAMIONA = 0;
    public int deklarisanaTezina = 0;
    public int stvarnaTezina = 0;
    public boolean dokumentacija = false;
    public static List<Kamion> sviKamioni = new ArrayList<>();
    public static List<Integer> deklarisaneTezineKamiona = new ArrayList<Integer>();

    static {
        try{
            Path putanja = Paths.get("src", "main", "resources", "com", "example", "projekat", "konfiguracija", "tezineKamiona.txt");

            File file = putanja.toFile();
            Scanner scanner = new Scanner(file);
            if(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] tezine = line.split(", ");
                for(String str : tezine)
                    deklarisaneTezineKamiona.add(Integer.parseInt(str));
            }
            scanner.close();
        }catch (IOException ex){
            Main.LOGGER.log(Level.SEVERE, "Greska prilikom citanja fajla u kojem su tezine", ex);
        }
    }

    public Kamion(){
        super("Камион" + ID_KAMIONA,random.nextInt(3) + 1, Color.RED);
        deklarisanaTezina = deklarisaneTezineKamiona.get(random.nextInt(deklarisaneTezineKamiona.size()));
        stvarnaTezina = random.nextInt(this.deklarisanaTezina/2) + (this.deklarisanaTezina/2);
        sviKamioni.add(this);
        ID_KAMIONA++;
    }

    public int getDeklarisanaTezina() {
        return deklarisanaTezina;
    }

    public void setDeklarisanaTezina(int deklarisanaTezina) {
        this.deklarisanaTezina = deklarisanaTezina;
    }

    public int getStvarnaTezina() {
        return stvarnaTezina;
    }

    public void setStvarnaTezina(int stvarnaTezina) {
        this.stvarnaTezina = stvarnaTezina;
    }

    public static List<Kamion> getSviKamioni() {
        return sviKamioni;
    }


    //PREPRAVKA STVARNE TEZINE U 20% KAMIONA TAKO DA IM NOVA STVARNA TEZINA BUDE DO 30% VECA OD DOZVOLJENE
    public static void izracunajNovuTezinu(){
        int postotak = (int)(ID_KAMIONA * 0.2);
        int[] izabraniKamioni = new int[postotak];
        int i = 0;
        while (i < postotak){
            int pom = random.nextInt(ID_KAMIONA);
            boolean vecPostoji = false;
            for(int j = 0; j < i; j++){
                if(izabraniKamioni[j] == pom){
                    vecPostoji = true;
                    break;
                }
            }
            if(!vecPostoji){
                Kamion k = sviKamioni.get(pom);
                int novaTezina = (int)(k.getDeklarisanaTezina() * random.nextDouble() * 0.3) + k.getDeklarisanaTezina();
                k.setStvarnaTezina(novaTezina);
                izabraniKamioni[i] = pom;
                i++;
            }
        }
    }

    @Override
    public String toString(){
        return super.toString();
    }

    @Override
    public void pomjeriVoziloNaPolicijskiTerminal() {
        if(!simulacija.simulacioniDio.daLiJePozicijaZauzeta(6, 10)){
            for(int i = 0; i < 2; i++)
                pomjeriVozilo(SmjerKretanjaVozila.GORE, simulacija.simulacioniDio);
            if (!simulacija.simulacioniDio.daLiJePozicijaZauzeta(6, 12))
                for (int j = 0; j < 2; j++)
                    pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.simulacioniDio);
        }

    }

    @Override
    public void pomjeriVoziloNaCarinskiTerminal() {
        if(trenutniRed == 6 && trenutnaKolona == 12 && !simulacija.simulacioniDio.daLiJePozicijaZauzeta(6, 15)){
            for(int i = 0; i < 3; i++){
                pomjeriVozilo(SmjerKretanjaVozila.DESNO, simulacija.simulacioniDio);
            }
        }
    }


}
