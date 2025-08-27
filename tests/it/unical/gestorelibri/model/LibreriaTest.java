package it.unical.gestorelibri.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibreriaTest {

    private Libreria libreria;
    private Libro libroTest;

    @BeforeEach
    void setUp() {
        libreria = Libreria.getInstance();
        libreria.pulisciLibreria();
        libroTest = new Libro("Il Signore degli Anelli", "J.R.R. Tolkien", "8845210423", "Fantasy");
    }

    //vari casi

    @Test
    @DisplayName("Aggiungere un libro aumenta la dimensione della libreria")
    void testAggiungiLibroAumentaDimensione() {
        // Pre-condizione: la libreria è vuota (garantito da setUp)
        assertEquals(0, libreria.getLibri().size());

        // Azione: aggiungiamo un libro
        libreria.aggiungiLibro(libroTest);

        // Post-condizione (Assert): verifichiamo il risultato atteso
        assertEquals(1, libreria.getLibri().size(), "La dimensione dovrebbe essere 1 dopo l'aggiunta");
        assertTrue(libreria.getLibri().contains(libroTest), "La libreria dovrebbe contenere il libro aggiunto");
    }

    @Test
    @DisplayName("Rimuovere un libro diminuisce la dimensione della libreria")
    void testRimuoviLibroDiminuisceDimensione() {
        // Setup specifico per questo test
        libreria.aggiungiLibro(libroTest);
        assertEquals(1, libreria.getLibri().size());

        // Azione: rimuoviamo il libro
        libreria.rimuoviLibro(libroTest);

        // Assert: verifichiamo il risultato
        assertEquals(0, libreria.getLibri().size(), "La dimensione dovrebbe essere 0 dopo la rimozione");
        assertFalse(libreria.getLibri().contains(libroTest), "La libreria non dovrebbe più contenere il libro");
    }

    @Test
    @DisplayName("Aggiungere un libro nullo dovrebbe lanciare un'eccezione")
    void testAggiungiLibroNullo() {
        // Verifichiamo che il metodo lanci un'eccezione di tipo IllegalArgumentException quando proviamo ad aggiungere un libro nullo.
        assertThrows(IllegalArgumentException.class, () -> {
            libreria.aggiungiLibro(null);
        });
    }
}