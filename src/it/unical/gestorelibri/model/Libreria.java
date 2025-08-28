package it.unical.gestorelibri.model;

import it.unical.gestorelibri.core.Observer;
import it.unical.gestorelibri.core.Subject;
import java.util.ArrayList;
import java.util.List;

public class Libreria implements Subject {

    // --- Sezione Singleton Pattern ---
    private static Libreria istanza;
    private List<Libro> libri;
    private List<Observer> observers;

    private Libreria() {
        libri = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public static synchronized Libreria getInstance() {
        if (istanza == null) {
            istanza = new Libreria();
        }
        return istanza;
    }

    public void aggiungiLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("Il libro non può essere nullo.");
        }
        //Aggiungo il vincolo di unicità, mi serve per ModificaLibroCommand
        if (isbnEsiste(libro.getIsbn())) {
            throw new IllegalArgumentException("Un libro con questo ISBN è già presente nella libreria.");
        }
        libri.add(libro);
        notificaObserver();
    }

    private boolean isbnEsiste(String isbn) {
        for (Libro libro : this.libri) {
            if (libro.getIsbn().equalsIgnoreCase(isbn)) {
                return true;
            }
        }
        return false;
    }

    public void rimuoviLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("Tetnativo di rimuovere un libro nullo.");
        }
        libri.remove(libro);
        notificaObserver();
    }

    /**
     * Metodo per svuotare la libreria. Utile principalmente per i test
     * per garantire che ogni test parta da uno stato pulito.
     */
    public void pulisciLibreria() {
        if (!libri.isEmpty()) {
            libri.clear();
            notificaObserver();
        }
    }

    public List<Libro> getLibri() {
        return new ArrayList<>(libri);
    }

    @Override
    public void registraObserver(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void rimuoviObserver(Observer obs) {
        observers.remove(obs);
    }

    @Override
    public void notificaObserver() {
        for (Observer observer : observers) {
            observer.update();
        }
    }


    public Memento creaMemento() {
        List<Libro> statoDaSalvare = new ArrayList<>();
        for (Libro libro : this.libri) {
            statoDaSalvare.add(new Libro(libro));
        }
        return new Memento(statoDaSalvare);
    }

    public void ripristina(Memento m) {
        this.libri = new ArrayList<>(m.getStato());
        System.out.println("Stato della libreria ripristinato.");
        notificaObserver();
    }

    public void caricaDati(List<Libro> libriDaCaricare) {
        this.libri = new ArrayList<>(libriDaCaricare);
        notificaObserver(); // Notifica la vista che i dati sono stati caricati
    }

    public void modificaLibro(String isbn, Libro statoNuovo) {
        if (statoNuovo == null) {
            throw new IllegalArgumentException("Il nuovo stato del libro non può essere nullo.");
        }

        // Controlliamo se il nuovo ISBN è valido e unico
        String nuovoIsbn = statoNuovo.getIsbn();
        if (!isbn.equalsIgnoreCase(nuovoIsbn) && isbnEsiste(nuovoIsbn)) {
            throw new IllegalArgumentException("Un altro libro con questo nuovo ISBN è già presente.");
        }

        // Troviamo il libro da modificare (ISBN è identificativo)
        Libro libroDaModificare = getLibroByIsbn(isbn);
        if (libroDaModificare != null) {
            libroDaModificare.setTitolo(statoNuovo.getTitolo());
            libroDaModificare.setAutore(statoNuovo.getAutore());
            libroDaModificare.setIsbn(statoNuovo.getIsbn());
            libroDaModificare.setGenere(statoNuovo.getGenere());
            libroDaModificare.setValutazione(statoNuovo.getValutazione());
            libroDaModificare.setStatoLettura(statoNuovo.getStatoLettura());

            notificaObserver();
        } else {
            throw new IllegalArgumentException("Nessun libro trovato con ISBN: " + isbn);
        }
    }

    public Libro getLibroByIsbn(String isbn) {
        for (Libro libro : this.libri) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }
}