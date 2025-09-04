package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AggiungiLibroCommandTest {

    private Libreria libreria;
    private Libro libro;
    private Libro libroNonValido;
    private AggiungiLibroCommand command;

    @BeforeEach
    void setUp() {
        libreria = Libreria.getInstance();
        libreria.pulisciLibreria();
        libro = new Libro("Titolo Test", "Autore Test", "1234567890", "Giallo");
        libroNonValido = new Libro("Titolo Test", "Autore Test", "12345678901", "Giallo");
        command = new AggiungiLibroCommand(libro, libreria);
    }

    @Test
    void executeAggiungeCorrettamenteIlLibro() {
        assertEquals(0, libreria.getLibri().size());
        command.execute();
        assertEquals(1, libreria.getLibri().size());
        assertTrue(libreria.getLibri().contains(libro));
    }

    @Test
    void executeNonAggiungeCorrettamenteIlLibro() {
        assertEquals(0, libreria.getLibri().size());
        command = new AggiungiLibroCommand(libroNonValido, libreria);
        assertThrows(IllegalArgumentException.class, () -> command.execute());
    }

    @Test
    void undoRimuoveCorrettamenteIlLibroAggiunto() {
        command.execute();
        assertEquals(1, libreria.getLibri().size());
        command.undo();
        assertEquals(0, libreria.getLibri().size());
    }
}