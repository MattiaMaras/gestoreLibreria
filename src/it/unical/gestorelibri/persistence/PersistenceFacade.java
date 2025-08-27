package it.unical.gestorelibri.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PersistenceFacade {

    private static final String FILENAME = "libreria.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void salva(Libreria libreria) {
        try (FileWriter writer = new FileWriter(FILENAME)) {
            List<Libro> libri = libreria.getLibri();
            gson.toJson(libri, writer);
            System.out.println("Libreria salvata con successo su " + FILENAME);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio della libreria: " + e.getMessage());
            //possibile aggiungere un toast/pop-up da mostrare all'utente
        }
    }

    public void carica() {
        Libreria libreria = Libreria.getInstance();
        try (FileReader reader = new FileReader(FILENAME)) {
            Type tipoListaLibri = new TypeToken<ArrayList<Libro>>(){}.getType();
            List<Libro> libriCaricati = gson.fromJson(reader, tipoListaLibri);

            if (libriCaricati != null) {
                // Svuotiamo la vecchia e carichiamo la nuova
                for (Libro libro : libreria.getLibri()) {
                    libreria.rimuoviLibro(libro); //per observer
                }
                for (Libro libro : libriCaricati) {
                    libreria.aggiungiLibro(libro); // per observer
                }
            }
            System.out.println("Libreria caricata con successo da " + FILENAME);
        } catch (IOException e) {
            System.err.println("Nessun file di salvataggio trovato o errore di lettura: " + e.getMessage());
            //succede quando all'avvio il file json è vuoto, libreria vuota
        }
    }
}