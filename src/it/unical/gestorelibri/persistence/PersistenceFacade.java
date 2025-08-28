package it.unical.gestorelibri.persistence;

import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;

import java.util.List;

public class PersistenceFacade {

    private final PersistenceStrategy strategy; //nel nostro caso può essere JSON o CSV

    public PersistenceFacade(PersistenceStrategy strategy) {
        this.strategy = strategy;
    }

    public void salva(Libreria libreria) {
        System.out.println("Salvataggio in corso con la strategia: " + strategy.getClass().getSimpleName());
        strategy.salva(libreria.getLibri());
    }

    public void carica() {
        System.out.println("Caricamento in corso con la strategia: " + strategy.getClass().getSimpleName());
        List<Libro> libriCaricati = strategy.carica();
        if (libriCaricati != null) {
            Libreria.getInstance().caricaDati(libriCaricati);
        }
    }
}