package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.core.Command;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;
import it.unical.gestorelibri.model.Memento;

public class ModificaLibroCommand implements Command {

    private final Libreria receiver;
    private Memento memento;

    // Dati per la modifica, prima versione di prova per testare il funzionamento, da cambiare poi con i dati che mette l'utente nel form
    private final Libro libroDaModificare;
    private final String nuovoTitolo;
    private final String nuovoAutore;
    private final String nuovoIsbn;
    private final String nuovoGenere;
    private final int nuovaValutazione;
    private final String nuovoStatoLettura;


    public ModificaLibroCommand(Libreria receiver, Libro libroDaModificare, String nuovoTitolo, String nuovoAutore, String nuovoIsbn, String nuovoGenere, int nuovaValutazione, String nuovoStatoLettura) {
        this.receiver = receiver;
        this.libroDaModificare = libroDaModificare;
        this.nuovoTitolo = nuovoTitolo;
        this.nuovoAutore = nuovoAutore;
        this.nuovoIsbn = nuovoIsbn;
        this.nuovoGenere = nuovoGenere;
        this.nuovaValutazione = nuovaValutazione;
        this.nuovoStatoLettura = nuovoStatoLettura;
    }

    @Override
    public void execute() {
        this.memento = receiver.creaMemento();
        libroDaModificare.setTitolo(nuovoTitolo);
        libroDaModificare.setAutore(nuovoAutore);
        libroDaModificare.setIsbn(nuovoIsbn);
        libroDaModificare.setGenere(nuovoGenere);
        libroDaModificare.setValutazione(nuovaValutazione);
        libroDaModificare.setStatoLettura(nuovoStatoLettura);

        receiver.notificaObserver();
    }

    @Override
    public void undo() {
        if (this.memento != null) {
            receiver.ripristina(this.memento);
        }
    }
}