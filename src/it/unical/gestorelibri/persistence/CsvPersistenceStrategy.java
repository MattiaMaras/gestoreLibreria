package it.unical.gestorelibri.persistence;

import it.unical.gestorelibri.model.Libro;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class CsvPersistenceStrategy implements PersistenceStrategy {

    private static final String FILENAME = "libreria.csv";
    private static final String SEPARATOR = ",";
    private static final String HEADER = "titolo,autore,isbn,genere,valutazione,statoLettura";

    @Override
    public void salva(List<Libro> libri) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
            writer.println(HEADER);
            for (Libro libro : libri) {
                String riga = String.join(SEPARATOR,
                        escape(libro.getTitolo()),
                        escape(libro.getAutore()),
                        escape(libro.getIsbn()),
                        escape(libro.getGenere()),
                        String.valueOf(libro.getValutazione()),
                        escape(libro.getStatoLettura())
                );
                writer.println(riga);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Errore durante il salvataggio in CSV:\n" + e.getMessage(), "Errore di Salvataggio", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Libro> carica() {
        List<Libro> libriCaricati = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            reader.readLine(); //La riga di intestazione va saltata

            String riga;
            while ((riga = reader.readLine()) != null) {
                String[] dati = riga.split(SEPARATOR);
                if (dati.length == 6) {
                    Libro libro = new Libro(dati[0], dati[1], dati[2], dati[3]);
                    libro.setValutazione(Integer.parseInt(dati[4]));
                    libro.setStatoLettura(dati[5]);
                    libriCaricati.add(libro);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("File CSV non trovato o corrotto. Si parte con una libreria vuota.");
            return new ArrayList<>();
        }
        return libriCaricati;
    }

    // Metodo di utility per gestire eventuali virgole nei campi di testo
    private String escape(String value) {
        if (value.contains(SEPARATOR)) {
            return "\"" + value + "\"";
        }
        return value;
    }
}