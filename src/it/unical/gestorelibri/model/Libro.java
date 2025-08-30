package it.unical.gestorelibri.model;

public class Libro {
    private String titolo;
    private String autore;
    private String isbn;
    private String genere;
    private int valutazione; // da 1 a 5
    private String statoLettura; // "da leggere", "in lettura", "letto"

    public Libro(String titolo, String autore, String isbn, String genere) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn.replace("-", "").trim(); //normalizzo ISBN prima di salvarlo
        this.genere = genere;
        this.valutazione = 0; // Valore di default
        this.statoLettura = "Da leggere"; // Valore di default
    }

    public Libro(Libro Libro) {
        this.titolo = Libro.titolo;
        this.autore = Libro.autore;
        this.isbn = Libro.isbn;
        this.genere = Libro.genere;
        this.valutazione = Libro.valutazione;
        this.statoLettura = Libro.statoLettura;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn.replace("-", "").trim();
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    public String getStatoLettura() {
        return statoLettura;
    }

    public void setStatoLettura(String statoLettura) {
        this.statoLettura = statoLettura;
    }
}