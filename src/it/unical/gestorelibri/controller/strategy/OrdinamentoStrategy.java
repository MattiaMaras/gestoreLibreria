package it.unical.gestorelibri.controller.strategy;

import it.unical.gestorelibri.model.Libro;
import java.util.List;

public interface OrdinamentoStrategy {
    void ordina(List<Libro> libri);
}