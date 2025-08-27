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
        libri.add(libro);
        notificaObserver();
    }

    public void rimuoviLibro(Libro libro) {
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
}