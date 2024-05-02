package src.main.java.com.example.dokumentacija;

import src.main.java.com.example.upravljanje.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class RadSaSuspenzijama {

    public static Path SUSPENDOVANI_PUTNICI = Paths.get("src", "main", "resources", "com", "example", "projekat", "serijalizovaniFajlovi", Main.podImeFajla + "_suspenzijaPutnika.ser");
    public static Path SUSPENDOVANA_VOZILA = Paths.get("src", "main", "resources", "com", "example", "projekat", "serijalizovaniFajlovi", Main.podImeFajla + "_suspenzijaVozila.ser");

    public static void suspendujVozilo(Suspenzija suspenzija){
        List<Suspenzija> suspendovanaVozila = ucitajListuSuspendovanihVozila();
        suspendovanaVozila.add(suspenzija);
        sacuvajListuSuspendovanihVozila(suspendovanaVozila);
    }

    public static void suspendujPutnika(Suspenzija suspenzija){
        List<Suspenzija> suspendovaniPutnici = ucitajListuSuspendovanihPutnika();
        suspendovaniPutnici.add(suspenzija);
        sacuvajListuSuspendovanihPutnika(suspendovaniPutnici);
    }

    public static List<Suspenzija> ucitajListuSuspendovanihVozila(){
        List<Suspenzija> suspendovanaVozila = new ArrayList<>();
        if(!Files.exists(SUSPENDOVANA_VOZILA)){
            try{
                Files.createFile(SUSPENDOVANA_VOZILA);
            }catch (IOException ex){
                Main.LOGGER.log(Level.SEVERE, "Nije moguce kreirati fajl SUSPENDOVANA_VOZILA", ex);
            }
        }else {
            try {
                if (Files.exists(SUSPENDOVANA_VOZILA) && Files.size(SUSPENDOVANA_VOZILA) != 0) {
                     ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(SUSPENDOVANA_VOZILA));
                     suspendovanaVozila = (List<Suspenzija>) objectInputStream.readObject();
                     objectInputStream.close();

                }else {
                    Main.LOGGER.log(Level.INFO, "Fajl je trenutno prazan");
                }
            }catch (IOException | ClassNotFoundException ex){
                Main.LOGGER.log(Level.SEVERE, "Greska u metodi ucitajListuSuspendovanihVozila", ex);
            }

        }

        return suspendovanaVozila;
    }

    public static List<Suspenzija> ucitajListuSuspendovanihPutnika(){
        List<Suspenzija> suspendovaniPutnici = new ArrayList<>();
        if(!Files.exists(SUSPENDOVANI_PUTNICI)){
            System.out.println("KREIRA FAJL");
            try{
                Files.createFile(SUSPENDOVANI_PUTNICI);
            }catch (IOException ex){
                Main.LOGGER.log(Level.SEVERE, "Greska u metodi ucitajListuSuspendovanihPutnika pri kreiranju fajla", ex);
            }
        }else {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(SUSPENDOVANI_PUTNICI));
                suspendovaniPutnici = (List<Suspenzija>) objectInputStream.readObject();
                objectInputStream.close();
            }catch (EOFException ex){
                Main.LOGGER.log(Level.SEVERE, "Greska u metodi ucitajListuSuspendovanihPutnika, EOF Exception", ex);
            }
            catch (IOException | ClassNotFoundException ex){
                Main.LOGGER.log(Level.SEVERE, "Greska u metodi ucitajListuSuspendovanihPutnika, IO | Class exception", ex);
            }
        }

        return suspendovaniPutnici;
    }

    private static void sacuvajListuSuspendovanihPutnika(List<Suspenzija> lista){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(SUSPENDOVANI_PUTNICI.toString(), true));
            objectOutputStream.writeObject(lista);
            objectOutputStream.flush();
            objectOutputStream.close();
        }catch (IOException ex){
            Main.LOGGER.log(Level.SEVERE, "Greska u metodi sacuvajListuSuspendovanihPutnika", ex);
        }
    }

    private static void sacuvajListuSuspendovanihVozila(List<Suspenzija> lista){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(SUSPENDOVANA_VOZILA.toString()));
            objectOutputStream.writeObject(lista);
            objectOutputStream.flush();
            objectOutputStream.close();
        }catch (IOException ex){
            Main.LOGGER.log(Level.SEVERE, "Greska u metodi sacuvajListuSuspendovanihVozila", ex);
        }
    }

}
