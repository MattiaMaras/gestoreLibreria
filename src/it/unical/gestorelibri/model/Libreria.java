package it.unical.gestorelibri.model;

import it.unical.gestorelibri.core.Observer;
import it.unical.gestorelibri.core.Subject;
import java.util.ArrayList;
import java.util.List;

public class Libreria implements Subject {

    // --- Sezione Singleton Pattern ---
    private static Libreria istanza;

    // Il costruttore è privato per impedire la creazione di istanze dall'esterno
    private Libreria() {
        libri = new ArrayList<>();
        observers = new ArrayList<>();
    }

    // Metodo statico per ottenere l'unica istanza della classe
    public static synchronized Libreria getInstance() {
        if (istanza == null) {
            istanza = new Libreria();
        }
        return istanza;
    }
    // --- Fine Sezione Singleton ---


    private List<Libro> libri;
    private List<Observer> observers;

    public void aggiungiLibro(Libro libro) {
        libri.add(libro);
        System.out.println("Libro aggiunto: " + libro.getTitolo());
        notificaObserver();
    }

    public void rimuoviLibro(Libro libro) {
        libri.remove(libro);
        System.out.println("Libro rimosso: " + libro.getTitolo());
        notificaObserver();
    }

    public List<Libro> getLibri() {
        return new ArrayList<>(libri);
    }


    // --- Sezione Subject Pattern ---
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
    // --- Fine Sezione Subject ---


    // --- Sezione Memento Pattern ---
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
    // --- Fine Sezione Memento ---
}