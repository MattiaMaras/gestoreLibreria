package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.core.Command;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;
import it.unical.gestorelibri.model.Memento;

public class RimuoviLibroCommand implements Command {

    private final Libro libroDaRimuovere;
    private final Libreria receiver;
    private Memento memento;

    public RimuoviLibroCommand(Libro libro, Libreria receiver) {
        this.libroDaRimuovere = libro;
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        this.memento = receiver.creaMemento();
        receiver.rimuoviLibro(libroDaRimuovere);
    }

    @Override
    public void undo() {
        if (this.memento != null) {
            receiver.ripristina(this.memento);
        }
    }
}