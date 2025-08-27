package it.unical.gestorelibri.controller.strategy;

import it.unical.gestorelibri.model.Libro;
import java.util.Comparator;
import java.util.List;

public class OrdinaPerAutore implements OrdinamentoStrategy {
    @Override
    public void ordina(List<Libro> libri) {
        libri.sort(Comparator.comparing(Libro::getAutore));
    }
}