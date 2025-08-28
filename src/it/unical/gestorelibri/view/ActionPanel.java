package it.unical.gestorelibri.view;

import it.unical.gestorelibri.controller.AggiungiLibroCommand;
import it.unical.gestorelibri.controller.CommandHandler;
import it.unical.gestorelibri.controller.ModificaLibroCommand;
import it.unical.gestorelibri.controller.RimuoviLibroCommand;
import it.unical.gestorelibri.core.Command;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;

import javax.swing.*;
import java.awt.FlowLayout;

public class ActionPanel {

    private final CommandHandler commandHandler;
    private final Libreria libreria;
    private final BookTablePanel tablePanel;

    private JPanel topPanel;
    private JPanel bottomPanel;

    public ActionPanel(BookTablePanel tablePanel) {
        this.commandHandler = new CommandHandler();
        this.libreria = Libreria.getInstance();
        this.tablePanel = tablePanel;

        createTopPanel();
        createBottomPanel();
    }

    private void createTopPanel() {
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Da aggiungere logica di filtro e ordinamento
        topPanel.add(new JLabel("Implementazione futura"));
    }

    private void createBottomPanel() {
        bottomPanel = new JPanel();
        JButton aggiungiButton = new JButton("Aggiungi Libro");
        JButton modificaButton = new JButton("Modifica Selezionato");
        JButton rimuoviButton = new JButton("Rimuovi Selezionato");
        JButton annullaButton = new JButton("Annulla (Undo)");
        JButton ripristinaButton = new JButton("Ripristina (Redo)");

        bottomPanel.add(aggiungiButton);
        bottomPanel.add(modificaButton);
        bottomPanel.add(rimuoviButton);
        bottomPanel.add(annullaButton);
        bottomPanel.add(ripristinaButton);

        //AGGIUNGI LIBRO
        aggiungiButton.addActionListener(e -> {
            String titolo = JOptionPane.showInputDialog(null, "Inserisci il titolo:");
            if (titolo != null && !titolo.trim().isEmpty()) {
                String autore = JOptionPane.showInputDialog(null, "Inserisci l'autore:");
                String isbn = JOptionPane.showInputDialog(null, "Inserisci l'ISBN:");
                String genere = JOptionPane.showInputDialog(null, "Inserisci il genere:");
                Libro nuovoLibro = new Libro(titolo, autore, isbn, genere); //valutazione e stato lettura hanno valore di default

                try {
                    commandHandler.eseguiComando(new AggiungiLibroCommand(nuovoLibro, libreria));
                } catch (IllegalArgumentException ex) { //eccezione lanciata dal metodo aggiungiLibro dentro Libreria se esiste già nella libreria quell'ISBN
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore ISBN", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //RIMUOVI LIBRO
        rimuoviButton.addActionListener(e -> {
            Libro libroSelezionato = tablePanel.getLibroSelezionato();
            if (libroSelezionato != null) {
                if (JOptionPane.showConfirmDialog(null, "Sei sicuro?", "Conferma Rimozione", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    commandHandler.eseguiComando(new RimuoviLibroCommand(libroSelezionato, libreria));
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleziona un libro da rimuovere.");
            }
        });

        //MODIFICA LIBRO
        modificaButton.addActionListener(e -> {
            Libro libroSelezionato = tablePanel.getLibroSelezionato();
            if (libroSelezionato != null) {
                String nuovoTitolo = JOptionPane.showInputDialog(null, "Modifica il titolo:", libroSelezionato.getTitolo());
                String nuovoAutore = JOptionPane.showInputDialog(null, "Modifica l'autore:", libroSelezionato.getAutore());
                String nuovoIsbn = JOptionPane.showInputDialog(null, "Modifica l'ISBN:", libroSelezionato.getIsbn());
                // Altri campi da aggiungere per la versione finale

                if (nuovoTitolo != null && nuovoAutore != null && nuovoIsbn != null) {
                    Libro datiNuovi = new Libro(nuovoTitolo, nuovoAutore, nuovoIsbn, libroSelezionato.getGenere());
                    //da mettere il set qui se vogliamo cambiare poi valutazione e stato lettura
                    try {
                        Command cmd = new ModificaLibroCommand(libreria, libroSelezionato, datiNuovi);
                        commandHandler.eseguiComando(cmd);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore di Modifica", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleziona un libro da modificare.");
            }
        });

        //UNDO
        annullaButton.addActionListener(e -> commandHandler.annullaUltimoComando());

        //REDO
        ripristinaButton.addActionListener(e -> commandHandler.ripristinaUltimoComando());
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public JPanel getBottomPanel() {
        return bottomPanel;
    }
}