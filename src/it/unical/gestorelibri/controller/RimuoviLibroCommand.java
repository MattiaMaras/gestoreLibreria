package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.core.Command;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;
import it.unical.gestorelibri.model.Memento;

public class RimuoviLibroCommand implements Command {
    private final Libreria receiver;
    private Memento memento;

    //modifica,non salvo il libro ma l'isbn
    private final String isbnLibroDaRimuovere;

    //copia del libro per l'undo
    private final Libro libroRimosso;

    public RimuoviLibroCommand(Libro libro, Libreria receiver) {
        this.receiver = receiver;
        this.isbnLibroDaRimuovere = libro.getIsbn();
        this.libroRimosso = new Libro(libro);
    }

    @Override
    public void execute() {
        this.memento = receiver.creaMemento();

        Libro libroTrovato = receiver.getLibroByIsbn(isbnLibroDaRimuovere);

        if (libroTrovato != null) {
            receiver.rimuoviLibro(libroTrovato);
        }
    }

    @Override
    public void undo() {
        if (this.memento != null) {
            receiver.ripristina(this.memento);
        }
    }
}