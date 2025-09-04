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
import it.unical.gestorelibri.model.enums.Genere;
import it.unical.gestorelibri.model.enums.StatoLettura;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

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
        String[] opzioniGenere = new String[Genere.values().length + 1];
        opzioniGenere[0] = "Tutti";
        int i = 1;
        for (Genere g : Genere.values()) {
            opzioniGenere[i++] = g.toString();
        }
        filterGenereComboBox = new JComboBox<>(opzioniGenere);
        topPanel.add(filterGenereComboBox);

        topPanel.add(new JLabel(" Stato:"));
        String[] opzioniStato = new String[StatoLettura.values().length + 1];
        opzioniStato[0] = "Tutti";
        int j = 1;
        for (StatoLettura s : StatoLettura.values()) {
            opzioniStato[j++] = s.toString();
        }
        filterStatoComboBox = new JComboBox<>(opzioniStato);
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
            // Passiamo null perché stiamo aggiungendo un nuovo libro, dentro modifica ad esempio passiamo poi il libro selezionato da modificare
            BookDialog dialog = new BookDialog((Frame) SwingUtilities.getWindowAncestor(this.bottomPanel), null);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Libro nuovoLibro = dialog.getLibro();
                try {
                    commandHandler.eseguiComando(new AggiungiLibroCommand(nuovoLibro, libreria));
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
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
            if (libroSelezionato == null) {
                JOptionPane.showMessageDialog(null, "Seleziona un libro da modificare.");
                return;
            }

            BookDialog dialog = new BookDialog((Frame) SwingUtilities.getWindowAncestor(this.bottomPanel), libroSelezionato);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Libro datiNuovi = dialog.getLibro();
                try {
                    Command cmd = new ModificaLibroCommand(libreria, libroSelezionato, datiNuovi);
                    commandHandler.eseguiComando(cmd);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore di Modifica", JOptionPane.ERROR_MESSAGE);
                }
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