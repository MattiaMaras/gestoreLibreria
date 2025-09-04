package it.unical.gestorelibri.controller.strategy;

import it.unical.gestorelibri.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrdinamentoStrategyTest {

    private List<Libro> libri;

    @BeforeEach
    void setUp() {
        libri = new ArrayList<>(); //lista di libri non ordinati prima di ogni test -> B,C,A

        Libro libroB = new Libro("Libro B", "Autore B", "1234567890", "Giallo");
        libroB.setValutazione(3);

        Libro libroC = new Libro("Libro C", "Autore C", "1234567899", "Fantasy");
        libroC.setValutazione(5);

        Libro libroA = new Libro("Libro A", "Autore A", "1234567888", "Romanzo");
        libroA.setValutazione(1);

        libri.add(libroB);
        libri.add(libroC);
        libri.add(libroA);
    }

    @Test
    void testOrdinaPerTitolo() {
        OrdinamentoStrategy strategia = new OrdinaPerTitolo();
        strategia.ordina(libri);

        //ordine corretto: A,B,C
        assertEquals("Libro A", libri.get(0).getTitolo());
        assertEquals("Libro B", libri.get(1).getTitolo());
        assertEquals("Libro C", libri.get(2).getTitolo());
    }

    @Test
    void testOrdinaPerAutore() {
        OrdinamentoStrategy strategia = new OrdinaPerAutore();
        strategia.ordina(libri);

        //Sempre A,B,C
        assertEquals("Autore A", libri.get(0).getAutore());
        assertEquals("Autore B", libri.get(1).getAutore());
        assertEquals("Autore C", libri.get(2).getAutore());
    }

    @Test
    void testOrdinaPerValutazione() {
        OrdinamentoStrategy strategia = new OrdinaPerValutazione();
        strategia.ordina(libri);

        //ordine corretto: 5,3,1
        assertEquals(5, libri.get(0).getValutazione());
        assertEquals(3, libri.get(1).getValutazione());
        assertEquals(1, libri.get(2).getValutazione());
    }
}