package src.main.java.com.example.model;

import src.main.java.com.example.dokumentacija.RadSaSuspenzijama;
import src.main.java.com.example.dokumentacija.Suspenzija;
import src.main.java.com.example.mapa.Mapa;
import src.main.java.com.example.mapa.Simulacija;
import src.main.java.com.example.upravljanje.Main;
import src.main.java.com.example.upravljanje.UpravljanjeSimulacijom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class PolicijskiTerminal extends Terminal{

    public PolicijskiTerminal(Mapa mapa, int red, int kolona){
        this.id = BROJAC++;
        this.nazivTerminala = "Полицијски_терминал " + id;
        this.simulacioniDio = mapa;
        this.red = red;
        this.kolona = kolona;
    }

    @Override
    public void procesirajVozilo(Vozilo vozilo) {
        synchronized (Vozilo.getLOCK()){
            if(!Simulacija.isPAUZA()){
                if(!vozilo.getVozac().isImaIspravnaDokumenta()){
                    List<Putnik> putnici = vozilo.getListaPutnika();

                    System.out.println();
                    System.out.println("Vozac vozila " + vozilo.getNazivVozila() + " ima neispravna dokumenta");
                    System.out.println();

                    Suspenzija suspenzija = new Suspenzija(vozilo, vozilo.getVozac(), this, RazloziSuspenzije.ВОЗАЧ_ИМА_НЕИСПРАВНА_ДОКУМЕНТА.toString());
                    RadSaSuspenzijama.suspendujVozilo(suspenzija);

                    for(Putnik p : putnici){
                        Suspenzija suspenzija1 = new Suspenzija(vozilo, p, this, RazloziSuspenzije.ВОЗАЧ_ИМА_НЕИСПРАВНА_ДОКУМЕНТА.toString());
                        RadSaSuspenzijama.suspendujPutnika(suspenzija1);
                    }
                    UpravljanjeSimulacijom.UKUPAN_BROJ_VOZILA--;
                    simulacioniDio.ukloniVoziloSaMape(vozilo.getTrenutniRed(), vozilo.getTrenutnaKolona());
                    return;
                }else {
                    if(vozilo instanceof LicnoVozilo || vozilo instanceof Kamion){
                        System.out.println(vozilo);
                        Iterator<Putnik> iterator = vozilo.getListaPutnika().iterator();
                        try{
                            sleep(500);
                        }catch (InterruptedException ex){
                            Main.LOGGER.log(Level.SEVERE, "Greska u metodi procesirajVozilo u klasi PolicijskiTerminal - LicnoVozilo ili Kamion", ex);
                        }
                        while (iterator.hasNext()){
                            Putnik p = iterator.next();

                            System.out.println(p);

                            if(!p.isImaIspravnaDokumenta()){

                                vozilo.setPutniciKojiSuImaliIncident(p);

                                System.out.println();
                                System.out.println("Putnik " + p.getIme()  + " " + p.getPrezime() + " IMA NEISPRAVNA DOKUMENTA");
                                System.out.println();

                                Suspenzija suspenzija = new Suspenzija(vozilo, p, this, RazloziSuspenzije.ПУТНИК_ИМА_НЕИСПРАВНА_ДОКУМЕНТА.toString());
                                RadSaSuspenzijama.suspendujPutnika(suspenzija);



                                iterator.remove();
                            }
                        }
                        List<Putnik> putniciSaIncidentom = vozilo.getPutniciKojiSuImaliIncident();
                        for(Putnik p : putniciSaIncidentom)
                            vozilo.listaPutnika.remove(p);
                    }else {
                        Iterator<Putnik> iterator = vozilo.getListaPutnika().iterator();
                        try{
                            sleep(100);
                        }catch (InterruptedException ex){
                            Main.LOGGER.log(Level.SEVERE, "Greska u metodi procesirajVozilo u klasi PolicijskiTerminal - putnici u Autobusu", ex);
                        }
                        while (iterator.hasNext()){



                            Putnik p = iterator.next();
                            System.out.println(p);


                            if(!p.isImaIspravnaDokumenta()){

                                System.out.println();
                                System.out.println("Putnik " + p.getIme()  + " " + p.getPrezime() + " IMA NEISPRAVNA DOKUMENTA");
                                System.out.println();
                                vozilo.setPutniciKojiSuImaliIncident(p);

                                Suspenzija suspenzija = new Suspenzija(vozilo, p, this, RazloziSuspenzije.ПУТНИК_ИМА_НЕИСПРАВНА_ДОКУМЕНТА.toString());
                                RadSaSuspenzijama.suspendujPutnika(suspenzija);

                                iterator.remove();
                            }
                        }

                        List<Putnik> putniciSaIncidentom = vozilo.getPutniciKojiSuImaliIncident();
                        for(Putnik p : putniciSaIncidentom)
                            vozilo.listaPutnika.remove(p);
                    }
                }


                vozilo.setVoziloProcesiranoOdStranePolicije(true);
            }else {
                try{
                    Vozilo.getLOCK().wait();
                }catch (InterruptedException ex){
                    Main.LOGGER.log(Level.SEVERE, "Greska na policijskom terminalu prilikom pauziranja simulacije", ex);
                }
            }

        }


        //kad zavrsi sa procesiranjem, obavijesti ostale
        Vozilo.getLOCK().notifyAll();
    }
}
