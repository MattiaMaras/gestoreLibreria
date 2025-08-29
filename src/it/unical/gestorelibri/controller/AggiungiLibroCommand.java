package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.core.Command;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;
import it.unical.gestorelibri.model.Memento;

import static it.unical.gestorelibri.utils.ValidationUtils.isIsbnValido;

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
        if (!isIsbnValido(libroDaAggiungere.getIsbn())) {
            throw new IllegalArgumentException("Il formato dell'ISBN non è valido.");
        }
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