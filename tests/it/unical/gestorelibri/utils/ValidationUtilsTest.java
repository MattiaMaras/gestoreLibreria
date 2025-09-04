package it.unical.gestorelibri.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "9788804680871",       // ISBN-13 Valido
            "8845210423",          // ISBN-10 Valido
            "884520705X",          // ISBN-10 Valido con X
            "978-88-452-7950-5"   // ISBN-13 Valido con trattini
    })
    void testIsIsbnValido_conIsbnValidi(String isbn) {
        assertTrue(ValidationUtils.isIsbnValido(isbn), "L'ISBN " + isbn + " dovrebbe essere valido");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345",               // Troppo corto
            "12345678901",         // Lunghezza sbagliata
            "978880468087A",       // Lettera non valida in ISBN-13
            "884521042X1",         // X non in ultima posizione
            "ABCDEFGHIJ",          // Solo lettere
            ""                     // Stringa vuota
    })
    void testIsIsbnValido_conIsbnNonValidi(String isbn) {
        assertFalse(ValidationUtils.isIsbnValido(isbn), "L'ISBN " + isbn + " non dovrebbe essere valido");
    }
}