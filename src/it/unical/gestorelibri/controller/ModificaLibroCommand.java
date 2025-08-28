package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.core.Command;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;

public class ModificaLibroCommand implements Command {

    private final Libreria receiver;
    private final Libro statoVecchio;
    private final Libro statoNuovo;

    public ModificaLibroCommand(Libreria receiver, Libro libroDaModificare, Libro datiNuovi) {
        this.receiver = receiver;
        this.statoVecchio = new Libro(libroDaModificare);
        this.statoNuovo = datiNuovi;
    }

    @Override
    public void execute() {
        receiver.modificaLibro(statoVecchio.getIsbn(), statoNuovo);
    }

    @Override
    public void undo() {
        receiver.modificaLibro(statoNuovo.getIsbn(), statoVecchio);
    }
}