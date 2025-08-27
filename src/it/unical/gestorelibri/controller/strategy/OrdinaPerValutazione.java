package it.unical.gestorelibri.controller.strategy;

import it.unical.gestorelibri.model.Libro;
import java.util.Comparator;
import java.util.List;

public class OrdinaPerValutazione implements OrdinamentoStrategy {
    @Override
    public void ordina(List<Libro> libri) {
        // Ordinamento decrescente (dalla valutazione più alta alla più bassa)
        libri.sort(Comparator.comparingInt(Libro::getValutazione).reversed());
    }
}