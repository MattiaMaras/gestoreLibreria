package it.unical.gestorelibri.persistence;

import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceFacadeTest {

    private Libreria libreria;

    @TempDir
    Path tempDir; //temporanea, solo per test

    @BeforeEach
    void setUp() { //partiamo ogni volta con una libreria vuota
        libreria = Libreria.getInstance();
        libreria.pulisciLibreria();
    }

    @Test
    void testSalvaECaricaConJsonStrategy() {
        File fileDiTest = tempDir.resolve("test_libreria.json").toFile();

        Libro libro1 = new Libro("Titolo JSON", "Autore JSON", "1111111111", "Giallo");
        libreria.aggiungiLibro(libro1);
        //System.out.println(fileDiTest.getAbsolutePath());
        PersistenceFacade facade = new PersistenceFacade(new JsonPersistenceStrategy(fileDiTest.getAbsolutePath()));
        facade.salva(libreria);

        assertTrue(fileDiTest.exists(), "Il file JSON di test dovrebbe essere stato creato invece fallisce");

        libreria.pulisciLibreria();
        assertEquals(0, libreria.getLibri().size(), "La libreria dovrebbe essere vuota dopo pulisciLibreria");
        facade.carica();

        List<Libro> libriCaricati = libreria.getLibri();
        assertEquals(1, libriCaricati.size());
        assertEquals("Titolo JSON", libriCaricati.get(0).getTitolo());
    }

    @Test
    void testSalvaECaricaConCsvStrategy() {
        File fileDiTest = tempDir.resolve("test_libreria.csv").toFile();

        Libro libro1 = new Libro("Titolo CSV", "Autore CSV", "2222222222", "Saggio");
        libreria.aggiungiLibro(libro1);

        PersistenceFacade facade = new PersistenceFacade(new CsvPersistenceStrategy(fileDiTest.getAbsolutePath()));
        facade.salva(libreria);

        assertTrue(fileDiTest.exists(), "Il file CSV di test dovrebbe essere stato creato invece fallisce");

        libreria.pulisciLibreria();
        assertEquals(0, libreria.getLibri().size(), "La libreria dovrebbe essere vuota dopo pulisciLibreria");
        facade.carica();

        List<Libro> libriCaricati = libreria.getLibri();
        assertEquals(1, libriCaricati.size());
        assertEquals("Autore CSV", libriCaricati.get(0).getAutore());
    }
}