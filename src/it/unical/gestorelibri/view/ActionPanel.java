package it.unical.gestorelibri.view;

import it.unical.gestorelibri.controller.AggiungiLibroCommand;
import it.unical.gestorelibri.controller.CommandHandler;
import it.unical.gestorelibri.controller.ModificaLibroCommand;
import it.unical.gestorelibri.controller.RimuoviLibroCommand;
import it.unical.gestorelibri.controller.strategy.OrdinaPerAutore;
import it.unical.gestorelibri.controller.strategy.OrdinaPerTitolo;
import it.unical.gestorelibri.controller.strategy.OrdinaPerValutazione;
import it.unical.gestorelibri.controller.strategy.OrdinamentoStrategy;
import it.unical.gestorelibri.core.Command;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.FlowLayout;

public class ActionPanel {

    private final CommandHandler commandHandler;
    private final Libreria libreria;
    private final BookTablePanel tablePanel;

    private JPanel topPanel;
    private JPanel bottomPanel;

    private JTextField searchField;
    private JComboBox<String> filterGenereComboBox;
    private JComboBox<String> filterStatoComboBox;
    private JComboBox<String> sortComboBox;

    private OrdinamentoStrategy strategiaCorrente;

    public ActionPanel(BookTablePanel tablePanel) {
        this.commandHandler = new CommandHandler();
        this.libreria = Libreria.getInstance();
        this.tablePanel = tablePanel;

        this.strategiaCorrente = new OrdinaPerTitolo(); //come ordinamento di default usiamo il titolo del libro

        createTopPanel();
        createBottomPanel();
    }

    private void createTopPanel() {
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        topPanel.add(new JLabel("Cerca:"));
        searchField = new JTextField(15);
        topPanel.add(searchField);

        topPanel.add(new JLabel(" Genere:"));
        String[] generi = {"Tutti", "Fantasy", "Romanzo", "Saggio"};
        filterGenereComboBox = new JComboBox<>(generi);
        topPanel.add(filterGenereComboBox);

        topPanel.add(new JLabel(" Stato:"));
        String[] stati = {"Tutti", "Da leggere", "In lettura", "Letto"};
        filterStatoComboBox = new JComboBox<>(stati);
        topPanel.add(filterStatoComboBox);

        topPanel.add(new JLabel(" Ordina per:"));
        String[] sortOptions = {"Titolo", "Autore", "Valutazione"};
        sortComboBox = new JComboBox<>(sortOptions);
        topPanel.add(sortComboBox);

        DocumentListener documentListener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { tablePanel.aggiornaVista(); }
            @Override public void removeUpdate(DocumentEvent e) { tablePanel.aggiornaVista(); }
            @Override public void changedUpdate(DocumentEvent e) { tablePanel.aggiornaVista(); }
        };
        searchField.getDocument().addDocumentListener(documentListener);

        filterGenereComboBox.addActionListener(e -> tablePanel.aggiornaVista());
        filterStatoComboBox.addActionListener(e -> tablePanel.aggiornaVista());

        sortComboBox.addActionListener(e -> {
            String selected = (String) sortComboBox.getSelectedItem();
            switch (selected) {
                case "Autore": strategiaCorrente = new OrdinaPerAutore(); break;
                case "Valutazione": strategiaCorrente = new OrdinaPerValutazione(); break;
                default: strategiaCorrente = new OrdinaPerTitolo(); break;
            }
            tablePanel.aggiornaVista();
        });
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
                String isbn = JOptionPane.showInputDialog(null, "Inserisci l'ISBN (10 o 13 cifre, es. 978-8817100002):");
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
                String nuovoIsbn = JOptionPane.showInputDialog(null, "Modifica l'ISBN (10 o 13 cifre):", libroSelezionato.getIsbn());
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

    // Getter usati da BookTablePanel
    public String getSearchText() { return searchField.getText(); }
    public String getGenereSelezionato() { return (String) filterGenereComboBox.getSelectedItem(); }
    public String getStatoSelezionato() { return (String) filterStatoComboBox.getSelectedItem(); }
    public OrdinamentoStrategy getStrategiaCorrente() { return strategiaCorrente; }

    public JPanel getTopPanel() { return topPanel; }
    public JPanel getBottomPanel() { return bottomPanel; }
}