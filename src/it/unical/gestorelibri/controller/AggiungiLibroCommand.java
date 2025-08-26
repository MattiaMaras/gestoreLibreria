package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.core.Command;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;
import it.unical.gestorelibri.model.Memento;

public class AggiungiLibroCommand implements Command {

    private final Libro libroDaAggiungere;
    private final Libreria receiver;
    private Memento memento;

    public AggiungiLibroCommand(Libro libro, Libreria receiver) {
        this.libroDaAggiungere = libro;
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        this.memento = receiver.creaMemento();
        receiver.aggiungiLibro(libroDaAggiungere);
    }

    @Override
    public void undo() {
        if (this.memento != null) {
            receiver.ripristina(this.memento);
        }
    }
}