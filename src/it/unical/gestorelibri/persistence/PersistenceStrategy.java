package it.unical.gestorelibri.persistence;

import it.unical.gestorelibri.model.Libro;
import java.util.List;

public interface PersistenceStrategy {

    void salva(List<Libro> libri);

    List<Libro> carica();

}