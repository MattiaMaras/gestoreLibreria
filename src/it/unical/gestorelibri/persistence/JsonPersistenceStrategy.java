package it.unical.gestorelibri.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.unical.gestorelibri.model.Libro;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonPersistenceStrategy implements PersistenceStrategy {

    private final String filename;
    private final Gson gson;

    //costruttore di default
    public JsonPersistenceStrategy() {
        this("libreria.json");
    }

    //costruttore per test, non modifichiamo così la libreria vera
    public JsonPersistenceStrategy(String filename) {
        this.filename = filename;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void salva(List<Libro> libri) {
        try (FileWriter writer = new FileWriter(this.filename)) {
            gson.toJson(libri, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Errore durante il salvataggio della libreria:\n" + e.getMessage(), "Errore di Salvataggio", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Libro> carica() {
        try (FileReader reader = new FileReader(this.filename)) {
            Type tipoListaLibri = new TypeToken<ArrayList<Libro>>(){}.getType();
            List<Libro> libriCaricati = gson.fromJson(reader, tipoListaLibri);
            return libriCaricati != null ? libriCaricati : new ArrayList<>();
        } catch (IOException e) {
            // Non mostriamo un errore qui, perché è normale che il file non esista al primo avvio
            System.out.println("File di salvataggio non trovato. Si parte con una libreria vuota.");
            return new ArrayList<>();
        }
    }
}