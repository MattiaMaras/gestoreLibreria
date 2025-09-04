package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RimuoviLibroCommandTest {

    private Libreria libreria;
    private Libro libro;
    private RimuoviLibroCommand command;

    @BeforeEach
    void setUp() {
        libreria = Libreria.getInstance();
        libreria.pulisciLibreria();
        libro = new Libro("Titolo Da Rimuovere", "Autore", "1234567890", "Giallo");
        libreria.aggiungiLibro(libro); // Partiamo con un libro nella libreria
        command = new RimuoviLibroCommand(libro, libreria);
    }

    @Test
    void executeRimuoveCorrettamenteIlLibro() {
        assertEquals(1, libreria.getLibri().size());
        command.execute(); //rimosso il libro
        assertEquals(0, libreria.getLibri().size());
    }

    @Test
    void undoRipristinaCorrettamenteIlLibroRimosso() {
        command.execute();
        assertEquals(0, libreria.getLibri().size());
        command.undo(); //di nuovo un libro nella libreria
        assertEquals(1, libreria.getLibri().size());
        assertEquals("1234567890", libreria.getLibri().get(0).getIsbn());
    }
}