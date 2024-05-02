package src.main.java.com.example.dokumentacija;

import src.main.java.com.example.upravljanje.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

public class RadSaCarinskomDokumentacijom {

    private static final Path CARINSKA_DOKUMENTACIJA = Paths.get("src", "main", "resources", "com", "example", "projekat", "carinskaDokumentacija", Main.podImeFajla  + "_SIMULACIJA_carinskaDokumentacija");

    public static void kreirajCarinskuDokumentaciju(CarinskaDokumentacija carinskaDokumentacija){
        dodajUFajl(carinskaDokumentacija);
    }

    private static void dodajUFajl(CarinskaDokumentacija carinskaDokumentacija){
        if(!Files.exists(CARINSKA_DOKUMENTACIJA)){
            try {
                Files.createFile(CARINSKA_DOKUMENTACIJA);
                Files.writeString(CARINSKA_DOKUMENTACIJA, carinskaDokumentacija.toString() + System.lineSeparator(), StandardOpenOption.APPEND);
            }catch (IOException ex){
                Main.LOGGER.log(Level.SEVERE, "Greska prilikom kreiranja fajla carinske dokumentacije", ex);
            }
        }else {
            try {
                Files.writeString(CARINSKA_DOKUMENTACIJA, carinskaDokumentacija.toString() + System.lineSeparator(), StandardOpenOption.APPEND);
            }catch (IOException ex){
                Main.LOGGER.log(Level.SEVERE, "Greska prilikom upisivanja u fajl", ex);
            }

        }
    }
}
