package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModificaLibroCommandTest {

    private Libreria libreria;
    private Libro libroOriginale;

    @BeforeEach
    void setUp() {
        libreria = Libreria.getInstance();
        libreria.pulisciLibreria();
        libroOriginale = new Libro("Titolo Originale", "Autore Originale", "0123456789", "Giallo");
        libreria.aggiungiLibro(libroOriginale);
    }

    @Test
    void executeModificaCorrettamenteIlLibro() {
        Libro datiNuovi = new Libro("Titolo Modificato", "Autore Modificato", "0987654321", "Giallo");
        ModificaLibroCommand command = new ModificaLibroCommand(libreria, libroOriginale, datiNuovi);
        command.execute();
        Libro libroModificato = libreria.getLibroByIsbn("0987654321");
        assertNotNull(libroModificato);
        assertEquals("Titolo Modificato", libroModificato.getTitolo());
        assertEquals("Autore Modificato", libroModificato.getAutore());
    }

    @Test
    void undoRipristinaCorrettamenteIlLibro() {
        Libro datiNuovi = new Libro("Titolo Modificato", "Autore Modificato", "0987654321", "Giallo");
        ModificaLibroCommand command = new ModificaLibroCommand(libreria, libroOriginale, datiNuovi);
        command.execute();
        command.undo();
        Libro libroRipristinato = libreria.getLibroByIsbn("0123456789");
        assertNotNull(libroRipristinato);
        assertEquals("Titolo Originale", libroRipristinato.getTitolo());
        assertEquals("Autore Originale", libroRipristinato.getAutore());
        assertNull(libreria.getLibroByIsbn("0987654321"));
    }
}