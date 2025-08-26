package it.unical.gestorelibri.model;

import java.util.ArrayList;
import java.util.List;

public class Memento {
    // Lo stato è 'final' per garantire che non venga modificato dopo la creazione del Memento.
    private final List<Libro> stato;

    public Memento(List<Libro> statoDaSalvare) {
        this.stato = new ArrayList<>(statoDaSalvare);
    }

    List<Libro> getStato() {
        return stato;
    }
}